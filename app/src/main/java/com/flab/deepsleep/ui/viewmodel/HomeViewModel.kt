package com.flab.deepsleep.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.flab.deepsleep.data.entity.room.Photo
import com.flab.deepsleep.data.entity.unplash.SinglePhoto
import com.flab.deepsleep.data.repository.db.PhotoRepository
import com.flab.deepsleep.data.repository.photo.PagingRepository
import com.flab.deepsleep.data.repository.photo.UnsplashRepository
import com.flab.deepsleep.ui.main.UiItem
import com.flab.deepsleep.ui.main.toPhoto
import com.flab.deepsleep.utils.Debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
    private val photoRepository: PhotoRepository,
    private val pagingRepository: PagingRepository
) : ViewModel() {

    /* Error */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /* Paging Flow */
    private val _query = MutableLiveData<String>()

    /* Room Flow */
    private val savedPhotos = photoRepository.getAllPhotos()

    private val queryFlow = _query.asFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _query.value
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: Flow<PagingData<UiItem>> = queryFlow
        .flatMapLatest { query ->
            pagingRepository.letPagingImagesFlow(
                query = query.orEmpty()
            ) { q, page ->
                getSearchPhotos(q, page)
            }
        }
        .cachedIn(viewModelScope)
        .combine(savedPhotos) { pagingData, savedPhotos ->
            pagingData.map { photo ->
                val savedPhoto = savedPhotos.find { it.id == photo.id }
                if (savedPhoto != null) {
                    mapToUiItem(photo, savedPhoto)
                } else {
                    mapToUiItem(photo, null).copy(isLike = false)
                }
            }
        }

    /* 사진 검색 */
    fun searchPhotos(query: String) {
        searchDebouncer(query)
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

    private suspend fun getSearchPhotos(query: String, page: Int): List<SinglePhoto> {
        return coroutineScope {
            try {
                unsplashRepository.getSearchPhotos(query, page, PagingRepository.DEFAULT_PAGE_SIZE)
                    .results
                    ?.mapNotNull { result ->
                        async {
                            result?.takeIf { it.description != null }?.id?.let { photoId ->
                                runCatching {
                                    unsplashRepository.getSinglePhotoById(photoId)
                                }.getOrNull()
                            }
                        }
                    }?.awaitAll()?.filterNotNull() ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    private fun mapToUiItem(photo: SinglePhoto, savedPhoto: Photo?): UiItem {
        return UiItem(
            id = photo.id,
            createdAt = photo.createdAt,
            description = photo.description,
            likes = photo.likes,
            urls = photo.urls?.raw,
            username = photo.user?.username,
            isLike = savedPhoto?.isLike ?: false
        )
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