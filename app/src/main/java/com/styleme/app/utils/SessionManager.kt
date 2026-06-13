package com.styleme.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.styleme.app.models.User

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("StyleMeSession", Context.MODE_PRIVATE)

    companion object {
        const val KEY_TOKEN = "auth_token"
        const val KEY_USER_ID = "user_id"
        const val KEY_USERNAME = "username"
        const val KEY_FIRST_NAME = "first_name"
        const val KEY_LAST_NAME = "last_name"
        const val KEY_EMAIL = "email"
        const val KEY_FACE_SHAPE_ID = "face_shape_id"
        const val KEY_HAIR_COLOUR_ID = "hair_colour_id"
        const val KEY_HAIR_COLOUR_HASH = "hair_colour_hash"
        const val KEY_HAIR_STYLE_ID = "hair_style_id"
        const val KEY_CURRENT_PICTURE_ID = "current_picture_id"
        const val KEY_CURRENT_MODEL_PICTURE_ID = "current_model_picture_id"
        const val KEY_CURRENT_PHOTO_URL = "current_photo_url"
    }

    fun saveSession(token: String, user: User) {
        prefs.edit().apply {
            putString(KEY_TOKEN, "Bearer $token")
            putInt(KEY_USER_ID, user.id)
            putString(KEY_USERNAME, user.userName)
            putString(KEY_FIRST_NAME, user.firstName)
            putString(KEY_LAST_NAME, user.lastName)
            putString(KEY_EMAIL, user.email)

            apply()
        }
    }
    fun saveCurrentPhotoUrl(url: String) { prefs.edit().putString(KEY_CURRENT_PHOTO_URL, url).apply() }
    fun getCurrentPhotoUrl(): String? = prefs.getString(KEY_CURRENT_PHOTO_URL, null)
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    fun getUsername(): String = prefs.getString(KEY_USERNAME, "") ?: ""
    fun getFirstName(): String = prefs.getString(KEY_FIRST_NAME, "") ?: ""
    fun getLastName(): String = prefs.getString(KEY_LAST_NAME, "") ?: ""
    fun getEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""
    fun getFaceShapeId(): Int? = if (prefs.contains(KEY_FACE_SHAPE_ID)) prefs.getInt(KEY_FACE_SHAPE_ID, -1) else null
    fun isLoggedIn(): Boolean = getToken() != null && getUserId() != -1

    fun getHairColourHash(): String? = prefs.getString(KEY_HAIR_COLOUR_HASH, null)
    fun getHairStyleId(): Int? = if (prefs.contains(KEY_HAIR_STYLE_ID)) prefs.getInt(KEY_HAIR_STYLE_ID, -1) else null

    fun updateFaceShape(id: Int) { prefs.edit().putInt(KEY_FACE_SHAPE_ID, id).apply() }
    fun updateHairColour(id: Int, hash: String) {
        prefs.edit().putInt(KEY_HAIR_COLOUR_ID, id).putString(KEY_HAIR_COLOUR_HASH, hash).apply()
    }
    fun updateHairStyle(id: Int) { prefs.edit().putInt(KEY_HAIR_STYLE_ID, id).apply() }

    fun saveCurrentPictureId(id: Int) { prefs.edit().putInt(KEY_CURRENT_PICTURE_ID, id).apply() }
    fun getCurrentPictureId(): Int = prefs.getInt(KEY_CURRENT_PICTURE_ID, 0)
    fun saveCurrentModelPictureId(id: Int) { prefs.edit().putInt(KEY_CURRENT_MODEL_PICTURE_ID, id).apply() }
    fun getCurrentModelPictureId(): Int = prefs.getInt(KEY_CURRENT_MODEL_PICTURE_ID, 0)

    fun clearSession() { prefs.edit().clear().apply() }
}
