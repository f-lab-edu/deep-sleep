package com.flab.photocollect.ui.listener

import com.flab.photocollect.data.entity.room.UiItem

fun interface OnHeartButtonClick {
    fun onHeartButtonClick(uiItem: UiItem)
}