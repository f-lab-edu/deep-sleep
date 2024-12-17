//package com.flab.deepsleep.data.di
//
//import com.flab.deepsleep.BuildConfig
//import com.flab.deepsleep.data.api.UnplashService
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class NetworkModule {
//    private val BASE_URL = "https://api.unsplash.com/"
//
//    //TODO : DI TEST
//    @Provides
//    @Singleton
//    fun provideTestString(): String{
//        return "This is a sample string from DI!"
//    }
//
//    // TODO : Network Module
//    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//    } else {
//        OkHttpClient.Builder().build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideUnplashService(retrofit: Retrofit): UnplashService {
//        return retrofit.create(UnplashService::class.java)
//    }
//}