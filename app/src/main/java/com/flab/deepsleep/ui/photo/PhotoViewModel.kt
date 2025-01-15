package com.flab.deepsleep.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.entity.photos.toPhoto
import com.flab.deepsleep.data.entity.room.Photo
import com.flab.deepsleep.data.repository.db.PhotoRepository
import com.flab.deepsleep.data.repository.photo.UnplashRepositoryImpl
import com.flab.deepsleep.data.source.PhotoPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val unplashRepository: UnplashRepositoryImpl,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    /* Error */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /* Paging Flow */
    private val _photoState = MutableLiveData<List<SinglePhoto>?>()
    val photoState: MutableLiveData<List<SinglePhoto>?> get() = _photoState

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: Flow<PagingData<SinglePhoto>> = _photoState.asFlow()
        .map { state -> state ?: emptyList() }
        .flatMapLatest { photos ->
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = { PhotoPagingSource(photos) }
            ).flow
        }
        .cachedIn(viewModelScope)

    /* 사진 검색 */
    fun searchPhotos(query: String) {
        searchDebouncer(query)
    }

    /* 즐겨찾기 추가 */
    fun insertPhoto(singlePhoto: SinglePhoto) {
        viewModelScope.launch(Dispatchers.IO) {
            photoRepository.insertPhoto(singlePhoto.toPhoto())
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
        _photoState.value = null
        try {
            val photos = getSearchPhotos(query)
            _photoState.value = photos
        } catch (e: Exception) {
            e.printStackTrace()
            _photoState.value = emptyList()
        }
    }
}