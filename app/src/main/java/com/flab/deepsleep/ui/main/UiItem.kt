package com.flab.deepsleep.ui.main

import android.os.Parcelable
import com.flab.deepsleep.data.entity.room.Photo
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiItem(
    val id: String?,
    val createdAt: String?,
    val description: String?,
    val likes: Int,
    val urls: String?,
    val username: String?,
    val isLike: Boolean = false
) : Parcelable

fun UiItem.toPhoto(): Photo {
    return Photo(
        id = this.id,
        likes = this.likes,
        urls = this.urls,
        createdAt = this.createdAt,
        description = this.description,
        username = this.username,
        isLike = true,
    )
}