package com.flab.deepsleep.data.repo

import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.api.UnplashService
import com.flab.deepsleep.data.entity.photos.RandomPhoto
import com.flab.deepsleep.data.entity.search.SearchPhotos
import javax.inject.Inject

class UnplashRepositoryImpl @Inject constructor(private val unplashService: UnplashService) :
    UnplashRepository {

    override suspend fun getRandomPhotos(count: Int): List<RandomPhoto> {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unplashService.getRandomPhotos(clientId, count)
        return response
    }

    override suspend fun getSearchPhotos(query: String): SearchPhotos {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unplashService.getSearchPhotos(clientId, query)
        return response
    }
}