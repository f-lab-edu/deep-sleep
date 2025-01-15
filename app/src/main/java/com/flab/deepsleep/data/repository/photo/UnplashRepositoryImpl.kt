package com.flab.deepsleep.data.repository.photo

import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.api.UnplashService
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.entity.photos.SearchPhotos
import javax.inject.Inject

class UnplashRepositoryImpl @Inject constructor(private val unplashService: UnplashService) :
    UnplashRepository {

    override suspend fun getRandomPhotos(count: Int): List<SinglePhoto> {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unplashService.getRandomPhotos(clientId, count)
        return response
    }

    override suspend fun getSearchPhotos(query: String): SearchPhotos {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unplashService.getSearchPhotos(clientId, query)
        return response
    }

    override suspend fun getSinglePhotoById(photoId: String): SinglePhoto {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unplashService.getSinglePhotoById(photoId, clientId)
        return response
    }

}