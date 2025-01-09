package com.flab.deepsleep.data.repository.photo

import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.entity.photos.SearchPhotos

interface UnplashRepository {
    suspend fun getRandomPhotos(count: Int): List<SinglePhoto>
    suspend fun getSearchPhotos(query: String): SearchPhotos
    suspend fun getSinglePhotoById(photoId: String): SinglePhoto
}