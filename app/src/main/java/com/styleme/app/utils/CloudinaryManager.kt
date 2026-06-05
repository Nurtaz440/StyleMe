package com.styleme.app.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CloudinaryManager {

    // ── Replace these 3 values with your Cloudinary dashboard values ──────────
    private const val CLOUD_NAME    = "dkfgv62gg"    // e.g. "dxyz1234"
    private const val API_KEY       = "298492148631448"        // e.g. "123456789012345"
    private const val API_SECRET    = "lSwL_BTV_KinAXsTGFTXnZgAGvY"     // e.g. "abcXYZ123..."
    private const val UPLOAD_PRESET = "styleme_upload"      // preset you created
    // ──────────────────────────────────────────────────────────────────────────

    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        try {
            val config = mapOf(
                "cloud_name" to CLOUD_NAME,
                "api_key"    to API_KEY,
                "api_secret" to API_SECRET
            )
            MediaManager.init(context, config)
            initialized = true
            Timber.d("CloudinaryManager initialized successfully")
        } catch (e: Exception) {
            Timber.e(e, "CloudinaryManager init failed")
        }
    }

    /**
     * Upload an image URI to Cloudinary.
     * Returns the secure HTTPS URL of the uploaded image.
     */
    suspend fun uploadImage(
        uri: Uri,
        folder: String = "styleme/user_photos"
    ): String = suspendCoroutine { continuation ->
        MediaManager.get()
            .upload(uri)
            .option("folder", folder)
            .option("upload_preset", UPLOAD_PRESET)
            .option("resource_type", "image")
            .callback(object : UploadCallback {

                override fun onStart(requestId: String) {
                    Timber.d("Cloudinary upload started: $requestId")
                }

                override fun onProgress(
                    requestId: String,
                    bytes: Long,
                    totalBytes: Long
                ) {
                    val percent = if (totalBytes > 0) (bytes * 100 / totalBytes) else 0
                    Timber.d("Cloudinary upload progress: $percent%")
                }

                override fun onSuccess(
                    requestId: String,
                    resultData: Map<*, *>
                ) {
                    val url = resultData["secure_url"] as? String
                    val publicId = resultData["public_id"] as? String
                    Timber.d("Cloudinary upload success. URL: $url, PublicId: $publicId")
                    if (url != null) {
                        continuation.resume(url)
                    } else {
                        continuation.resumeWithException(
                            Exception("Cloudinary returned no URL")
                        )
                    }
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    Timber.e("Cloudinary upload error: ${error.description}")
                    continuation.resumeWithException(
                        Exception("Upload failed: ${error.description}")
                    )
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    Timber.w("Cloudinary upload rescheduled: ${error.description}")
                    continuation.resumeWithException(
                        Exception("Upload rescheduled: ${error.description}")
                    )
                }
            })
            .dispatch()
    }

    /**
     * Get public_id from a Cloudinary URL.
     * URL format: https://res.cloudinary.com/cloud/image/upload/v123/folder/filename.jpg
     * Returns: folder/filename  (without extension)
     */
    fun extractPublicId(cloudinaryUrl: String): String {
        return try {
            cloudinaryUrl
                .substringAfter("/upload/")
                .let {
                    // Remove version prefix like v1234567890/
                    if (it.matches(Regex("v\\d+/.*"))) it.substringAfter("/")
                    else it
                }
                .substringBeforeLast(".")
        } catch (e: Exception) {
            Timber.e(e, "Failed to extract public_id from URL: $cloudinaryUrl")
            cloudinaryUrl
        }
    }

    /**
     * Generate a Cloudinary URL that applies a hair colour tint overlay.
     * Uses Cloudinary's e_colorize transformation — no local PC needed!
     *
     * @param publicId  The Cloudinary public_id of the image
     * @param r         Red value (0-255)
     * @param g         Green value (0-255)
     * @param b         Blue value (0-255)
     * @param strength  Colorize strength (0-100), default 60
     */
    fun applyHairColourTransformation(
        publicId: String,
        r: Int,
        g: Int,
        b: Int,
        strength: Int = 60
    ): String {
        val hex = String.format("%02X%02X%02X", r, g, b)
        return "https://res.cloudinary.com/$CLOUD_NAME/image/upload/" +
                "e_colorize:$strength,co_rgb:$hex/$publicId"
    }

    /**
     * Generate a Cloudinary URL with basic image enhancement.
     * Useful for showing a better version of the uploaded photo.
     */
    fun applyAutoEnhancement(publicId: String): String {
        return "https://res.cloudinary.com/$CLOUD_NAME/image/upload/" +
                "e_improve,e_auto_brightness/$publicId"
    }

    /**
     * Generate a thumbnail URL from a Cloudinary public_id.
     */
    fun getThumbnailUrl(publicId: String, width: Int = 300, height: Int = 300): String {
        return "https://res.cloudinary.com/$CLOUD_NAME/image/upload/" +
                "c_fill,w_$width,h_$height/$publicId"
    }
}