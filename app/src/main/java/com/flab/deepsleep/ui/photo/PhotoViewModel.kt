package com.flab.deepsleep.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.deepsleep.data.repo.UnplashRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(private val unplashRepositoryImpl: UnplashRepositoryImpl) : ViewModel() {

    private val _randomphotoUrl = MutableLiveData<String?>()
    val randomphotoUrl: LiveData<String?> get() = _randomphotoUrl

    val urlList = mutableListOf<String?>()
    private val _searchPhotosList = MutableLiveData<List<String?>>()
    val searchPhotosList: LiveData<List<String?>> get() = _searchPhotosList


    /* 랜덤 사진 하나 출력 */
    fun getARandomPhoto(count: Int) {
        viewModelScope.launch {
            try {
                val result = unplashRepositoryImpl.getRandomPhotos(count)
                val randomPhoto = result.getOrNull(0);
                if (randomPhoto != null) {
                    _randomphotoUrl.value = randomPhoto.urls?.full
                }
            } catch (e: IOException) {
                networkError(e)
            }
        }
    }


    /* 사진 검색 */
    fun getSearchPhotos(query: String) {
        viewModelScope.launch {
            try {
                val result = unplashRepositoryImpl.getSearchPhotos(query)
                Timber.d("getSearchPhotos() " + result)

                for(results in result.results){
                    if (results?.description != null) {
                        val id: String = results.id.toString()
                        getAPhotoById(id)
                    }
                }

            } catch (e: IOException) {
                networkError(e)
            }
        }
    }

    fun getAPhotoById(id: String){
        viewModelScope.launch {
            try {
                val result = id?.let {
                    val singlePhoto = unplashRepositoryImpl.getAPhotoById(it)
                    urlList.add(singlePhoto.urls?.full)
                }
                _searchPhotosList.value = urlList
                Timber.d("urlList " + urlList)
            }catch (e: IOException){
                networkError(e)
            }
        }
    }

    /* Error */
    fun networkError(e: IOException){
        Timber.e("Network error: ${e.localizedMessage}")
    }

}