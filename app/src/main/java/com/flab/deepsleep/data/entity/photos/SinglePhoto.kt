package com.flab.deepsleep.data.entity.photos

import com.google.gson.annotations.SerializedName

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
    val user: User?
)

fun singlePhotoExtention(singlePhoto: SinglePhoto): SinglePhoto{
    return SinglePhoto(
        id = singlePhoto?.id,
        description = singlePhoto?.description,
        color = singlePhoto?.color,
        createdAt = singlePhoto?.createdAt,
        downloads = 0,
        height = 0,
        width = 0,
        blurHash = singlePhoto?.blurHash,
        likedByUser = false,
        likes = 0,
        publicDomain = true,
        updatedAt = singlePhoto?.updatedAt,
        exif = singlePhoto?.exif,
        urls = singlePhoto?.urls,
        user = singlePhoto?.user
    )
}