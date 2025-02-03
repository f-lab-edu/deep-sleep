package com.flab.deepsleep.ui.listener

import com.flab.deepsleep.data.entity.room.UiItem

fun interface OnHeartButtonClick {
    fun onHeartButtonClick(uiItem: UiItem)
}