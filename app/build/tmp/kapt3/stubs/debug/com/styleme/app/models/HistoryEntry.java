package com.styleme.app.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u001e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001Bg\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\b\u0010\r\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\u0010\u0010\u001f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013J\u0010\u0010 \u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013J\u0010\u0010!\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013J\u0010\u0010\"\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013J\u0010\u0010#\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013J\u0010\u0010$\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013J\u0010\u0010%\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013J\u000b\u0010&\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\u0084\u0001\u0010\'\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\fH\u00c6\u0001\u00a2\u0006\u0002\u0010(J\t\u0010)\u001a\u00020\u0003H\u00d6\u0001J\u0013\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010-H\u00d6\u0003J\t\u0010.\u001a\u00020\u0003H\u00d6\u0001J\t\u0010/\u001a\u00020\fH\u00d6\u0001J\u0019\u00100\u001a\u0002012\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u0003H\u00d6\u0001R\u0018\u0010\u000b\u001a\u0004\u0018\u00010\f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0018\u0010\r\u001a\u0004\u0018\u00010\f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u001a\u0010\t\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0007\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u0015\u0010\u0013R\u001a\u0010\b\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u0016\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u0019\u0010\u0013R\u001a\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u001a\u0010\u0013R\u001a\u0010\u0006\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u001b\u0010\u0013R\u001a\u0010\n\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u001c\u0010\u0013\u00a8\u00065"}, d2 = {"Lcom/styleme/app/models/HistoryEntry;", "Landroid/os/Parcelable;", "id", "", "pictureId", "originalPictureId", "previousPictureId", "hairColourId", "hairStyleId", "faceShapeId", "userId", "dateCreated", "", "dateUpdated", "(ILjava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;)V", "getDateCreated", "()Ljava/lang/String;", "getDateUpdated", "getFaceShapeId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getHairColourId", "getHairStyleId", "getId", "()I", "getOriginalPictureId", "getPictureId", "getPreviousPictureId", "getUserId", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ILjava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;)Lcom/styleme/app/models/HistoryEntry;", "describeContents", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "app_debug"})
@kotlinx.parcelize.Parcelize
public final class HistoryEntry implements android.os.Parcelable {
    private final int id = 0;
    @com.google.gson.annotations.SerializedName(value = "picture_id")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer pictureId = null;
    @com.google.gson.annotations.SerializedName(value = "original_picture_id")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer originalPictureId = null;
    @com.google.gson.annotations.SerializedName(value = "previous_picture_id")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer previousPictureId = null;
    @com.google.gson.annotations.SerializedName(value = "hair_colour_id")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer hairColourId = null;
    @com.google.gson.annotations.SerializedName(value = "hair_style_id")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer hairStyleId = null;
    @com.google.gson.annotations.SerializedName(value = "face_shape_id")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer faceShapeId = null;
    @com.google.gson.annotations.SerializedName(value = "user_id")
    @org.jetbrains.annotations.Nullable
    private final java.lang.Integer userId = null;
    @com.google.gson.annotations.SerializedName(value = "date_created")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String dateCreated = null;
    @com.google.gson.annotations.SerializedName(value = "date_updated")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String dateUpdated = null;
    
    public HistoryEntry(int id, @org.jetbrains.annotations.Nullable
    java.lang.Integer pictureId, @org.jetbrains.annotations.Nullable
    java.lang.Integer originalPictureId, @org.jetbrains.annotations.Nullable
    java.lang.Integer previousPictureId, @org.jetbrains.annotations.Nullable
    java.lang.Integer hairColourId, @org.jetbrains.annotations.Nullable
    java.lang.Integer hairStyleId, @org.jetbrains.annotations.Nullable
    java.lang.Integer faceShapeId, @org.jetbrains.annotations.Nullable
    java.lang.Integer userId, @org.jetbrains.annotations.Nullable
    java.lang.String dateCreated, @org.jetbrains.annotations.Nullable
    java.lang.String dateUpdated) {
        super();
    }
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getPictureId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getOriginalPictureId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getPreviousPictureId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getHairColourId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getHairStyleId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getFaceShapeId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getUserId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getDateCreated() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getDateUpdated() {
        return null;
    }
    
    public final int component1() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.models.HistoryEntry copy(int id, @org.jetbrains.annotations.Nullable
    java.lang.Integer pictureId, @org.jetbrains.annotations.Nullable
    java.lang.Integer originalPictureId, @org.jetbrains.annotations.Nullable
    java.lang.Integer previousPictureId, @org.jetbrains.annotations.Nullable
    java.lang.Integer hairColourId, @org.jetbrains.annotations.Nullable
    java.lang.Integer hairStyleId, @org.jetbrains.annotations.Nullable
    java.lang.Integer faceShapeId, @org.jetbrains.annotations.Nullable
    java.lang.Integer userId, @org.jetbrains.annotations.Nullable
    java.lang.String dateCreated, @org.jetbrains.annotations.Nullable
    java.lang.String dateUpdated) {
        return null;
    }
    
    @java.lang.Override
    public int describeContents() {
        return 0;
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
    
    @java.lang.Override
    public void writeToParcel(@org.jetbrains.annotations.NotNull
    android.os.Parcel parcel, int flags) {
    }
}