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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.styleme.app.utils.CloudinaryManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
class HairStyleViewModel(application: Application) : AndroidViewModel(application) {

    private val usersApi    = ApiClient.usersApi
    private val picturesApi = ApiClient.picturesApi

    private val _modelPictures = MutableLiveData<Resource<List<ModelPicture>>>()
    val modelPictures: LiveData<Resource<List<ModelPicture>>> = _modelPictures

    private val _changeResult = MutableLiveData<Resource<Bitmap?>>()
    val changeResult: LiveData<Resource<Bitmap?>> = _changeResult

    val updatedPictureId = MutableLiveData<Int?>()

    fun loadModelPictures() {
        _modelPictures.value = Resource.Loading
        viewModelScope.launch {
            if (MockRepository.enabled) {
                MockRepository.fakeDelay(600)
                _modelPictures.value = Resource.Success(MockRepository.fakeModelPictures)
                return@launch
            }
            try {
                val r = picturesApi.getModelPictures()
                _modelPictures.value = if (r.isSuccessful)
                    Resource.Success(r.body() ?: emptyList())
                else Resource.Error("Failed to load model pictures")
            } catch (e: Exception) {
                Timber.e(e)
                _modelPictures.value = Resource.Error(e.localizedMessage ?: "Error")
            }
        }
    }

    fun changeHairStyle(userPictureId: Int, modelPictureId: Int) {
        _changeResult.value = Resource.Loading
        viewModelScope.launch {
            if (MockRepository.enabled) {
                MockRepository.fakeLongDelay(2500)
                val model = MockRepository.fakeModelPictures.find { it.id == modelPictureId }
                val newPic = MockRepository.makeFakePicture("style_${model?.hairStyleId}.jpg")
                updatedPictureId.value = newPic.id
                _changeResult.value = Resource.Success(
                    MockRepository.makeStyleBitmap(model?.hairStyleId)
                )
                return@launch
            }
            try {
                val response = ApiClient.picturesApi.changeHairStyle(userPictureId, modelPictureId)
                if (response.isSuccessful) {
                    val body       = response.body()!!
                    val resultUrl  = body.currentPicture?.filePath
                    val newPicId   = body.currentPicture?.id ?: userPictureId

                    updatedPictureId.value = newPicId

                    if (!resultUrl.isNullOrBlank()) {
                        // Save to Firestore for persistence
                        withContext(Dispatchers.IO) {
                            Firebase.firestore.collection("pictures")
                                .document(newPicId.toString())
                                .set(mapOf(
                                    "id"           to newPicId,
                                    "url"          to resultUrl,
                                    "public_id"    to (body.currentPicture?.filePath
                                        ?.let { CloudinaryManager.extractPublicId(it) } ?: ""),
                                    "file_name"    to "hair_style_result.jpg",
                                    "date_created" to System.currentTimeMillis()
                                )).await()
                        }

                        // Load the wig overlay result image
                        val bitmap = withContext(Dispatchers.IO) {
                            com.bumptech.glide.Glide
                                .with(getApplication<Application>())
                                .asBitmap()
                                .load(resultUrl)
                                .submit()
                                .get()
                        }
                        _changeResult.value = Resource.Success(bitmap)
                    } else {
                        _changeResult.value = Resource.Error("No result image returned")
                    }
                } else {
                    _changeResult.value = Resource.Error("Failed to change style (${response.code()})")
                }
            } catch (e: Exception) {
                Timber.e(e)
                _changeResult.value = Resource.Error(e.localizedMessage ?: "Error")
            }
        }
    }

    fun getModelPictureBitmap(modelPictureId: Int, callback: (Bitmap?) -> Unit) {
        viewModelScope.launch {
            if (MockRepository.enabled) {
                MockRepository.fakeDelay(300)
                val model = MockRepository.fakeModelPictures.find { it.id == modelPictureId }
                callback(MockRepository.makeStyleBitmap(model?.hairStyleId))
                return@launch
            }
            try {
                val r = picturesApi.getModelPictureFile(modelPictureId)
                callback(if (r.isSuccessful) r.body()?.toBitmap() else null)
            } catch (e: Exception) {
                Timber.e(e); callback(null)
            }
        }
    }
}
