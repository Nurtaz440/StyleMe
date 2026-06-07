package com.styleme.app.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\b\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0010"}, d2 = {"Lcom/styleme/app/api/ApiClient;", "", "()V", "picturesApi", "Lcom/styleme/app/api/PicturesApiService;", "getPicturesApi", "()Lcom/styleme/app/api/PicturesApiService;", "picturesApi$delegate", "Lkotlin/Lazy;", "usersApi", "Lcom/styleme/app/api/UsersApiService;", "getUsersApi", "()Lcom/styleme/app/api/UsersApiService;", "usersApi$delegate", "buildClient", "Lokhttp3/OkHttpClient;", "app_debug"})
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
    public final com.styleme.app.api.PicturesApiService getPicturesApi() {
        return null;
    }
}