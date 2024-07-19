package com.dinesh.openinapp.di

import com.dinesh.openinapp.api.OpenInAppAPIService
import com.dinesh.openinapp.api.TokenInterceptor
import com.dinesh.openinapp.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(AppConstants.BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providesAPIService(retrofit: Retrofit) : OpenInAppAPIService{
        return retrofit.create(OpenInAppAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenInterceptor : TokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(tokenInterceptor).build()
    }
}