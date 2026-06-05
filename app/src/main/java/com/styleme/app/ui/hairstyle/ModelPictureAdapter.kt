package com.styleme.app.ui.hairstyle

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.styleme.app.databinding.ItemModelPictureBinding
import com.styleme.app.models.ModelPicture

class ModelPictureAdapter(
    private val onImageNeeded: (Int, (Bitmap?) -> Unit) -> Unit,
    private val onModelClick: (ModelPicture) -> Unit
) : ListAdapter<ModelPicture, ModelPictureAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemModelPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ModelPicture) {
            val stylePart = model.hairStyleLabel ?: model.hairStyleName?.replaceFirstChar { it.uppercase() }
            val facePart  = model.faceShapeLabel ?: model.faceShapeName?.replaceFirstChar { it.uppercase() }
            binding.tvModelName.text = when {
                stylePart != null && facePart != null -> "$stylePart – $facePart"
                stylePart != null                    -> stylePart
                else -> model.fileName.substringBeforeLast('.').replace('_', ' ')
            }

            binding.ivModelPicture.setImageResource(android.R.drawable.ic_menu_gallery)

            onImageNeeded(model.id) { bitmap ->
                if (bitmap != null) {
                    binding.ivModelPicture.setImageBitmap(bitmap)
                }
            }

            binding.root.setOnClickListener { onModelClick(model) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemModelPictureBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<ModelPicture>() {
        override fun areItemsTheSame(a: ModelPicture, b: ModelPicture) = a.id == b.id
        override fun areContentsTheSame(a: ModelPicture, b: ModelPicture) = a == b
    }
}
