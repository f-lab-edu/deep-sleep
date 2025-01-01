package com.flab.deepsleep.data.entity.search

import com.flab.deepsleep.data.entity.photos.Urls
import com.google.gson.annotations.SerializedName

data class SearchUser(
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("instagram_username")
    val instagramUsername: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("portfolio_url")
    val portfolioUrl: String?,
    @SerializedName("twitter_username")
    val twitterUsername: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("urls")
    val urls: Urls?
)


