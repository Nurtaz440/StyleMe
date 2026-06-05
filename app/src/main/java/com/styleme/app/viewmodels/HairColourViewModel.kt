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
            if (MockRepository.enabled) {
                MockRepository.fakeLongDelay(2000)
                val colorInt = try {
                    android.graphics.Color.parseColor(
                        if (colour.colourHash.startsWith("#")) colour.colourHash
                        else "#${colour.colourHash}"
                    )
                } catch (e: Exception) {
                    android.graphics.Color.GRAY
                }
                val label = colour.colourName
                    ?.replace('_', ' ')
                    ?.replaceFirstChar { it.uppercase() } ?: "Colour"
                _changeResult.value = Resource.Success(
                    MockRepository.makePlaceholderBitmap(colorInt, label)
                )
                return@launch
            }
            // Get local API URL from settings
            // Apply hair colour via Cloudinary — no local PC needed
            val result = FirebaseRepository.changeHairColour(pictureId, colour)

            when (result) {
                is Resource.Success -> {
                    val transformedUrl = result.data

                    // Load transformed image from Cloudinary URL
                    val bitmap = withContext(Dispatchers.IO) {
                        com.bumptech.glide.Glide
                            .with(getApplication<Application>())
                            .asBitmap()
                            .load(transformedUrl)
                            .submit()
                            .get()
                    }
                    updatedPictureId.value = pictureId
                    _changeResult.value = Resource.Success(bitmap)
                }
                is Resource.Error -> {
                    _changeResult.value = Resource.Error(result.message)
                }
                else -> {}
            }
        }
    }
}
