package com.example.project9

import androidx.recyclerview.widget.DiffUtil
/**
 * DiffUtil.ItemCallback implementation for the Image data class.
 * This class is used by the RecyclerView adapter to efficiently update the list of images.
 * It determines whether items are the same and whether their contents are the same.
 */
class ImageDiffItemCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image)
            = (oldItem.imageUrl == newItem.imageUrl)
    override fun areContentsTheSame(oldItem: Image, newItem: Image) = (oldItem == newItem)
}