package com.flab.deepsleep.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.entity.room.Photo
import com.flab.deepsleep.data.repository.db.PhotoRepository
import com.flab.deepsleep.data.repository.photo.UnplashRepositoryImpl
import com.flab.deepsleep.data.source.PhotoPagingSource
import com.flab.deepsleep.ui.listener.UiItem
import com.flab.deepsleep.ui.listener.toPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val unplashRepository: UnplashRepositoryImpl,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    /* Error */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /* Paging Flow */
    private val _apiPhotoState = MutableLiveData<List<SinglePhoto>?>()

    /* Room Flow */
    private val savedPhotos = photoRepository.getAllPhotos()

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: Flow<PagingData<UiItem>> = _apiPhotoState.asFlow()
        .map { state -> state ?: emptyList() }
        .flatMapLatest { photos ->
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = { PhotoPagingSource(photos) }
            ).flow
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

    private suspend fun getSearchPhotos(query: String): List<SinglePhoto> {
        return coroutineScope {
            try {
                unplashRepository.getSearchPhotos(query)
                    .results
                    ?.mapNotNull { result ->
                        async {
                            result?.takeIf { it.description != null }?.id?.let { photoId ->
                                runCatching {
                                    unplashRepository.getSinglePhotoById(photoId)
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

    private fun <T> debounce(
        timeMillis: Long = 300L,
        coroutineScope: CoroutineScope,
        block: suspend (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel() // 이전 작업 취소
            debounceJob = coroutineScope.launch {
                delay(timeMillis)
                block(param)
            }
        }
    }

    private val searchDebouncer = debounce<String>(
        timeMillis = 300L,
        coroutineScope = viewModelScope
    ) { query ->
        _apiPhotoState.value = null
        try {
            val photos = getSearchPhotos(query)
            _apiPhotoState.value = photos
        } catch (e: Exception) {
            e.printStackTrace()
            _apiPhotoState.value = emptyList()
        }
    }
}