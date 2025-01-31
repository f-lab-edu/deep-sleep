package com.flab.deepsleep.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.entity.photos.toPhoto
import com.flab.deepsleep.data.entity.room.Photo
import com.flab.deepsleep.data.repository.db.PhotoRepository
import com.flab.deepsleep.data.repository.photo.PagingRepository
import com.flab.deepsleep.data.repository.photo.UnsplashRepository
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
    private val photoRepository: PhotoRepository,
    private val pagingRepository: PagingRepository
) : ViewModel() {

    /* Error */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /* Paging Flow */
    private val _query = MutableLiveData<String>()

    private val queryFlow = _query.asFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _query.value
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: Flow<PagingData<SinglePhoto>> = queryFlow
        .flatMapLatest { query ->
            pagingRepository.letPagingImagesFlow(
                query = query.orEmpty()
            ) { q, page ->
                getSearchPhotos(q, page)
            }
        }
        .cachedIn(viewModelScope)

    /* Room Flow */
    val allPhotos: Flow<List<Photo>> = photoRepository.getAllPhotos()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

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
        timeMillis = 500L,
        coroutineScope = viewModelScope
    ) { query ->
        try {
            _query.value = query
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}