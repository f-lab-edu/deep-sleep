package com.flab.deepsleep.domain.repo

import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.api.NetworkModule
import com.flab.deepsleep.data.entity.PhotoResponse


class UnplashRepository  {
    private val unplashService = NetworkModule.unplashService

    suspend fun getARandomPhoto(count: Int): Result<List<PhotoResponse>> {
        return try {
            val clientId = BuildConfig.UNSPLASH_ACCESS_KEY // Unsplash API Access Key
            val response = unplashService.getRandomPhotos(clientId, count)

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}