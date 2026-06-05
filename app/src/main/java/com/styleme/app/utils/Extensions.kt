package com.styleme.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun View.visible()   { visibility = View.VISIBLE }
fun View.gone()      { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun Fragment.toast(msg: String) =
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

fun Context.toast(msg: String) =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun ResponseBody.toBitmap(): Bitmap? = try {
    BitmapFactory.decodeStream(byteStream())
} catch (e: Exception) {
    Timber.e(e, "Failed to decode bitmap"); null
}

fun Uri.toByteArray(context: Context): ByteArray? = try {
    val input: InputStream? = context.contentResolver.openInputStream(this)
    val out = ByteArrayOutputStream()
    input?.use { stream ->
        val buf = ByteArray(4 * 1024)
        var read: Int
        while (stream.read(buf).also { read = it } != -1) out.write(buf, 0, read)
    }
    out.toByteArray()
} catch (e: Exception) {
    Timber.e(e, "Failed to read URI bytes"); null
}
