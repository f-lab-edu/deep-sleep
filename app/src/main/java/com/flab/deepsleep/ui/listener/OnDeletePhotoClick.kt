package com.flab.deepsleep.ui.listener

import com.flab.deepsleep.data.entity.room.Photo

fun interface OnDeletePhotoClick {
    fun onDeletePhotoClick(photo: Photo)
}