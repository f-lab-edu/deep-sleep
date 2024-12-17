package com.flab.deepsleep.data.repo

import okhttp3.ResponseBody

interface UnplashRepository {
    suspend fun getRandomPhotos(count: Int) : ResponseBody
}