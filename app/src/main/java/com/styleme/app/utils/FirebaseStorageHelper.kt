package com.styleme.app.utils



import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File

object FirebaseStorageHelper {
    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadUserPhoto(userId: Int, file: File): String {
        val ref = storage.reference.child("user_photos/$userId/${System.currentTimeMillis()}_${file.name}")
        ref.putFile(Uri.fromFile(file)).await()
        return ref.downloadUrl.await().toString()
    }
}