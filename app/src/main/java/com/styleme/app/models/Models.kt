package com.styleme.app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class User(
    val id: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val email: String
)

@Parcelize
data class Picture(
    val id: Int,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("file_path") val filePath: String?,
    @SerializedName("file_size") val fileSize: String?,
    val height: Int?,
    val width: Int?,
    @SerializedName("date_created") val dateCreated: String?,
    @SerializedName("date_updated") val dateUpdated: String?,
    val gender: String? = null
) : Parcelable

data class UploadPictureResponse(
    val picture: Picture,
    @SerializedName("face_shape") val faceShape: FaceShape?,
    @SerializedName("history_entry") val historyEntry: HistoryEntry?,
    val id: Int,
    @SerializedName("file_name") val fileName: String,
    val message: String? = null
)

@Parcelize
data class HairColour(
    val id: Int,
    @SerializedName("name") val colourName: String? = null,
    @SerializedName("html_code") val colourHash: String = "#808080",
    @SerializedName("links_to_face_shape_id") val faceShapeId: Int? = null
) : Parcelable

data class ChangeHairColourResponse(
    @SerializedName("history_entry") val historyEntry: HistoryEntry?,
    val picture: Picture?,
    @SerializedName("hair_colour") val hairColour: HairColour?,
    val message: String? = null,
    @SerializedName("file_name") val fileName: String? = null
)

@Parcelize
data class HairStyle(
    val id: Int,
    val name: String,
    val label: String?
) : Parcelable

data class ChangeHairStyleResponse(
    @SerializedName("history_entry") val historyEntry: HistoryEntry?,
    @SerializedName("hair_style") val hairStyle: HairStyle?,
    @SerializedName("current_picture") val currentPicture: Picture?,
    @SerializedName("original_picture") val originalPicture: Picture?,
    val message: String? = null,
    @SerializedName("file_name") val fileName: String? = null
)

@Parcelize
data class FaceShape(
    val id: Int,
    val name: String,
    val label: String?
) : Parcelable


@Parcelize
data class HairLength(
    val id: Int,
    val name: String,
    val label: String?,
    @SerializedName("hair_length_name") val hairLengthName: String
) : Parcelable

@Parcelize
data class ModelPicture(
    val id: Int,
    @SerializedName("file_name")      val fileName: String,
    @SerializedName("file_path")      val filePath: String?,
    @SerializedName("file_size")      val fileSize: String?,
    @SerializedName("hair_colour_id") val hairColourId: Int?,
    @SerializedName("hair_style_id")  val hairStyleId: Int?,
    @SerializedName("face_shape_id")  val faceShapeId: Int?,
    @SerializedName("hair_length_id") val hairLengthId: Int?
) : Parcelable

@Parcelize
data class HistoryEntry(
    val id: Int,
    @SerializedName("picture_id") val pictureId: Int?,
    @SerializedName("original_picture_id") val originalPictureId: Int?,
    @SerializedName("previous_picture_id") val previousPictureId: Int?,
    @SerializedName("hair_colour_id") val hairColourId: Int?,
    @SerializedName("hair_style_id") val hairStyleId: Int?,
    @SerializedName("face_shape_id") val faceShapeId: Int?,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("date_created") val dateCreated: String?,
    @SerializedName("date_updated") val dateUpdated: String?
) : Parcelable

data class LatestHistoryResponse(
    @SerializedName("history_entry") val historyEntry: HistoryEntry?,
    @SerializedName("current_picture") val currentPicture: Picture?,
    @SerializedName("original_picture") val originalPicture: Picture?,
    val history: HistoryEntry? = null, val message: String? = null
)

data class DiscardChangesResponse(
    val history: HistoryEntry?,
    val message: String? = null,
    @SerializedName("current_picture") val currentPicture: Picture?
)




