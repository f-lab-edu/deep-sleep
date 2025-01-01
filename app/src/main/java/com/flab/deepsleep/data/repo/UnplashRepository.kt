package com.flab.deepsleep.data.repo

import com.flab.deepsleep.data.entity.photos.RandomPhoto
import com.flab.deepsleep.data.entity.search.SearchPhotos

interface UnplashRepository {
    suspend fun getRandomPhotos(count: Int) : List<RandomPhoto>
    suspend fun getSearchPhotos(query: String) : SearchPhotos
}