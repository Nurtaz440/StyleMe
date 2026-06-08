package com.styleme.app.api
 // ← put it in the same package as RetrofitClient

import android.content.Context
import com.styleme.app.BuildConfig
import com.styleme.app.models.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface UsersApiService {
    @GET("hair_colours")
    suspend fun getHairColours(): Response<List<HairColour>>

    @GET("hair_styles")
    suspend fun getHairStyles(): Response<List<HairStyle>>

    @GET("hair_lengths")
    suspend fun getHairLengths(): Response<List<HairLength>>

    @GET("face_shapes")
    suspend fun getFaceShapes(): Response<List<FaceShape>>
}





object ApiClient {

    private fun buildClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val usersApi: UsersApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_USERS)
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsersApiService::class.java)
    }

    val picturesApi: PicturesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_PICTURES)
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PicturesApiService::class.java)
    }
    interface PicturesApiService {

        @Multipart
        @POST("pictures")
        suspend fun uploadPicture(
            @Part file: MultipartBody.Part
        ): Response<UploadPictureResponse>

        @GET("pictures/file/{picture_id}")
        suspend fun getPictureFile(
            @Path("picture_id") pictureId: Int
        ): Response<okhttp3.ResponseBody>

        @GET("pictures/change_hair_colour/{picture_id}")
        suspend fun changeHairColour(
            @Path("picture_id") pictureId: Int,
            @Query("colour") colour: String,
            @Query("r") r: Int,
            @Query("g") g: Int,
            @Query("b") b: Int
        ): Response<ChangeHairColourResponse>

        @GET("pictures/change_hair_style")
        suspend fun changeHairStyle(
            @Query("user_picture_id")  userPictureId: Int,
            @Query("model_picture_id") modelPictureId: Int
        ): Response<ChangeHairStyleResponse>

        @DELETE("pictures/discard_changes/{original_picture_id}")
        suspend fun discardChanges(
            @Path("original_picture_id") originalPictureId: Int
        ): Response<DiscardChangesResponse>

        @GET("model_pictures")
        suspend fun getModelPictures(
            @Query("skip")  skip:  Int = 0,
            @Query("limit") limit: Int = 100
        ): Response<List<ModelPicture>>

        @GET("model_pictures/file/{model_picture_id}")
        suspend fun getModelPictureFile(
            @Path("model_picture_id") modelPictureId: Int
        ): Response<okhttp3.ResponseBody>

        @GET("history/latest")
        suspend fun getLatestHistory(): Response<LatestHistoryResponse>
    }
}
