package com.flab.deepsleep.ui.photo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.deepsleep.domain.repo.UnplashRepository
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {
    private val unplashRepository = UnplashRepository()

    private val _randomPhoto = MutableLiveData<String>()
    val randomPhoto: LiveData<String> get() = _randomPhoto

    fun getARandomPhoto(count: Int) {
        viewModelScope.launch {
            val result = unplashRepository.getARandomPhoto(count)
            result.onSuccess { photoList ->
                _randomPhoto.postValue(photoList.toString())
            }.onFailure { error ->
                Log.e("PhotoViewModel", error.message ?: "Unknown error")
            }
        }
    }

}