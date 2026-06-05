package com.styleme.app.viewmodels;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u000bJ\u0006\u0010\u001e\u001a\u00020\u001bR\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\f\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR#\u0010\u0010\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/styleme/app/viewmodels/HairColourViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_changeResult", "Landroidx/lifecycle/MutableLiveData;", "Lcom/styleme/app/utils/Resource;", "Landroid/graphics/Bitmap;", "_colours", "", "Lcom/styleme/app/models/HairColour;", "changeResult", "Landroidx/lifecycle/LiveData;", "getChangeResult", "()Landroidx/lifecycle/LiveData;", "colours", "getColours", "picturesApi", "Lcom/styleme/app/api/PicturesApiService;", "updatedPictureId", "", "getUpdatedPictureId", "()Landroidx/lifecycle/MutableLiveData;", "usersApi", "Lcom/styleme/app/api/UsersApiService;", "changeHairColour", "", "pictureId", "colour", "loadColours", "app_debug"})
public final class HairColourViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.styleme.app.api.UsersApiService usersApi = null;
    @org.jetbrains.annotations.NotNull
    private final com.styleme.app.api.PicturesApiService picturesApi = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.styleme.app.utils.Resource<java.util.List<com.styleme.app.models.HairColour>>> _colours = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<com.styleme.app.utils.Resource<java.util.List<com.styleme.app.models.HairColour>>> colours = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.styleme.app.utils.Resource<android.graphics.Bitmap>> _changeResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<com.styleme.app.utils.Resource<android.graphics.Bitmap>> changeResult = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Integer> updatedPictureId = null;
    
    public HairColourViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.styleme.app.utils.Resource<java.util.List<com.styleme.app.models.HairColour>>> getColours() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.styleme.app.utils.Resource<android.graphics.Bitmap>> getChangeResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Integer> getUpdatedPictureId() {
        return null;
    }
    
    public final void loadColours() {
    }
    
    public final void changeHairColour(int pictureId, @org.jetbrains.annotations.NotNull
    com.styleme.app.models.HairColour colour) {
    }
}