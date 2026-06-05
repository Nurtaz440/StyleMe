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
                val resp = picturesApi.changeHairStyle(userPictureId, modelPictureId)
                if (resp.isSuccessful) {
                    val newId = resp.body()?.currentPicture?.id
                    updatedPictureId.value = newId
                    if (newId != null) {
                        val pic = picturesApi.getPictureFile(newId)
                        _changeResult.value = if (pic.isSuccessful)
                            Resource.Success(pic.body()?.toBitmap())
                        else Resource.Error("Style applied but failed to load image")
                    }
                } else {
                    _changeResult.value = Resource.Error("Failed to change style (${resp.code()})")
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
