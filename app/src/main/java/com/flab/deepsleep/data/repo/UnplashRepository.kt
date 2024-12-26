package com.flab.deepsleep.data.repo

import com.flab.deepsleep.data.entity.photo.RandomPhoto

interface UnplashRepository {
    suspend fun getRandomPhotos(count: Int) : List<RandomPhoto>
}