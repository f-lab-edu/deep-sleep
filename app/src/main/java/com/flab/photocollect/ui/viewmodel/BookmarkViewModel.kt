package com.flab.photocollect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.photocollect.data.entity.photo.Photo
import com.flab.photocollect.data.repository.db.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val photoRepository: PhotoRepository) :
    ViewModel() {

    /* Bookmark 조회 */
    fun getAllPhotos(): Flow<List<Photo>> = photoRepository.getAllPhotos()

    /* Bookmark 삭제 */
    fun deletePhoto(id: String) {
        viewModelScope.launch {
            photoRepository.deletePhoto(id)
        }
    }
}