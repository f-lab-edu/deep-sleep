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
//@InstallIn(SingletonComponent::class)
//@Module
//class NetworkModule {
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
//            .baseUrl("https://api.unsplash.com/") // Base URL
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideUnplashService(retrofit: Retrofit): UnplashService {
//        return retrofit.create(UnplashService::class.java)
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
//}