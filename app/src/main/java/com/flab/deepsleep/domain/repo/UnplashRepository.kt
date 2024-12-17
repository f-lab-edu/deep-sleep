package com.flab.deepsleep.domain.repo

import android.util.Log
import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.api.NetworkModule

class UnplashRepository  {
    private val unplashService = NetworkModule.unplashService

    suspend fun getARandomPhoto(count: Int): Result<String?> {
        return try {
            val clientId = BuildConfig.UNSPLASH_ACCESS_KEY // Unsplash API Access Key
            val response = unplashService.getRandomPhotos(clientId, count)

            if (response.isSuccessful) {
                Log.d("UnplashRepository", response.toString())
                Result.success(response.toString())
            } else {
                Log.d("UnplashRepository", "else")
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}