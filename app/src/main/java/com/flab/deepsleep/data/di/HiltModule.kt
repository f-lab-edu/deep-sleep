package com.flab.deepsleep.data.di

import com.flab.deepsleep.BuildConfig
import com.flab.deepsleep.data.api.UnplashService
import com.flab.deepsleep.data.repo.UnplashRepository
import com.flab.deepsleep.data.repo.UnplashRepositoryImpl
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    private const val BASE_URL = "https://api.unsplash.com/"
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addHttpLoggingInterceptor(this)
                    addInterceptor(OkHttpProfilerInterceptor())
                }
            }
            .build()
    }

    private fun addHttpLoggingInterceptor(builder: OkHttpClient.Builder) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(loggingInterceptor)
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUnplashService(retrofit: Retrofit): UnplashService {
        return HiltModule.retrofit.create(UnplashService::class.java)
    }

    @Provides
    @Singleton
    fun provideUnplashRepository(): UnplashRepository =
        UnplashRepositoryImpl(provideUnplashService(retrofit))
}