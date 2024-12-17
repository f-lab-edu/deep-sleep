package com.flab.deepsleep.data.repo

import android.util.Log
import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.api.UnplashService
import okhttp3.ResponseBody
import javax.inject.Inject

class UnplashRepositoryImpl @Inject constructor(private val unplashService: UnplashService): UnplashRepository {

    override suspend fun getRandomPhotos(count: Int): ResponseBody {
        val clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        val response = unplashService.getRandomPhotos(clientId, count)
        Log.d("UnplashRepositoryImpl", "?? ")
        return response
    }

}