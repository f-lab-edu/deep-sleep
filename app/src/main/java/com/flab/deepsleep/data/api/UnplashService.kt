package com.flab.deepsleep.data.api

import com.flab.deepsleep.data.entity.photo.RandomPhoto
import retrofit2.http.GET
import retrofit2.http.Query

interface UnplashService {

    @GET("/photos/random")
    suspend fun getRandomPhotos(
        @Query("client_id") clientId: String,
        @Query("count") count: Int = 1
    ): List<RandomPhoto>
}