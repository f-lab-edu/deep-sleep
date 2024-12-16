package com.flab.deepsleep.ui.photo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flab.deepsleep.data.entity.PhotoResponse
import com.flab.deepsleep.data.entity.ResponseBody
import com.flab.deepsleep.domain.repo.UnplashRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class PhotoViewModel : ViewModel() {
    private val unplashRepository = UnplashRepository()
    private val _randomPhoto = MutableLiveData<String>()
    val randomPhoto: LiveData<String> get() = _randomPhoto

    fun getARandomPhoto(count: Int) {
        viewModelScope.launch {
            val result = unplashRepository.getARandomPhoto(count)
            result.onSuccess { it ->

                // TODO : url 가져오기
                val test = parseResponse(it.toString())
                val url = test.url
                Log.d("PhotoViewModel", "url " + url)

            }.onFailure { error ->
                Log.e("PhotoViewModel", error.message ?: "Unknown error")
            }
        }
    }

    fun parseResponse(responseString: String): ResponseBody {
        val regex = """Response\{protocol=(.*?), code=(\d+), message=(.*?), url=(.*?)\}""".toRegex()
        val matchResult = regex.find(responseString)

        return if (matchResult != null) {
            val (protocol, code, message, url) = matchResult.destructured
            ResponseBody(protocol, code.toInt(), message, url)
        } else {
            throw IllegalArgumentException("Invalid response format")
        }
    }
}