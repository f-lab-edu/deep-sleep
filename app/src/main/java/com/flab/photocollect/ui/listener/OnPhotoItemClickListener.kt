package com.flab.photocollect.ui.listener

import com.flab.photocollect.data.entity.room.UiItem

fun interface OnPhotoItemClickListener {
    fun onPhotoItemClick(uiItem: UiItem)
}