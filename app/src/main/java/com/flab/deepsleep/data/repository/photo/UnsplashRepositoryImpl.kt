package com.flab.deepsleep.data.repository.photo

import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.api.UnsplashService
import com.flab.deepsleep.data.entity.unplash.SinglePhoto
import com.flab.deepsleep.data.entity.unplash.SearchPhotos
import javax.inject.Inject

class UnsplashRepositoryImpl @Inject constructor(private val unsplashService: UnsplashService) :
    UnsplashRepository {

    override suspend fun getRandomPhotos(count: Int): List<SinglePhoto> {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unsplashService.getRandomPhotos(clientId, count)
        return response
    }

    override suspend fun getSearchPhotos(query: String, page: Int, perPage: Int): SearchPhotos {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unsplashService.getSearchPhotos(clientId, query, page, perPage)
        return response
    }

    override suspend fun getSinglePhotoById(photoId: String): SinglePhoto {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unsplashService.getSinglePhotoById(photoId, clientId)
        return response
    }

}