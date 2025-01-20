package com.flab.deepsleep.data.entity.photos

import android.os.Parcelable
import com.flab.deepsleep.data.entity.room.Photo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SinglePhoto(
    @SerializedName("blur_hash")
    val blurHash: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("downloads")
    val downloads: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean?,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("public_domain")
    val publicDomain: Boolean?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("width")
    val width: Int,
    @SerializedName("exif")
    val exif: Exif?,
    @SerializedName("urls")
    val urls: Urls?,
    @SerializedName("user")
    val user: User?,
    var isLike: Boolean = false
) : Parcelable

fun SinglePhoto.toPhoto(): Photo {
    return Photo(
        id = this.id,
        likes = this.likes,
        urls = this.urls?.raw,
        createdAt = this.createdAt,
        username = this.user?.username,
        isLike = this.isLike
    )
}
