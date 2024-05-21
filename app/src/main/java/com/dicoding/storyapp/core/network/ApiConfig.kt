package com.dicoding.storyapp.core.network

import com.dicoding.storyapp.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig (
    private val baseURL: String = BuildConfig.BASE_URL,
    private val client: OkHttpClient
) {
    fun <T> createService(service: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(service)
    }
}