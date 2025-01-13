package com.flab.deepsleep.ui.photo

import com.flab.deepsleep.data.entity.photos.SinglePhoto

interface onItemClick {
    fun onClick(singlePhoto: SinglePhoto, position: Int)
}