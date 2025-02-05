package com.flab.deepsleep.data.repository.photo

import com.flab.deepsleep.data.api.UnsplashService
import com.flab.deepsleep.data.entity.unplash.SinglePhoto
import com.flab.deepsleep.data.entity.unplash.SearchPhotos
import javax.inject.Inject

class UnsplashServiceImpl @Inject constructor(private val unsplashService: UnsplashService) :
    UnsplashRepository {
    private val clientId = com.flab.deepsleep.BuildConfig.UNSPLASH_ACCESS_KEY

    override suspend fun getListPhotos(page: Int, perPage: Int): List<SinglePhoto> {
        val response = unsplashService.getListPhotos(clientId, page, perPage)
        return response
    }

    override suspend fun getRandomPhotos(count: Int): List<SinglePhoto> {
        val response = unsplashService.getRandomPhotos(clientId, count)
        return response
    }

    override suspend fun getSearchPhotos(query: String, page: Int, perPage: Int): SearchPhotos {
        val response = unsplashService.getSearchPhotos(clientId, query, page, perPage)
        return response
    }

    override suspend fun getSinglePhotoById(photoId: String): SinglePhoto {
        val response = unsplashService.getSinglePhotoById(photoId, clientId)
        return response
    }

}