package com.flab.deepsleep.data.entity

data class PhotoResponse(
    val id: String,
    val urls: Urls
)
data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)