package com.flab.deepsleep.data.repository.db

import com.flab.deepsleep.data.entity.photo.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getAllPhotos(): Flow<List<Photo>>
    fun getSinglePhoto(id: String): Flow<Photo?>
    suspend fun insertPhoto(photo: Photo)
    suspend fun deletePhoto(id: String)
}