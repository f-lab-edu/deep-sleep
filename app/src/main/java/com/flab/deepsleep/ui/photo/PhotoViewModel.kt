package com.flab.deepsleep.ui.photo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.deepsleep.data.repo.UnplashRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(private val unplashRepositoryImpl: UnplashRepositoryImpl) : ViewModel() {

    fun getARandomPhoto(count: Int) {
        viewModelScope.launch {
            val result = unplashRepositoryImpl.getRandomPhotos(count)
            Log.d("PhotoViewModel", "확인 " + result)
        }
    }

}