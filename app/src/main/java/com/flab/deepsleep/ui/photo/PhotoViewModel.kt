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

    fun getARandomPhoto(count: Int) {
        viewModelScope.launch {
            try {
                val result = unplashRepositoryImpl.getRandomPhotos(count)
                val randomPhoto = result.getOrNull(0);
                if (randomPhoto != null) {
                    _randomphotoUrl.value = randomPhoto.urls?.full
                }
            }catch (e: IOException){
                Timber.e("Network error: ${e.localizedMessage}")
            }
        }
    }

    fun getSearchPhotos(query: String){
        viewModelScope.launch {
            try {
                val result = unplashRepositoryImpl.getSearchPhotos(query)
                Timber.d("TIMBER " + result)
            }catch (e: IOException){
                Timber.e("Network error: ${e.localizedMessage}")
            }
        }
    }

