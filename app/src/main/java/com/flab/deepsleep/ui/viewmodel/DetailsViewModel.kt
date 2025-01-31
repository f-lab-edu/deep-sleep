package com.flab.deepsleep.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.deepsleep.data.repository.db.PhotoRepository
import com.flab.deepsleep.ui.photo.UiItem
import com.flab.deepsleep.ui.photo.toPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    /* Bookmark */
    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> get() = _isLiked

    /* 즐겨찾기 추가 */
    fun insertPhoto(uiItem: UiItem) {
        viewModelScope.launch {
            photoRepository.insertPhoto(uiItem.toPhoto())
        }
    }

    fun loadPhotoLikeStatus(photoId: String) {
        viewModelScope.launch {
            photoRepository.getSinglePhoto(photoId).collectLatest { photo ->
                photo?.let {
                    _isLiked.value = photo.isLike
                }
            }
        }
    }
}