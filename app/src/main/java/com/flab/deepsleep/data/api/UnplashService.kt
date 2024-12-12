package com.flab.deepsleep.data.api

import retrofit2.Response
import retrofit2.http.GET

interface UnplashService {

    @GET("/photos/random")
    suspend fun getARandomPhoto(): Response<String>
}