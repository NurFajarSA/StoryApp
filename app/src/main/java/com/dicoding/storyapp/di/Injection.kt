package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.core.network.ApiConfig
import com.dicoding.storyapp.core.utils.dataStore
import com.dicoding.storyapp.data.local.DataPreferences
import com.dicoding.storyapp.data.remote.StoryService
import com.dicoding.storyapp.data.remote.UserService
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.data.repository.UserRepository

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val pref = DataPreferences.getInstance(context.dataStore)
        val userService = ApiConfig.getApiService(UserService::class.java)
        return UserRepository.getInstance(
            dataPreference = pref,
            userService = userService,
        )
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = DataPreferences.getInstance(context.dataStore)
        val storyService = ApiConfig.getApiService(StoryService::class.java)
        return StoryRepository.getInstance(
            dataPreference = pref,
            storyService = storyService
        )
    }
}