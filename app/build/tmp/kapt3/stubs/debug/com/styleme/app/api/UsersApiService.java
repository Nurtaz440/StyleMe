package com.styleme.app.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001d\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00040\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00040\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00040\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\r"}, d2 = {"Lcom/styleme/app/api/UsersApiService;", "", "getFaceShapes", "Lretrofit2/Response;", "", "Lcom/styleme/app/models/FaceShape;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHairColours", "Lcom/styleme/app/models/HairColour;", "getHairLengths", "Lcom/styleme/app/models/HairLength;", "getHairStyles", "Lcom/styleme/app/models/HairStyle;", "app_debug"})
public abstract interface UsersApiService {
    
    @retrofit2.http.GET(value = "hair_colours")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getHairColours(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.styleme.app.models.HairColour>>> $completion);
    
    @retrofit2.http.GET(value = "hair_styles")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getHairStyles(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.styleme.app.models.HairStyle>>> $completion);
    
    @retrofit2.http.GET(value = "hair_lengths")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getHairLengths(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.styleme.app.models.HairLength>>> $completion);
    
    @retrofit2.http.GET(value = "face_shapes")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getFaceShapes(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.styleme.app.models.FaceShape>>> $completion);
}