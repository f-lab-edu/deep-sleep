package com.flab.deepsleep.data.repository.db

import com.flab.deepsleep.data.entity.photo.Photo
import com.flab.deepsleep.data.entity.photo.PhotoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OffLinePhotoRepository(private val photoDao: PhotoDao) : PhotoRepository {
    override fun getAllPhotos(): Flow<List<Photo>> = photoDao.getAll()
    override fun getSinglePhoto(id: String): Flow<Photo?> = photoDao.getPhoto(id)
    override suspend fun insertPhoto(photo: Photo) =
        withContext(Dispatchers.IO) { photoDao.insert(photo) }

    override suspend fun deletePhoto(id: String) =
        withContext(Dispatchers.IO) { photoDao.delete(id) }
}