package com.flab.deepsleep.data.entity.photos

import com.google.gson.annotations.SerializedName

data class RandomPhoto(
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
    val publicDomain: Boolean,
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
