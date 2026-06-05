package com.styleme.app.models;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\u000bJ\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\tH\u00c6\u0003JE\u0010\u001a\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\tH\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001J\t\u0010 \u001a\u00020\tH\u00d6\u0001R\u0018\u0010\n\u001a\u0004\u0018\u00010\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0018\u0010\u0006\u001a\u0004\u0018\u00010\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006!"}, d2 = {"Lcom/styleme/app/models/ChangeHairColourResponse;", "", "historyEntry", "Lcom/styleme/app/models/HistoryEntry;", "picture", "Lcom/styleme/app/models/Picture;", "hairColour", "Lcom/styleme/app/models/HairColour;", "message", "", "fileName", "(Lcom/styleme/app/models/HistoryEntry;Lcom/styleme/app/models/Picture;Lcom/styleme/app/models/HairColour;Ljava/lang/String;Ljava/lang/String;)V", "getFileName", "()Ljava/lang/String;", "getHairColour", "()Lcom/styleme/app/models/HairColour;", "getHistoryEntry", "()Lcom/styleme/app/models/HistoryEntry;", "getMessage", "getPicture", "()Lcom/styleme/app/models/Picture;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class ChangeHairColourResponse {
    @com.google.gson.annotations.SerializedName(value = "history_entry")
    @org.jetbrains.annotations.Nullable
    private final com.styleme.app.models.HistoryEntry historyEntry = null;
    @org.jetbrains.annotations.Nullable
    private final com.styleme.app.models.Picture picture = null;
    @com.google.gson.annotations.SerializedName(value = "hair_colour")
    @org.jetbrains.annotations.Nullable
    private final com.styleme.app.models.HairColour hairColour = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String message = null;
    @com.google.gson.annotations.SerializedName(value = "file_name")
    @org.jetbrains.annotations.Nullable
    private final java.lang.String fileName = null;
    
    public ChangeHairColourResponse(@org.jetbrains.annotations.Nullable
    com.styleme.app.models.HistoryEntry historyEntry, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.Picture picture, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.HairColour hairColour, @org.jetbrains.annotations.Nullable
    java.lang.String message, @org.jetbrains.annotations.Nullable
    java.lang.String fileName) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.HistoryEntry getHistoryEntry() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.Picture getPicture() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.HairColour getHairColour() {
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
    public final com.styleme.app.models.Picture component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.styleme.app.models.HairColour component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.styleme.app.models.ChangeHairColourResponse copy(@org.jetbrains.annotations.Nullable
    com.styleme.app.models.HistoryEntry historyEntry, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.Picture picture, @org.jetbrains.annotations.Nullable
    com.styleme.app.models.HairColour hairColour, @org.jetbrains.annotations.Nullable
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