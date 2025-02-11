package com.flab.photocollect.data.api

import com.flab.photocollect.data.entity.unplash.SinglePhoto
import com.flab.photocollect.data.entity.unplash.SearchPhotos
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashService {
    @GET("/photos")
    suspend fun getListPhotos(
        @Query("client_id") clientId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<SinglePhoto>

    @GET("/photos/random")
    suspend fun getRandomPhotos(
        @Query("client_id") clientId: String,
        @Query("count") count: Int = 1
    ): List<SinglePhoto>

    @GET("/search/photos")
    suspend fun getSearchPhotos(
        @Query("client_id") clientId: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): SearchPhotos

    @GET("/photos/{id}")
    suspend fun getSinglePhotoById(
        @Path("id") photoId: String,
        @Query("client_id") clientId: String
    ): SinglePhoto
}