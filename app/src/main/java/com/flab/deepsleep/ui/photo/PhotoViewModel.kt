package com.flab.deepsleep.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.deepsleep.data.repo.UnplashRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(private val unplashRepositoryImpl: UnplashRepositoryImpl) : ViewModel() {

    private val _randomphotoUrl = MutableLiveData<String?>()
    val randomphotoUrl: LiveData<String?> get() = _randomphotoUrl

    fun getARandomPhoto(count: Int) {
        viewModelScope.launch {
            val result = unplashRepositoryImpl.getRandomPhotos(count)
            val randomPhoto = result.getOrNull(0);
            if (randomPhoto != null) {
                _randomphotoUrl.value = randomPhoto.urls?.full
            }
        }
    }

    fun getSearchPhotos(query: String){
        Timber.plant(Timber.DebugTree())
        viewModelScope.launch {
            val result = unplashRepositoryImpl.getSearchPhotos(query)
            Timber.d("TIMBER " + result)
        }
    }

}