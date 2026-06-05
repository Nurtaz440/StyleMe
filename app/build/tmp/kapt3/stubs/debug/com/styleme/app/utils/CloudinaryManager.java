package com.styleme.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0004J0\u0010\f\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\b\b\u0002\u0010\u0011\u001a\u00020\u000eJ\u000e\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0004J\"\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\u0015\u001a\u00020\u000e2\b\b\u0002\u0010\u0016\u001a\u00020\u000eJ\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ#\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006 "}, d2 = {"Lcom/styleme/app/utils/CloudinaryManager;", "", "()V", "API_KEY", "", "API_SECRET", "CLOUD_NAME", "UPLOAD_PRESET", "initialized", "", "applyAutoEnhancement", "publicId", "applyHairColourTransformation", "r", "", "g", "b", "strength", "extractPublicId", "cloudinaryUrl", "getThumbnailUrl", "width", "height", "init", "", "context", "Landroid/content/Context;", "uploadImage", "uri", "Landroid/net/Uri;", "folder", "(Landroid/net/Uri;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class CloudinaryManager {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String CLOUD_NAME = "dkfgv62gg";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String API_KEY = "298492148631448";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String API_SECRET = "lSwL_BTV_KinAXsTGFTXnZgAGvY";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String UPLOAD_PRESET = "styleme_upload";
    private static boolean initialized = false;
    @org.jetbrains.annotations.NotNull
    public static final com.styleme.app.utils.CloudinaryManager INSTANCE = null;
    
    private CloudinaryManager() {
        super();
    }
    
    public final void init(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    /**
     * Upload an image URI to Cloudinary.
     * Returns the secure HTTPS URL of the uploaded image.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object uploadImage(@org.jetbrains.annotations.NotNull
    android.net.Uri uri, @org.jetbrains.annotations.NotNull
    java.lang.String folder, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    /**
     * Get public_id from a Cloudinary URL.
     * URL format: https://res.cloudinary.com/cloud/image/upload/v123/folder/filename.jpg
     * Returns: folder/filename  (without extension)
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String extractPublicId(@org.jetbrains.annotations.NotNull
    java.lang.String cloudinaryUrl) {
        return null;
    }
    
    /**
     * Generate a Cloudinary URL that applies a hair colour tint overlay.
     * Uses Cloudinary's e_colorize transformation — no local PC needed!
     *
     * @param publicId  The Cloudinary public_id of the image
     * @param r         Red value (0-255)
     * @param g         Green value (0-255)
     * @param b         Blue value (0-255)
     * @param strength  Colorize strength (0-100), default 60
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String applyHairColourTransformation(@org.jetbrains.annotations.NotNull
    java.lang.String publicId, int r, int g, int b, int strength) {
        return null;
    }
    
    /**
     * Generate a Cloudinary URL with basic image enhancement.
     * Useful for showing a better version of the uploaded photo.
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String applyAutoEnhancement(@org.jetbrains.annotations.NotNull
    java.lang.String publicId) {
        return null;
    }
    
    /**
     * Generate a thumbnail URL from a Cloudinary public_id.
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getThumbnailUrl(@org.jetbrains.annotations.NotNull
    java.lang.String publicId, int width, int height) {
        return null;
    }
}