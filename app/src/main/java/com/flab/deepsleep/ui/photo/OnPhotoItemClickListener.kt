package com.flab.deepsleep.ui.photo

import com.flab.deepsleep.data.entity.photos.SinglePhoto

fun interface OnPhotoItemClickListener {
    fun onPhotoItemClick(singlePhoto : SinglePhoto)
}