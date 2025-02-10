package com.flab.deepsleep.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.data.entity.room.UiItem
import com.flab.deepsleep.databinding.ActivityDetailsBinding
import com.flab.deepsleep.ui.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val detailsBinding: ActivityDetailsBinding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }
    private val detailsViewModel: DetailsViewModel by viewModels()
    private val uiItem: UiItem? by lazy {
        @Suppress("DEPRECATION") intent.getParcelableExtra<UiItem>("uiItem")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailsBinding.root)

        uiItem?.let {
            loadImage(it.urls)
            bindPhotoDetails(it)
            uiItem?.id?.let { detailsViewModel.loadPhotoLikeStatus(it) }
        } ?: run {
            loadImage(null)
            Timber.d("singlePhoto is null")
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.isLiked.collect { isLiked ->
                    detailsBinding.detailBtHeart.isSelected = isLiked
                }
            }
        }

        detailsBinding.detailBtHeart.setOnClickListener {
            val item = uiItem ?: return@setOnClickListener
            if (detailsViewModel.isLiked.value) {
                item.id?.let { detailsViewModel.deletePhoto(it) }
            } else {
                detailsViewModel.insertPhoto(item)
            }
        }
    }

    private fun loadImage(imageUrl: String?) {
        val placeholderImage = R.drawable.ic_launcher_foreground
        Glide.with(detailsBinding.detailsImageView.context)
            .load(imageUrl ?: placeholderImage)
            .placeholder(placeholderImage)
            .into(detailsBinding.detailsImageView)
    }

    private fun bindPhotoDetails(uiItem: UiItem) {
        val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
        val result = numberFormat.format(uiItem.likes)
        detailsBinding.apply {
            detailDescription.text = uiItem.description ?: "No description available"
            detailCreateAt.text = uiItem.createdAt?.take(10) ?: "Unknown date"
            detailLikes.text = result
            detailUsername.text = uiItem.username
        }
    }

    companion object {
        fun startActivity(context: Context, uiItem: UiItem) {
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("uiItem", uiItem)
            }
            context.startActivity(intent)
        }
    }

}