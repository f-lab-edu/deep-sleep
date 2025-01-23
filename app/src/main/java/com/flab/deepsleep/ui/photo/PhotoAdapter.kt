package com.flab.deepsleep.ui.photo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.databinding.HolderItemPhotoBinding

class PhotoAdapter(
    private val buttonClick: OnButtonClickListener,
    private val onPhotoItemClickListener: OnPhotoItemClickListener
) :
    PagingDataAdapter<UiItem, PhotoAdapter.ImageViewHolder>(ARTICLE_DIFF_CALLBACK) {

    class ImageViewHolder(
        private val binding: HolderItemPhotoBinding,
        private val buttonClick: OnButtonClickListener,
        private val onPhotoItemClickListener: OnPhotoItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.photoImageView

        fun bind(uiItem: UiItem) {
            itemView.tag = uiItem
            binding.btHeart.isSelected = uiItem.isLike

            /* Like Button */
            binding.btHeart.setOnClickListener {
                buttonClick.onButtonClick(uiItem)
            }

            imageView.setOnClickListener {
                onPhotoItemClickListener.onPhotoItemClick(uiItem)
            }

            val imageUrl = uiItem.urls
            if (imageUrl != null) {
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            HolderItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding, buttonClick, onPhotoItemClickListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uiItem = getItem(position)
        uiItem?.let {
            holder.bind(uiItem)
        }
    }

    /* Paging */
    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiItem>() {
            override fun areItemsTheSame(oldItem: UiItem, newItem: UiItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UiItem, newItem: UiItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}