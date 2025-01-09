package com.flab.deepsleep.data.repository.db

import com.flab.deepsleep.data.entity.room.Photo
import com.flab.deepsleep.data.entity.room.PhotoDao
import kotlinx.coroutines.flow.Flow

class OffLinePhotoRepository(private val photoDao: PhotoDao) : PhotoRepository {
    override fun getAllPhotos(): Flow<List<Photo>> = photoDao.getAll()
    override suspend fun insertPhoto(like: Photo) = photoDao.insert(like)
    override suspend fun deletePhoto(like: Photo) = photoDao.delete(like)
}