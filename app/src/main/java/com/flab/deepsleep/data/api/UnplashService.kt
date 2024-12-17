package com.flab.deepsleep.data.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface UnplashService {

    @GET("/photos/random")
    suspend fun getRandomPhotos(
        @Query("client_id") clientId: String,
        @Query("count") count: Int = 1
    ): retrofit2.Response<ResponseBody>
}