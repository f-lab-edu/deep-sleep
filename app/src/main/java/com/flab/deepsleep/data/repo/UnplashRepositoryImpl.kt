package com.flab.deepsleep.data.repo

import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.api.UnplashService
import com.flab.deepsleep.data.entity.photo.RandomPhoto
import javax.inject.Inject

class UnplashRepositoryImpl @Inject constructor(private val unplashService: UnplashService): UnplashRepository {

    override suspend fun getRandomPhotos(count: Int): List<RandomPhoto> {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unplashService.getRandomPhotos(clientId, count)
        return response
    }
}