package com.styleme.app.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 $2\u00020\u0001:\u0001$B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\nJ\u0006\u0010\f\u001a\u00020\rJ\r\u0010\u000e\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000fJ\u0006\u0010\u0010\u001a\u00020\rJ\b\u0010\u0011\u001a\u0004\u0018\u00010\rJ\r\u0010\u0012\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000fJ\u0006\u0010\u0013\u001a\u00020\rJ\b\u0010\u0014\u001a\u0004\u0018\u00010\rJ\u0006\u0010\u0015\u001a\u00020\nJ\u0006\u0010\u0016\u001a\u00020\rJ\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\nJ\u000e\u0010\u001b\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\nJ\u0016\u0010\u001c\u001a\u00020\b2\u0006\u0010\u001d\u001a\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fJ\u000e\u0010 \u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\nJ\u0016\u0010!\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\rJ\u000e\u0010#\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/styleme/app/utils/SessionManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "prefs", "Landroid/content/SharedPreferences;", "clearSession", "", "getCurrentModelPictureId", "", "getCurrentPictureId", "getEmail", "", "getFaceShapeId", "()Ljava/lang/Integer;", "getFirstName", "getHairColourHash", "getHairStyleId", "getLastName", "getToken", "getUserId", "getUsername", "isLoggedIn", "", "saveCurrentModelPictureId", "id", "saveCurrentPictureId", "saveSession", "token", "user", "Lcom/styleme/app/models/User;", "updateFaceShape", "updateHairColour", "hash", "updateHairStyle", "Companion", "app_debug"})
public final class SessionManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_TOKEN = "auth_token";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_USER_ID = "user_id";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_USERNAME = "username";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_FIRST_NAME = "first_name";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_LAST_NAME = "last_name";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_EMAIL = "email";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_FACE_SHAPE_ID = "face_shape_id";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_HAIR_COLOUR_ID = "hair_colour_id";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_HAIR_COLOUR_HASH = "hair_colour_hash";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_HAIR_STYLE_ID = "hair_style_id";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_CURRENT_PICTURE_ID = "current_picture_id";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String KEY_CURRENT_MODEL_PICTURE_ID = "current_model_picture_id";
    @org.jetbrains.annotations.NotNull
    public static final com.styleme.app.utils.SessionManager.Companion Companion = null;
    
    public SessionManager(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    public final void saveSession(@org.jetbrains.annotations.NotNull
    java.lang.String token, @org.jetbrains.annotations.NotNull
    com.styleme.app.models.User user) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getToken() {
        return null;
    }
    
    public final int getUserId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUsername() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getFirstName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLastName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getEmail() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getFaceShapeId() {
        return null;
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getHairColourHash() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getHairStyleId() {
        return null;
    }
    
    public final void updateFaceShape(int id) {
    }
    
    public final void updateHairColour(int id, @org.jetbrains.annotations.NotNull
    java.lang.String hash) {
    }
    
    public final void updateHairStyle(int id) {
    }
    
    public final void saveCurrentPictureId(int id) {
    }
    
    public final int getCurrentPictureId() {
        return 0;
    }
    
    public final void saveCurrentModelPictureId(int id) {
    }
    
    public final int getCurrentModelPictureId() {
        return 0;
    }
    
    public final void clearSession() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/styleme/app/utils/SessionManager$Companion;", "", "()V", "KEY_CURRENT_MODEL_PICTURE_ID", "", "KEY_CURRENT_PICTURE_ID", "KEY_EMAIL", "KEY_FACE_SHAPE_ID", "KEY_FIRST_NAME", "KEY_HAIR_COLOUR_HASH", "KEY_HAIR_COLOUR_ID", "KEY_HAIR_STYLE_ID", "KEY_LAST_NAME", "KEY_TOKEN", "KEY_USERNAME", "KEY_USER_ID", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}