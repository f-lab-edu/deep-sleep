package com.flab.deepsleep.data.entity

data class ResponseBody(
    val protocol: String,
    val code: Int,
    val message: String,
    val url: String
)
