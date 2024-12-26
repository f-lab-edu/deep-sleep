package com.flab.deepsleep.ui.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.deepsleep.data.repo.UnplashRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(private val unplashRepositoryImpl: UnplashRepositoryImpl) : ViewModel() {

    private val _randomphotoUrl = MutableLiveData<String?>()
    val randomphotoUrl: LiveData<String?> get() = _randomphotoUrl

    fun getARandomPhoto(count: Int) {
        viewModelScope.launch {
            val result = unplashRepositoryImpl.getRandomPhotos(count)
            val url = result[0].urls
            if (url != null) {
                _randomphotoUrl.value = url.full
            }
        }
    }

}