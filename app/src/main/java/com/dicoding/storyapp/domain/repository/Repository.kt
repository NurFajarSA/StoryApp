package com.dicoding.storyapp.domain.repository

import androidx.paging.PagingData
import com.dicoding.storyapp.core.utils.Resource
import com.dicoding.storyapp.domain.model.Story
import com.dicoding.storyapp.domain.model.User
import com.dicoding.storyapp.domain.request.AddStoryRequest
import com.dicoding.storyapp.domain.request.LoginRequest
import com.dicoding.storyapp.domain.request.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun doRegister(request: RegisterRequest): Flow<Resource<String>>

    suspend fun doLogin(request: LoginRequest): Flow<Resource<User>>

    suspend fun doLogout(): Flow<String>

    suspend fun doAddStory(request: AddStoryRequest): Flow<Resource<String>>

    fun getStories(): Flow<PagingData<Story>>
}