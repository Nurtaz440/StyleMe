package com.styleme.app.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.styleme.app.api.ApiClient
import com.styleme.app.models.*
import com.styleme.app.utils.*
import kotlinx.coroutines.launch
import timber.log.Timber
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.styleme.app.firebase.FirebaseRepository
import com.styleme.app.models.HairColour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.abs

class HairColourViewModel(application: Application) : AndroidViewModel(application) {

    private val usersApi   = ApiClient.usersApi
    private val picturesApi = ApiClient.picturesApi

    private val _colours = MutableLiveData<Resource<List<HairColour>>>()
    val colours: LiveData<Resource<List<HairColour>>> = _colours

    private val _changeResult = MutableLiveData<Resource<Bitmap?>>()
    val changeResult: LiveData<Resource<Bitmap?>> = _changeResult

    val updatedPictureId = MutableLiveData<Int?>()

    fun loadColours() {
        _colours.value = Resource.Loading
        viewModelScope.launch {
            if (MockRepository.enabled) {
                MockRepository.fakeDelay(500)
                _colours.value = Resource.Success(MockRepository.fakeHairColours)
                return@launch
            }
            // Use Firebase instead of local API
            _colours.value = FirebaseRepository.getHairColours()
        }
    }


    fun changeHairColour(pictureId: Int, colour: HairColour) {
        _changeResult.value = Resource.Loading
        viewModelScope.launch {

            // ── Mock mode ─────────────────────────────────────────────────────────
            if (MockRepository.enabled) {
                MockRepository.fakeLongDelay(2000)
                val colorInt = try {
                    android.graphics.Color.parseColor(
                        if (colour.colourHash.startsWith("#")) colour.colourHash
                        else "#${colour.colourHash}"
                    )
                } catch (e: Exception) { android.graphics.Color.GRAY }
                val label = colour.colourName
                    ?.replace('_', ' ')
                    ?.replaceFirstChar { it.uppercase() } ?: "Colour"
                _changeResult.value = Resource.Success(
                    MockRepository.makePlaceholderBitmap(colorInt, label)
                )
                return@launch
            }

            // ── Real mode ─────────────────────────────────────────────────────────
            try {
                // 1 — Get picture public_id from Firestore
                val doc = withContext(Dispatchers.IO) {
                    Firebase.firestore
                        .collection("pictures")
                        .document(pictureId.toString())
                        .get()
                        .await()
                }

                val publicId = doc.getString("public_id") ?: run {
                    _changeResult.value = Resource.Error(
                        "Picture not found. Please upload your photo again."
                    )
                    return@launch
                }

                // 2 — Parse hex colour to RGB
                val hex = colour.colourHash.trimStart('#').padEnd(6, '0')
                val r = hex.substring(0, 2).toInt(16)
                val g = hex.substring(2, 4).toInt(16)
                val b = hex.substring(4, 6).toInt(16)

                // 3 — Build Cloudinary transformation URL
                val transformedUrl = CloudinaryManager
                    .applyHairColourTransformation(publicId, r, g, b)

                // 4 — Load transformed image on IO thread
                val bitmap = withContext(Dispatchers.IO) {
                    com.bumptech.glide.Glide
                        .with(getApplication<Application>())
                        .asBitmap()
                        .load(transformedUrl)
                        .submit()
                        .get()
                }

                // 5 — Save result to Firestore for persistence
                val newPicId = abs(transformedUrl.hashCode())
                withContext(Dispatchers.IO) {
                    Firebase.firestore.collection("pictures")
                        .document(newPicId.toString())
                        .set(mapOf(
                            "id"           to newPicId,
                            "url"          to transformedUrl,
                            "public_id"    to publicId,
                            "file_name"    to "hair_colour_result.jpg",
                            "date_created" to System.currentTimeMillis()
                        )).await()
                }

                // 6 — Update state
                updatedPictureId.value = newPicId
                _changeResult.value = Resource.Success(bitmap)

            } catch (e: Exception) {
                Timber.e(e)
                _changeResult.value = Resource.Error(
                    e.localizedMessage ?: "Failed to change colour"
                )
            }
        }
    }
}
