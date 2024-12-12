package com.flab.deepsleep.ui.photo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.deepsleep.domain.repo.UnplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val unplashRepository: UnplashRepository): ViewModel() {

    private val _randomPhoto = MutableLiveData<String>()
    val randomPhoto: LiveData<String> get() = _randomPhoto

    fun getARandomPhoto(){
        viewModelScope.launch {
            try{
                val tempString = unplashRepository.getARandomPhoto()
                _randomPhoto.postValue(tempString.toString())

            }catch (e: Exception){
                Log.e("PhotoViewModel", e.message.toString())
            }
        }
    }

}