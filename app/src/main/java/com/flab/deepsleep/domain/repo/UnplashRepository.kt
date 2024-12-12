package com.flab.deepsleep.domain.repo

import com.flab.deepsleep.data.api.UnplashService
import retrofit2.Response
import javax.inject.Inject

class UnplashRepository @Inject constructor(private val unplashService: UnplashService) {

    //TODO: Get Meahtod
    suspend fun getARandomPhoto(): Response<String>{
        return unplashService.getARandomPhoto();
    }
}