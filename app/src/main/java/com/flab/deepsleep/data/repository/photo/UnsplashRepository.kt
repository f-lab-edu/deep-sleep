package com.flab.deepsleep.data.repository.photo

import com.flab.deepsleep.data.entity.unplash.SinglePhoto
import com.flab.deepsleep.data.entity.unplash.SearchPhotos

interface UnsplashRepository {
    suspend fun getListPhotos(page: Int, perPage: Int): List<SinglePhoto>
    suspend fun getRandomPhotos(count: Int): List<SinglePhoto>
    suspend fun getSearchPhotos(query: String, page: Int, perPage: Int): SearchPhotos
    suspend fun getSinglePhotoById(photoId: String): SinglePhoto
}