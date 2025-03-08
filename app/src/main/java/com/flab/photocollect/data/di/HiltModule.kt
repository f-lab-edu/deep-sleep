package com.flab.photocollect.data.di

import android.content.Context
import com.flab.photocollect.data.api.UnsplashService
import com.flab.photocollect.data.entity.photo.PhotoDatabase
import com.flab.photocollect.data.entity.room.AppDatabase
import com.flab.photocollect.data.repository.db.PhotoRepository
import com.flab.photocollect.data.repository.db.OffLinePhotoRepository
import com.flab.photocollect.data.repository.photo.PagingRepository
import com.flab.photocollect.data.repository.photo.UnsplashRepository
import com.flab.photocollect.data.repository.photo.UnsplashServiceImpl
import com.flab.photocollect.BuildConfig
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
                    addOkHttpProfilerInterceptor(this)
                    addHttpLoggingInterceptor(this)
                }
            }
            .build()
    }

    private fun addOkHttpProfilerInterceptor(builder: OkHttpClient.Builder) {
        builder.addInterceptor(OkHttpProfilerInterceptor())
    }

    private fun addHttpLoggingInterceptor(builder: OkHttpClient.Builder) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
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
    fun provideUnsplashService(): UnsplashService {
        return retrofit.create(UnsplashService::class.java)
    }

    @Provides
    @Singleton
    fun provideUnsplashRepository(apiService: UnsplashService): UnsplashRepository {
        return UnsplashServiceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providePagingRepository(
        appDatabase: AppDatabase,
        unsplashRepository: UnsplashRepository
    ): PagingRepository {
        return PagingRepository(appDatabase, unsplashRepository)
    }

    /*-- Room Database --*/
    @Provides
    @Singleton
    fun provideItemsRepository(database: PhotoDatabase): PhotoRepository {
        return OffLinePhotoRepository(database.photoDao())
    }

    @Provides
    @Singleton
    fun providePhotoDatabase(@ApplicationContext context: Context): PhotoDatabase {
        return PhotoDatabase.getDatabase(context)
    }
}