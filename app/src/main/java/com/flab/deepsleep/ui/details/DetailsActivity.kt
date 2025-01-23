package com.flab.deepsleep.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.flab.deepsleep.R
import com.flab.deepsleep.databinding.ActivityDetailsBinding
import com.flab.deepsleep.ui.photo.UiItem
import dagger.hilt.android.AndroidEntryPoint
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
            /* 좋아요 표시 */
            uiItem?.id?.let { detailsViewModel.loadPhotoLikeStatus(it) }
        } ?: run {
            loadImage(null)
            Timber.d("singlePhoto is null")
        }

        detailsViewModel.isLiked.observe(this) { isLiked ->
            detailsBinding.detailBtHeart.isSelected = isLiked
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

        detailsBinding.detailBtHeart.setOnClickListener {
            detailsViewModel.insertPhoto(uiItem)
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


