package com.styleme.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u001b\u0010!\u001a\u00020\"2\b\b\u0002\u0010#\u001a\u00020$H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%J\u001b\u0010&\u001a\u00020\"2\b\b\u0002\u0010#\u001a\u00020$H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%J\u0010\u0010\'\u001a\u00020(2\b\b\u0002\u0010)\u001a\u00020*J\u0016\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u001c2\u0006\u0010.\u001a\u00020*J\u0015\u0010/\u001a\u00020,2\b\u00100\u001a\u0004\u0018\u00010\u001c\u00a2\u0006\u0002\u00101R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\rR\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\rR\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00062"}, d2 = {"Lcom/styleme/app/utils/MockRepository;", "", "()V", "enabled", "", "getEnabled", "()Z", "setEnabled", "(Z)V", "fakeFaceShapes", "", "Lcom/styleme/app/models/FaceShape;", "getFakeFaceShapes", "()Ljava/util/List;", "fakeHairColours", "Lcom/styleme/app/models/HairColour;", "getFakeHairColours", "fakeHairStyles", "Lcom/styleme/app/models/HairStyle;", "getFakeHairStyles", "fakeModelPictures", "Lcom/styleme/app/models/ModelPicture;", "getFakeModelPictures", "fakeUser", "Lcom/styleme/app/models/User;", "getFakeUser", "()Lcom/styleme/app/models/User;", "pictureCounter", "", "darken", "color", "factor", "", "fakeDelay", "", "ms", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fakeLongDelay", "makeFakePicture", "Lcom/styleme/app/models/Picture;", "name", "", "makePlaceholderBitmap", "Landroid/graphics/Bitmap;", "colour", "label", "makeStyleBitmap", "styleId", "(Ljava/lang/Integer;)Landroid/graphics/Bitmap;", "app_debug"})
public final class MockRepository {
    private static boolean enabled = false;
    @org.jetbrains.annotations.NotNull
    private static final com.styleme.app.models.User fakeUser = null;
    @org.jetbrains.annotations.NotNull
    private static final java.util.List<com.styleme.app.models.HairColour> fakeHairColours = null;
    @org.jetbrains.annotations.NotNull
    private static final java.util.List<com.styleme.app.models.HairStyle> fakeHairStyles = null;
    @org.jetbrains.annotations.NotNull
    private static final java.util.List<com.styleme.app.models.FaceShape> fakeFaceShapes = null;
    @org.jetbrains.annotations.NotNull
    private static final java.util.List<com.styleme.app.models.ModelPicture> fakeModelPictures = null;
    private static int pictureCounter = 100;
    @org.jetbrains.annotations.NotNull
    public static final com.styleme.app.utils.MockRepository INSTANCE = null;
    
    private MockRepository() {
        super();
    }
    
    public final boolean getEnabled() {
        return false;
    }
    
    public final void setEnabled(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.models.User getFakeUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.styleme.app.models.HairColour> getFakeHairColours() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.styleme.app.models.HairStyle> getFakeHairStyles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.styleme.app.models.FaceShape> getFakeFaceShapes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.styleme.app.models.ModelPicture> getFakeModelPictures() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.models.Picture makeFakePicture(@org.jetbrains.annotations.NotNull
    java.lang.String name) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final android.graphics.Bitmap makePlaceholderBitmap(int colour, @org.jetbrains.annotations.NotNull
    java.lang.String label) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final android.graphics.Bitmap makeStyleBitmap(@org.jetbrains.annotations.Nullable
    java.lang.Integer styleId) {
        return null;
    }
    
    private final int darken(int color, float factor) {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object fakeDelay(long ms, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object fakeLongDelay(long ms, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}