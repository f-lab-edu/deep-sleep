package com.flab.deepsleep.data.di

import android.content.Context
import com.flab.deepsleep.data.api.UnsplashService
import com.flab.deepsleep.data.entity.room.AppDatabase
import com.flab.deepsleep.data.repository.db.PhotoRepository
import com.flab.deepsleep.data.repository.db.OffLinePhotoRepository
import com.flab.deepsleep.data.repository.photo.PagingRepository
import com.flab.deepsleep.data.repository.photo.UnsplashRepository
import com.flab.deepsleep.data.repository.photo.UnsplashRepositoryImpl
import com.itkacher.okprofiler.BuildConfig
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideUnplashService(retrofit: Retrofit): UnsplashService {
        return HiltModule.retrofit.create(UnsplashService::class.java)
    }

    @Provides
    @Singleton
    fun provideUnplashRepository(apiService: UnsplashService): UnsplashRepository {
        return UnsplashRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun providePagingRepository() : PagingRepository {
        return PagingRepository()
    }

    /*-- Room Database --*/
    @Provides
    @Singleton
    fun provideItemsRepository(database: AppDatabase): PhotoRepository {
        return OffLinePhotoRepository(database.photoDao())
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
}