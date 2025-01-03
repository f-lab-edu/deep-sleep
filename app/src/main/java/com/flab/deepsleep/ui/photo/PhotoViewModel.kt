package com.flab.deepsleep.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flab.deepsleep.data.entity.paging.UiAction
import com.flab.deepsleep.data.entity.paging.UiState
import com.flab.deepsleep.data.entity.photos.SinglePhoto
import com.flab.deepsleep.data.repo.PhotoRepository
import com.flab.deepsleep.data.repo.UnplashRepositoryImpl
import com.flab.deepsleep.data.source.PhotoPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val unplashRepositoryImpl: UnplashRepositoryImpl
) : ViewModel() {
    /* Photo */
    private val _randomphotoUrl = MutableLiveData<String?>()
    val randomphotoUrl: LiveData<String?> get() = _randomphotoUrl
    val urlList = mutableListOf<String?>()
    private val _searchPhotosList = MutableLiveData<List<String?>>()

    /* Error */
    val searchPhotosList: LiveData<List<String?>> get() = _searchPhotosList
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /* Paging Flow */
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query
    val items = _query
        .debounce(300) // 검색어 입력 지연 처리
        .distinctUntilChanged()
        .flatMapLatest { query ->
            Pager(PagingConfig(pageSize = 20)) {
                PhotoPagingSource(unplashRepositoryImpl, query) // 검색어 전달
            }.flow
        }
        .cachedIn(viewModelScope)

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    /* 랜덤 사진 하나 출력 */
    fun getSingleRandomPhoto(count: Int) {
        viewModelScope.launch {
            try {
                val result = unplashRepositoryImpl.getRandomPhotos(count)
                val randomPhoto = result.getOrNull(0);
                if (randomPhoto != null) {
                    _randomphotoUrl.value = randomPhoto.urls?.full
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    /* 사진 검색 */
    fun getSearchPhotos(query: String) {
        viewModelScope.launch {
            try {
                val result = unplashRepositoryImpl.getSearchPhotos(query)
                for (results in result.results!!) {
                    if (results?.description != null) {
                        val id: String = results.id.toString()
                        getAPhotoById(id)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun getAPhotoById(id: String) {
        viewModelScope.launch {
            try {
                val result = id?.let {
                    val singlePhoto = unplashRepositoryImpl.getSinglePhotoById(it)
                    urlList.add(singlePhoto.urls?.full)
                }
                _searchPhotosList.value = urlList
                Timber.d("urlList " + urlList)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

}