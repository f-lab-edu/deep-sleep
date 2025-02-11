package com.flab.photocollect.ui.listener

import com.flab.photocollect.data.entity.photo.Photo

fun interface OnDeletePhotoClick {
    fun onDeletePhotoClick(photo: Photo)
}