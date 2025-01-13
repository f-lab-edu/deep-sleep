package com.flab.deepsleep.ui.photo

import com.flab.deepsleep.data.entity.photos.SinglePhoto

interface onItemClick {
    fun onButtonClick(singlePhoto: SinglePhoto, position: Int)
    fun onPhotoClick(singlePhoto: SinglePhoto, position: Int)
}