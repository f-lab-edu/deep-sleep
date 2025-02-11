package com.flab.photocollect.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flab.photocollect.R
import com.flab.photocollect.databinding.HolderItemPhotoBinding
import com.flab.photocollect.ui.listener.OnHeartButtonClick
import com.flab.photocollect.ui.listener.OnPhotoItemClickListener
import com.flab.photocollect.data.entity.room.UiItem

/* HomeFragment - Adapter */
class PagingAdapter(
    private val onHeartButtonClick: OnHeartButtonClick,
    private val onPhotoItemClickListener: OnPhotoItemClickListener
) :
    PagingDataAdapter<UiItem, PagingAdapter.ImageViewHolder>(ARTICLE_DIFF_CALLBACK) {

    class ImageViewHolder(
        private val binding: HolderItemPhotoBinding,
        private val onHeartButtonClick: OnHeartButtonClick,
        private val onPhotoItemClickListener: OnPhotoItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.photoImageView
        private val btHeart: ImageView = binding.btHeart

        fun bind(uiItem: UiItem) {
            binding.photoTitle.text = uiItem.description
            btHeart.isSelected = uiItem.isLike
            btHeart.setOnClickListener {
                onHeartButtonClick.onHeartButtonClick(uiItem)
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
        return ImageViewHolder(binding, onHeartButtonClick, onPhotoItemClickListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
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