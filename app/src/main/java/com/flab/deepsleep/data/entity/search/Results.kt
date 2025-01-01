package com.flab.deepsleep.data.entity.search
import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("blur_hash")
    val blurHash: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String?,
    @SerializedName("liked_by_user")
    val likedByUser: Boolean,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("width")
    val width: Int,
    @SerializedName("user")
    val user: SearchUser?
)
