package com.flab.deepsleep.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.deepsleep.data.entity.room.UiItem
import com.flab.deepsleep.data.entity.room.toPhoto
import com.flab.deepsleep.data.repository.db.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    /* Bookmark */
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked

    /* Bookmark 추가 */
    fun insertPhoto(uiItem: UiItem) {
        viewModelScope.launch {
            photoRepository.insertPhoto(uiItem.toPhoto())
            _isLiked.value = true
        }
    }

    /* Bookmark 삭제 */
    fun deletePhoto(id: String) {
        viewModelScope.launch {
            photoRepository.deletePhoto(id)
            _isLiked.value = false
        }
    }

    fun loadPhotoLikeStatus(photoId: String) {
        viewModelScope.launch {
            photoRepository.getSinglePhoto(photoId)
                .collectLatest { photo ->
                    _isLiked.value = photo?.isLike ?: false
                }
        }
    }

}