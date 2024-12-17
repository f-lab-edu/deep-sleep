package com.flab.deepsleep.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flab.deepsleep.data.repo.UnplashRepositoryImpl
import com.flab.deepsleep.ui.photo.PhotoViewModel

class PhotoViewModelFactory(
    private val unsplashRepositoryImpl: UnplashRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(unsplashRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}