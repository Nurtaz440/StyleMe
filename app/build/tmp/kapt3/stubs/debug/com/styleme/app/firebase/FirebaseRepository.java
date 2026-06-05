package com.styleme.app.firebase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\'\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00100\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u001d\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00100\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u001f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\b2\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001aR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001b"}, d2 = {"Lcom/styleme/app/firebase/FirebaseRepository;", "", "()V", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "storage", "Lcom/google/firebase/storage/FirebaseStorage;", "changeHairColour", "Lcom/styleme/app/utils/Resource;", "", "pictureId", "", "colour", "Lcom/styleme/app/models/HairColour;", "(ILcom/styleme/app/models/HairColour;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHairColours", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHairStyles", "Lcom/styleme/app/models/HairStyle;", "getLatestHistory", "Lcom/styleme/app/models/LatestHistoryResponse;", "uploadPicture", "Lcom/styleme/app/models/UploadPictureResponse;", "uri", "Landroid/net/Uri;", "(Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class FirebaseRepository {
    @org.jetbrains.annotations.NotNull
    private static final com.google.firebase.firestore.FirebaseFirestore db = null;
    @org.jetbrains.annotations.NotNull
    private static final com.google.firebase.storage.FirebaseStorage storage = null;
    @org.jetbrains.annotations.NotNull
    public static final com.styleme.app.firebase.FirebaseRepository INSTANCE = null;
    
    private FirebaseRepository() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getHairColours(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.styleme.app.utils.Resource<? extends java.util.List<com.styleme.app.models.HairColour>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getHairStyles(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.styleme.app.utils.Resource<? extends java.util.List<com.styleme.app.models.HairStyle>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object uploadPicture(@org.jetbrains.annotations.NotNull
    android.net.Uri uri, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.styleme.app.utils.Resource<com.styleme.app.models.UploadPictureResponse>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object changeHairColour(int pictureId, @org.jetbrains.annotations.NotNull
    com.styleme.app.models.HairColour colour, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.styleme.app.utils.Resource<java.lang.String>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getLatestHistory(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.styleme.app.utils.Resource<com.styleme.app.models.LatestHistoryResponse>> $completion) {
        return null;
    }
}