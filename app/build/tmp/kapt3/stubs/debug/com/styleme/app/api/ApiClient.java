package com.styleme.app.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u00068F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0013"}, d2 = {"Lcom/styleme/app/api/ApiClient;", "", "()V", "_picturesApi", "Lcom/styleme/app/api/PicturesApiService;", "_usersApi", "Lcom/styleme/app/api/UsersApiService;", "picturesApi", "getPicturesApi", "()Lcom/styleme/app/api/PicturesApiService;", "usersApi", "getUsersApi", "()Lcom/styleme/app/api/UsersApiService;", "buildClient", "Lokhttp3/OkHttpClient;", "init", "", "context", "Landroid/content/Context;", "app_debug"})
public final class ApiClient {
    @org.jetbrains.annotations.Nullable
    private static com.styleme.app.api.UsersApiService _usersApi;
    @org.jetbrains.annotations.Nullable
    private static com.styleme.app.api.PicturesApiService _picturesApi;
    @org.jetbrains.annotations.NotNull
    public static final com.styleme.app.api.ApiClient INSTANCE = null;
    
    private ApiClient() {
        super();
    }
    
    private final okhttp3.OkHttpClient buildClient() {
        return null;
    }
    
    public final void init(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
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