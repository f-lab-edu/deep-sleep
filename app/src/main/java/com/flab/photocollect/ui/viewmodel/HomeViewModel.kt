package com.flab.photocollect.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.flab.photocollect.data.repository.db.PhotoRepository
import com.flab.photocollect.data.repository.photo.PagingRepository
import com.flab.photocollect.data.entity.room.UiItem
import com.flab.photocollect.utils.Debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val pagingRepository: PagingRepository
) : ViewModel() {

    /* Error */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /* Search Flow */
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    /* Room Flow */
    private val savedPhotos = photoRepository.getAllPhotos()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchUiItem(): Flow<PagingData<UiItem>> {
        return query
            .flatMapLatest { searchQuery ->
                pagingRepository.letPagingImagesFlowDb(searchQuery)
            }
            .cachedIn(viewModelScope)
            .combine(savedPhotos) { pagingData, savedPhotos ->
                pagingData.map { photo ->
                    if (savedPhotos.any { it.id == photo.id }) {
                        photo.copy(isLike = true)
                    } else {
                        photo.copy(isLike = false)
                    }
                }
            }
    }

    /* Bookmark 추가 */
    fun insertPhoto(uiItem: UiItem) {
        viewModelScope.launch {
            photoRepository.insertPhoto(uiItem.toPhoto())
        }
    }

    /* Bookmark 삭제 */
    fun deletePhoto(id: String) {
        viewModelScope.launch {
            photoRepository.deletePhoto(id)
        }
    }

    /* 사진 검색 */
    fun searchPhotos(query: String) {
        searchDebouncer(query)
    }

    private val searchDebouncer = Debounce.debounce<String>(
        timeMillis = 300L,
        coroutineScope = viewModelScope
    ) { query ->
        try {
            _query.value = query
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}