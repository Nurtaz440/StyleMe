package com.styleme.app.ui.hairstyle;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u0012\u0012\u0004\u0012\u00020\u0002\u0012\b\u0012\u00060\u0003R\u00020\u00000\u0001:\u0002\u0013\u0014BA\u0012&\u0010\u0004\u001a\"\u0012\u0004\u0012\u00020\u0006\u0012\u0012\u0012\u0010\u0012\u0006\u0012\u0004\u0018\u00010\b\u0012\u0004\u0012\u00020\t0\u0007\u0012\u0004\u0012\u00020\t0\u0005\u0012\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\u0007\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\f\u001a\u00020\t2\n\u0010\r\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\u001c\u0010\u000f\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0006H\u0016R.\u0010\u0004\u001a\"\u0012\u0004\u0012\u00020\u0006\u0012\u0012\u0012\u0010\u0012\u0006\u0012\u0004\u0018\u00010\b\u0012\u0004\u0012\u00020\t0\u0007\u0012\u0004\u0012\u00020\t0\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/styleme/app/ui/hairstyle/ModelPictureAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/styleme/app/models/ModelPicture;", "Lcom/styleme/app/ui/hairstyle/ModelPictureAdapter$ViewHolder;", "onImageNeeded", "Lkotlin/Function2;", "", "Lkotlin/Function1;", "Landroid/graphics/Bitmap;", "", "onModelClick", "(Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function1;)V", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "DiffCallback", "ViewHolder", "app_debug"})
public final class ModelPictureAdapter extends androidx.recyclerview.widget.ListAdapter<com.styleme.app.models.ModelPicture, com.styleme.app.ui.hairstyle.ModelPictureAdapter.ViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function2<java.lang.Integer, kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit>, kotlin.Unit> onImageNeeded = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.styleme.app.models.ModelPicture, kotlin.Unit> onModelClick = null;
    
    public ModelPictureAdapter(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super kotlin.jvm.functions.Function1<? super android.graphics.Bitmap, kotlin.Unit>, kotlin.Unit> onImageNeeded, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.styleme.app.models.ModelPicture, kotlin.Unit> onModelClick) {
        super(null);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.styleme.app.ui.hairstyle.ModelPictureAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.styleme.app.ui.hairstyle.ModelPictureAdapter.ViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/styleme/app/ui/hairstyle/ModelPictureAdapter$DiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/styleme/app/models/ModelPicture;", "()V", "areContentsTheSame", "", "a", "b", "areItemsTheSame", "app_debug"})
    public static final class DiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.styleme.app.models.ModelPicture> {
        
        public DiffCallback() {
            super();
        }
        
        @java.lang.Override
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull
        com.styleme.app.models.ModelPicture a, @org.jetbrains.annotations.NotNull
        com.styleme.app.models.ModelPicture b) {
            return false;
        }
        
        @java.lang.Override
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull
        com.styleme.app.models.ModelPicture a, @org.jetbrains.annotations.NotNull
        com.styleme.app.models.ModelPicture b) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/styleme/app/ui/hairstyle/ModelPictureAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/styleme/app/databinding/ItemModelPictureBinding;", "(Lcom/styleme/app/ui/hairstyle/ModelPictureAdapter;Lcom/styleme/app/databinding/ItemModelPictureBinding;)V", "bind", "", "model", "Lcom/styleme/app/models/ModelPicture;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.styleme.app.databinding.ItemModelPictureBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        com.styleme.app.databinding.ItemModelPictureBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.styleme.app.models.ModelPicture model) {
        }
    }
}