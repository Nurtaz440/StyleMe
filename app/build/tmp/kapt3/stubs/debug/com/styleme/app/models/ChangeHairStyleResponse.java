package com.styleme.app.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\fJ\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\nH\u00c6\u0003JQ\u0010\u001d\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\nH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\"H\u00d6\u0001J\t\u0010#\u001a\u00020\nH\u00d6\u0001R\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0018\u0010\u000b\u001a\u0004\u0018\u00010\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u0018\u0010\b\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000e\u00a8\u0006$"}, d2 = {"Lcom/styleme/app/models/ChangeHairStyleResponse;", "", "historyEntry", "Lcom/styleme/app/models/HistoryEntry;", "hairStyle", "Lcom/styleme/app/models/HairStyle;", "currentPicture", "Lcom/styleme/app/models/Picture;", "originalPicture", "message", "", "fileName", "(Lcom/styleme/app/models/HistoryEntry;Lcom/styleme/app/models/HairStyle;Lcom/styleme/app/models/Picture;Lcom/styleme/app/models/Picture;Ljava/lang/String;Ljava/lang/String;)V", "getCurrentPicture", "()Lcom/styleme/app/models/Picture;", "getFileName", "()Ljava/lang/String;", "getHairStyle", "()Lcom/styleme/app/models/HairStyle;", "getHistoryEntry", "()Lcom/styleme/app/models/HistoryEntry;", "getMessage", "getOriginalPicture", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class ChangeHairStyleResponse {
    @com.google.gson.annotations.SerializedName(value = "history_entry")
    @org.jetbrains.annotations.Nullable
    private final com.styleme.app.models.HistoryEntry historyEntry = null;
    @com.google.gson.annotations.SerializedName(value = "hair_style")
    @org.jetbrains.annotations.Nullable
    private final com.styleme.app.models.HairStyle hairStyle = null;
    @com.google.gson.annotations.SerializedName(value = "current_picture")
    @org.jetbrains.annotations.Nullable
    private final com.styleme.app.models.Picture currentPicture = null;
    @com.google.gson.annotations.SerializedName(value = "original_picture")
    @org.jetbrains.annotations.Nullable
    private final com.styleme.app.models.Picture originalPicture = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String message = null;
    @com.google.gson.annotations.SerializedName(value = "file_name")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String fileName = null;
    
    public ChangeHairStyleResponse(@org.jetbrains.annotations.Nullable
    com.styleme.app.models.HistoryEntry historyEntry, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.HairStyle hairStyle, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.Picture currentPicture, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.Picture originalPicture, @org.jetbrains.annotations.Nullable
    java.lang.String message, @org.jetbrains.annotations.Nullable
    java.lang.String fileName) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.HistoryEntry getHistoryEntry() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.HairStyle getHairStyle() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.Picture getCurrentPicture() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.Picture getOriginalPicture() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getFileName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.HistoryEntry component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.HairStyle component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.Picture component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.Picture component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.models.ChangeHairStyleResponse copy(@org.jetbrains.annotations.Nullable
    com.styleme.app.models.HistoryEntry historyEntry, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.HairStyle hairStyle, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.Picture currentPicture, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.Picture originalPicture, @org.jetbrains.annotations.Nullable
    java.lang.String message, @org.jetbrains.annotations.Nullable
    java.lang.String fileName) {
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