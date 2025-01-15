package com.flab.deepsleep.data.repository.db

import com.flab.deepsleep.data.entity.room.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getAllPhotos(): Flow<List<Photo>>
    suspend fun insertPhoto(photo: Photo)
    suspend fun deletePhoto(photo: Photo)
}