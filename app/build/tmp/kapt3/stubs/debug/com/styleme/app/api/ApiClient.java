package com.styleme.app.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u0010B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\b\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0011"}, d2 = {"Lcom/styleme/app/api/ApiClient;", "", "()V", "picturesApi", "Lcom/styleme/app/api/ApiClient$PicturesApiService;", "getPicturesApi", "()Lcom/styleme/app/api/ApiClient$PicturesApiService;", "picturesApi$delegate", "Lkotlin/Lazy;", "usersApi", "Lcom/styleme/app/api/UsersApiService;", "getUsersApi", "()Lcom/styleme/app/api/UsersApiService;", "usersApi$delegate", "buildClient", "Lokhttp3/OkHttpClient;", "PicturesApiService", "app_debug"})
public final class ApiClient {
    @org.jetbrains.annotations.NotNull
    private static final kotlin.Lazy usersApi$delegate = null;
    @org.jetbrains.annotations.NotNull
    private static final kotlin.Lazy picturesApi$delegate = null;
    @org.jetbrains.annotations.NotNull
    public static final com.styleme.app.api.ApiClient INSTANCE = null;
    
    private ApiClient() {
        super();
    }
    
    private final okhttp3.OkHttpClient buildClient() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.api.UsersApiService getUsersApi() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.api.ApiClient.PicturesApiService getPicturesApi() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001JI\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\t\u001a\u00020\u00062\b\b\u0001\u0010\n\u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ+\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00032\b\b\u0001\u0010\u000f\u001a\u00020\u00062\b\b\u0001\u0010\u0010\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u00032\b\b\u0001\u0010\u0014\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018J!\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\u0010\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J1\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u001c0\u00032\b\b\u0003\u0010\u001e\u001a\u00020\u00062\b\b\u0003\u0010\u001f\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J5\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010#\u001a\u00020\b2\b\b\u0001\u0010$\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%J!\u0010&\u001a\b\u0012\u0004\u0012\u00020\'0\u00032\b\b\u0001\u0010(\u001a\u00020)H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006+"}, d2 = {"Lcom/styleme/app/api/ApiClient$PicturesApiService;", "", "changeHairColour", "Lretrofit2/Response;", "Lcom/styleme/app/models/ChangeHairColourResponse;", "pictureId", "", "colour", "", "r", "g", "b", "(ILjava/lang/String;IIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "changeHairStyle", "Lcom/styleme/app/models/ChangeHairStyleResponse;", "userPictureId", "modelPictureId", "(IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "discardChanges", "Lcom/styleme/app/models/DiscardChangesResponse;", "originalPictureId", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLatestHistory", "Lcom/styleme/app/models/LatestHistoryResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getModelPictureFile", "Lokhttp3/ResponseBody;", "getModelPictures", "", "Lcom/styleme/app/models/ModelPicture;", "skip", "limit", "getPictureFile", "registerPicture", "", "url", "publicId", "(ILjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uploadPicture", "Lcom/styleme/app/models/UploadPictureResponse;", "file", "Lokhttp3/MultipartBody$Part;", "(Lokhttp3/MultipartBody$Part;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
    public static abstract interface PicturesApiService {
        
        @retrofit2.http.Multipart
        @retrofit2.http.POST(value = "pictures")
        @org.jetbrains.annotations.Nullable
        public abstract java.lang.Object uploadPicture(@retrofit2.http.Part
        @org.jetbrains.annotations.NotNull
        okhttp3.MultipartBody.Part file, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super retrofit2.Response<com.styleme.app.models.UploadPictureResponse>> $completion);
        
        @retrofit2.http.GET(value = "pictures/file/{picture_id}")
        @org.jetbrains.annotations.Nullable
        public abstract java.lang.Object getPictureFile(@retrofit2.http.Path(value = "picture_id")
        int pictureId, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super retrofit2.Response<okhttp3.ResponseBody>> $completion);
        
        @retrofit2.http.GET(value = "pictures/change_hair_colour/{picture_id}")
        @org.jetbrains.annotations.Nullable
        public abstract java.lang.Object changeHairColour(@retrofit2.http.Path(value = "picture_id")
        int pictureId, @retrofit2.http.Query(value = "colour")
        @org.jetbrains.annotations.NotNull
        java.lang.String colour, @retrofit2.http.Query(value = "r")
        int r, @retrofit2.http.Query(value = "g")
        int g, @retrofit2.http.Query(value = "b")
        int b, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super retrofit2.Response<com.styleme.app.models.ChangeHairColourResponse>> $completion);
        
        @retrofit2.http.GET(value = "pictures/change_hair_style")
        @org.jetbrains.annotations.Nullable
        public abstract java.lang.Object changeHairStyle(@retrofit2.http.Query(value = "user_picture_id")
        int userPictureId, @retrofit2.http.Query(value = "model_picture_id")
        int modelPictureId, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super retrofit2.Response<com.styleme.app.models.ChangeHairStyleResponse>> $completion);
        
        @retrofit2.http.DELETE(value = "pictures/discard_changes/{original_picture_id}")
        @org.jetbrains.annotations.Nullable
        public abstract java.lang.Object discardChanges(@retrofit2.http.Path(value = "original_picture_id")
        int originalPictureId, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super retrofit2.Response<com.styleme.app.models.DiscardChangesResponse>> $completion);
        
        @retrofit2.http.GET(value = "model_pictures")
        @org.jetbrains.annotations.Nullable
        public abstract java.lang.Object getModelPictures(@retrofit2.http.Query(value = "skip")
        int skip, @retrofit2.http.Query(value = "limit")
        int limit, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.styleme.app.models.ModelPicture>>> $completion);
        
        @retrofit2.http.GET(value = "model_pictures/file/{model_picture_id}")
        @org.jetbrains.annotations.Nullable
        public abstract java.lang.Object getModelPictureFile(@retrofit2.http.Path(value = "model_picture_id")
        int modelPictureId, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super retrofit2.Response<okhttp3.ResponseBody>> $completion);
        
        @retrofit2.http.GET(value = "history/latest")
        @org.jetbrains.annotations.Nullable
        public abstract java.lang.Object getLatestHistory(@org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super retrofit2.Response<com.styleme.app.models.LatestHistoryResponse>> $completion);
        
        @retrofit2.http.GET(value = "pictures/register")
        @org.jetbrains.annotations.Nullable
        public abstract java.lang.Object registerPicture(@retrofit2.http.Query(value = "picture_id")
        int pictureId, @retrofit2.http.Query(value = "url")
        @org.jetbrains.annotations.NotNull
        java.lang.String url, @retrofit2.http.Query(value = "public_id")
        @org.jetbrains.annotations.NotNull
        java.lang.String publicId, @org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
        public static final class DefaultImpls {
        }
    }
}