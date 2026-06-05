package com.styleme.app.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u000bH\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003JK\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u000bH\u00c6\u0001J\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\tH\u00d6\u0001J\t\u0010$\u001a\u00020\u000bH\u00d6\u0001R\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\n\u001a\u00020\u000b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0013\u0010\f\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006%"}, d2 = {"Lcom/styleme/app/models/UploadPictureResponse;", "", "picture", "Lcom/styleme/app/models/Picture;", "faceShape", "Lcom/styleme/app/models/FaceShape;", "historyEntry", "Lcom/styleme/app/models/HistoryEntry;", "id", "", "fileName", "", "message", "(Lcom/styleme/app/models/Picture;Lcom/styleme/app/models/FaceShape;Lcom/styleme/app/models/HistoryEntry;ILjava/lang/String;Ljava/lang/String;)V", "getFaceShape", "()Lcom/styleme/app/models/FaceShape;", "getFileName", "()Ljava/lang/String;", "getHistoryEntry", "()Lcom/styleme/app/models/HistoryEntry;", "getId", "()I", "getMessage", "getPicture", "()Lcom/styleme/app/models/Picture;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class UploadPictureResponse {
    @org.jetbrains.annotations.NotNull
    private final com.styleme.app.models.Picture picture = null;
    @com.google.gson.annotations.SerializedName(value = "face_shape")
    @org.jetbrains.annotations.Nullable
    private final com.styleme.app.models.FaceShape faceShape = null;
    @com.google.gson.annotations.SerializedName(value = "history_entry")
    @org.jetbrains.annotations.Nullable
    private final com.styleme.app.models.HistoryEntry historyEntry = null;
    private final int id = 0;
    @com.google.gson.annotations.SerializedName(value = "file_name")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String fileName = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String message = null;
    
    public UploadPictureResponse(@org.jetbrains.annotations.NotNull
    com.styleme.app.models.Picture picture, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.FaceShape faceShape, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.HistoryEntry historyEntry, int id, @org.jetbrains.annotations.NotNull
    java.lang.String fileName, @org.jetbrains.annotations.Nullable
    java.lang.String message) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.models.Picture getPicture() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.FaceShape getFaceShape() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.HistoryEntry getHistoryEntry() {
        return null;
    }
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getFileName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.models.Picture component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.FaceShape component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.HistoryEntry component3() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.models.UploadPictureResponse copy(@org.jetbrains.annotations.NotNull
    com.styleme.app.models.Picture picture, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.FaceShape faceShape, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.HistoryEntry historyEntry, int id, @org.jetbrains.annotations.NotNull
    java.lang.String fileName, @org.jetbrains.annotations.Nullable
    java.lang.String message) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}