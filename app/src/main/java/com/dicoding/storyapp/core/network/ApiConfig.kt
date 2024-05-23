package com.dicoding.storyapp.core.network

import com.dicoding.storyapp.BuildConfig
import com.dicoding.storyapp.core.utils.OkHttpsClientFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig{
    companion object {
        private const val baseURL: String = BuildConfig.BASE_URL
        fun <T> getApiService(service: Class<T>): T {
            val client = OkHttpsClientFactory.create()
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(service)
        }
    }
}