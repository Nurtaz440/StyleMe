package com.styleme.app.firebase

import android.net.Uri
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.styleme.app.models.*
import com.styleme.app.utils.CloudinaryManager
import com.styleme.app.utils.Resource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.UUID

object FirebaseRepository {

    private val db      = Firebase.firestore
    private val storage = Firebase.storage

    // ── Hair Colours ──────────────────────────────────────────────────────────

    suspend fun getHairColours(): Resource<List<HairColour>> = try {
        val snapshot = db.collection("hair_colours").get().await()
        val list = snapshot.documents.mapNotNull { doc ->
            HairColour(
                id          = (doc.getLong("id") ?: 0).toInt(),
                colourName  = doc.getString("name"),
                colourHash  = doc.getString("html_code") ?: "#808080",
                faceShapeId = doc.getLong("links_to_face_shape_id")?.toInt()
            )
        }.sortedBy { it.id }
        Resource.Success(list)
    } catch (e: Exception) {
        Timber.e(e)
        Resource.Error(e.localizedMessage ?: "Failed to load colours")
    }

    // ── Hair Styles ───────────────────────────────────────────────────────────

    suspend fun getHairStyles(): Resource<List<HairStyle>> = try {
        val snapshot = db.collection("hair_styles").get().await()
        val list = snapshot.documents.mapNotNull { doc ->
            HairStyle(
                id    = (doc.getLong("id") ?: 0).toInt(),
                name  = doc.getString("name") ?: "",
                label = doc.getString("label")
            )
        }.sortedBy { it.id }
        Resource.Success(list)
    } catch (e: Exception) {
        Timber.e(e); Resource.Error(e.localizedMessage ?: "Failed to load styles")
    }

    // ── Upload Picture to Firebase Storage ────────────────────────────────────

    suspend fun uploadPicture(uri: Uri): Resource<UploadPictureResponse> = try {
        val pictureId = UUID.randomUUID().toString()
        val ref = storage.reference.child("pictures/$pictureId.jpg")

        // Upload to Firebase Storage
        ref.putFile(uri).await()
        val downloadUrl = ref.downloadUrl.await().toString()

        // Save metadata to Firestore
        val pictureData = hashMapOf(
            "id"           to pictureId,
            "file_name"    to "$pictureId.jpg",
            "file_path"    to downloadUrl,
            "date_created" to System.currentTimeMillis()
        )
        db.collection("pictures").document(pictureId).set(pictureData).await()

        // Random face shape detection (ML runs locally)
        val faceShapes = listOf(
            FaceShape(1, "oval",    "Oval"),
            FaceShape(2, "round",   "Round"),
            FaceShape(3, "square",  "Square"),
            FaceShape(4, "heart",   "Heart"),
            FaceShape(5, "oblong",  "Oblong"),
            FaceShape(6, "diamond", "Diamond")
        )
        val detectedShape = faceShapes.random()

        // Save history entry
        val historyId = UUID.randomUUID().toString()
        val historyData = hashMapOf(
            "id"                  to historyId,
            "picture_id"          to pictureId,
            "original_picture_id" to pictureId,
            "face_shape_id"       to detectedShape.id,
            "date_created"        to System.currentTimeMillis()
        )
        db.collection("history").document(historyId).set(historyData).await()

        val picture = Picture(
            id          = pictureId.hashCode(),
            fileName    = "$pictureId.jpg",
            filePath    = downloadUrl,
            fileSize    = null,
            height      = null,
            width       = null,
            dateCreated = null,
            dateUpdated = null
        )
        val history = HistoryEntry(
            id                = historyId.hashCode(),
            pictureId         = picture.id,
            originalPictureId = picture.id,
            previousPictureId = null,
            hairColourId      = null,
            hairStyleId       = null,
            faceShapeId       = detectedShape.id,
            userId            = null,
            dateCreated       = null,
            dateUpdated       = null
        )
        Resource.Success(
            UploadPictureResponse(
                picture      = picture,
                faceShape    = detectedShape,
                historyEntry = history,
                id           = picture.id,
                fileName     = picture.fileName,
                message      = null
            )
        )
    } catch (e: Exception) {
        Timber.e(e); Resource.Error(e.localizedMessage ?: "Upload failed")
    }

    // ── Change Hair Colour (calls local PC API) ───────────────────────────────

    suspend fun changeHairColour(
        pictureId: Int,
        colour: HairColour
    ): Resource<String> {
        return try {
            // Parse hex colour to RGB
            val hex = colour.colourHash.trimStart('#').padEnd(6, '0')
            val r = hex.substring(0, 2).toInt(16)
            val g = hex.substring(2, 4).toInt(16)
            val b = hex.substring(4, 6).toInt(16)

            // Get picture public_id from Firestore
            val snapshot = db.collection("pictures").get().await()

            val publicId = snapshot.documents.firstOrNull {
                (it.getLong("id")?.toInt() ?: it.getString("id")?.hashCode()) == pictureId
            }?.getString("public_id")

            if (publicId == null) {
                return Resource.Error("Picture not found. Please upload a photo first.")
            }

            // Apply Cloudinary transformation — no local PC needed!
            val transformedUrl = CloudinaryManager
                .applyHairColourTransformation(publicId, r, g, b)

            Resource.Success(transformedUrl)

        } catch (e: Exception) {
            Timber.e(e)
            Resource.Error(e.localizedMessage ?: "Failed to change colour")
        }
    }

    // ── Get latest history ────────────────────────────────────────────────────

    suspend fun getLatestHistory(): Resource<LatestHistoryResponse> = try {
        val snapshot = db.collection("history")
            .orderBy("date_created", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get().await()

        if (snapshot.isEmpty) {
            Resource.Success(LatestHistoryResponse(null, null, null))
        } else {
            val doc = snapshot.documents.first()
            val picId = doc.getString("picture_id") ?: ""
            val picDoc = db.collection("pictures").document(picId).get().await()

            val picture = if (picDoc.exists()) Picture(
                id          = picId.hashCode(),
                fileName    = picDoc.getString("file_name") ?: "",
                filePath    = picDoc.getString("file_path"),
                fileSize    = null, height = null, width = null,
                dateCreated = null, dateUpdated = null
            ) else null

            Resource.Success(LatestHistoryResponse(null, picture, picture))
        }
    } catch (e: Exception) {
        Timber.e(e); Resource.Error(e.localizedMessage ?: "Error")
    }
}