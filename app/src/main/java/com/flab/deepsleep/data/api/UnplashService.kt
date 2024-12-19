package com.flab.deepsleep.data.api

import com.flab.deepsleep.data.entity.photos.RandomPhoto
import com.flab.deepsleep.data.entity.search.SearchPhotos
import retrofit2.http.GET
import retrofit2.http.Query

interface UnplashService {

    @GET("/photos/random")
    suspend fun getRandomPhotos(
        @Query("client_id") clientId: String,
        @Query("count") count: Int = 1
    ): List<RandomPhoto>

    @GET("/search/photos")
    suspend fun getSearchPhotos(
        @Query("client_id") clientId: String,
        @Query("query") query: String
    ): SearchPhotos

}