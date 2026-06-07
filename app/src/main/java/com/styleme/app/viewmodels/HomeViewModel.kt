package com.styleme.app.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.styleme.app.api.ApiClient
import com.styleme.app.firebase.FirebaseRepository
import com.styleme.app.models.*
import com.styleme.app.utils.*
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val picturesApi = ApiClient.picturesApi

    private val _uploadState = MutableLiveData<Resource<UploadPictureResponse>>()
    val uploadState: LiveData<Resource<UploadPictureResponse>> = _uploadState

    private val _currentBitmap = MutableLiveData<Resource<Bitmap?>>()
    val currentBitmap: LiveData<Resource<Bitmap?>> = _currentBitmap

    private val _discardResult = MutableLiveData<Resource<DiscardChangesResponse>>()
    val discardResult: LiveData<Resource<DiscardChangesResponse>> = _discardResult

    val currentPictureId = MutableLiveData<Int?>()
    val originalPictureId = MutableLiveData<Int?>()
    val detectedFaceShape = MutableLiveData<FaceShape?>()


    var cachedBitmap: Bitmap? = null

    fun uploadPicture(uri: Uri) {
        _uploadState.value = Resource.Loading
        viewModelScope.launch {
            if (MockRepository.enabled) {
                MockRepository.fakeLongDelay(1400)
                val pic = MockRepository.makeFakePicture("my_photo.jpg")
                val shape = MockRepository.fakeFaceShapes.random()
                currentPictureId.value = pic.id
                originalPictureId.value = pic.id
                detectedFaceShape.value = shape
                cachedBitmap = MockRepository.makePlaceholderBitmap(
                    0xFF7B4FBE.toInt(), "Your Photo\nFace: ${shape.label}"
                )
                _uploadState.value = Resource.Success(
                    UploadPictureResponse(
                        picture = pic,
                        faceShape = shape,
                        historyEntry = null,
                        id = pic.id,
                        fileName = pic.fileName,
                        message = null
                    )
                )
                _currentBitmap.value = Resource.Success(cachedBitmap)
                return@launch
            }
            // Upload to Firebase Storage
            val result = FirebaseRepository.uploadPicture(uri)
            if (result is Resource.Success) {
                val body = result.data
                currentPictureId.value = body.picture.id
                originalPictureId.value = body.picture.id
                detectedFaceShape.value = body.faceShape
                _uploadState.value = Resource.Success(body)
                // Load image from Firebase Storage URL
                body.picture.filePath?.let { url ->
                    loadPictureFromUrl(url)
                }
            } else if (result is Resource.Error) {
                _uploadState.value = Resource.Error(result.message)
            }
        }
    }

    fun loadPictureFromUrl(url: String) {
        _currentBitmap.value = Resource.Loading
        viewModelScope.launch {
            try {
                // Must run on IO background thread
                val bitmap = withContext(Dispatchers.IO) {
                    com.bumptech.glide.Glide
                        .with(getApplication<Application>())
                        .asBitmap()
                        .load(url)
                        .submit()
                        .get()
                }
                cachedBitmap = bitmap
                _currentBitmap.value = Resource.Success(bitmap)
            } catch (e: Exception) {
                Timber.e(e)
                _currentBitmap.value = Resource.Error("Failed to load image")
            }
        }
    }

    fun loadPicture(pictureId: Int) {
        _currentBitmap.value = Resource.Loading
        viewModelScope.launch {
            try {
                val r = picturesApi.getPictureFile(pictureId)
                if (r.isSuccessful) {
                    val bitmap = r.body()?.byteStream()
                        ?.let { BitmapFactory.decodeStream(it) }
                    _currentBitmap.value = if (bitmap != null)
                        Resource.Success(bitmap)
                    else
                        Resource.Error("Failed to decode image")
                } else {
                    _currentBitmap.value = Resource.Error("Could not load picture (${r.code()})")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "loadPicture failed", e)
                _currentBitmap.value = Resource.Error(e.localizedMessage ?: "Error")
            }
        }
    }

    fun loadLatestHistory() {
        viewModelScope.launch {
            if (MockRepository.enabled) return@launch
            try {
                val r = picturesApi.getLatestHistory()
                if (r.isSuccessful) {
                    r.body()?.historyEntry?.let { entry ->
                        currentPictureId.value = entry.pictureId
                        originalPictureId.value = entry.originalPictureId
                        entry.pictureId?.let { loadPicture(it) }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun discardChanges() {
        val origId = originalPictureId.value ?: return
        _discardResult.value = Resource.Loading
        viewModelScope.launch {
            try {
                val r = picturesApi.discardChanges(originalPictureId = origId)
                if (r.isSuccessful) {
                    val body = r.body() ?: DiscardChangesResponse(
                        history = null,
                        message = null,
                        currentPicture = null
                    )
                    // Use the picture returned by server, fall back to original
                    val newId = body.currentPicture?.id ?: origId
                    currentPictureId.value = newId
                    _discardResult.value = Resource.Success(body)
                    loadPicture(newId)
                } else {
                    _discardResult.value = Resource.Error("Could not discard changes (${r.code()})")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "discardChanges failed", e)
                _discardResult.value = Resource.Error(e.localizedMessage ?: "Error")
            }
        }
    }

    fun seedFirestore() {
        val db = Firebase.firestore

        val colours = listOf(
            mapOf("id" to 1, "name" to "jet_black", "html_code" to "#0A0A0A"),
            mapOf("id" to 2, "name" to "dark_brown", "html_code" to "#3B1F0E"),
            mapOf("id" to 3, "name" to "medium_brown", "html_code" to "#6B3A2A"),
            mapOf("id" to 4, "name" to "light_brown", "html_code" to "#A0522D"),
            mapOf("id" to 5, "name" to "dirty_blonde", "html_code" to "#C8A96E"),
            mapOf("id" to 6, "name" to "golden_blonde", "html_code" to "#F0C040"),
            mapOf("id" to 7, "name" to "platinum_blonde", "html_code" to "#F5E6C8"),
            mapOf("id" to 8, "name" to "auburn", "html_code" to "#922B21"),
            mapOf("id" to 9, "name" to "copper_red", "html_code" to "#C0392B"),
            mapOf("id" to 10, "name" to "bright_red", "html_code" to "#E74C3C"),
            mapOf("id" to 11, "name" to "rose_gold", "html_code" to "#E8B4B8"),
            mapOf("id" to 12, "name" to "pastel_pink", "html_code" to "#FFB6C1"),
            mapOf("id" to 13, "name" to "electric_blue", "html_code" to "#1A73E8"),
            mapOf("id" to 14, "name" to "violet_purple", "html_code" to "#8E44AD"),
            mapOf("id" to 15, "name" to "silver_grey", "html_code" to "#95A5A6")
        )

        // Clear old data first then re-seed
        colours.forEach { colour ->
            db.collection("hair_colours")
                .document(colour["id"].toString())
                .set(colour)
        }
    }

    val fakeHairColours = listOf(
        HairColour(1, "jet_black", "#0A0A0A", null),
        HairColour(2, "dark_brown", "#3B1F0E", null),
        HairColour(3, "medium_brown", "#6B3A2A", null),
        HairColour(4, "light_brown", "#A0522D", null),
        HairColour(5, "dirty_blonde", "#C8A96E", null),
        HairColour(6, "golden_blonde", "#F0C040", null),
        HairColour(7, "platinum_blonde", "#F5E6C8", null),
        HairColour(8, "strawberry", "#E8846A", null),
        HairColour(9, "auburn", "#922B21", null),
        HairColour(10, "copper_red", "#C0392B", null),
        HairColour(11, "bright_red", "#E74C3C", null),
        HairColour(12, "rose_gold", "#E8B4B8", null),
        HairColour(13, "pastel_pink", "#FFB6C1", null),
        HairColour(14, "electric_blue", "#1A73E8", null),
        HairColour(15, "violet_purple", "#8E44AD", null),
        HairColour(16, "silver_grey", "#95A5A6", null)
    )
}