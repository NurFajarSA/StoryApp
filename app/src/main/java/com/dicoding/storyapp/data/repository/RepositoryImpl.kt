package com.dicoding.storyapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dicoding.storyapp.core.utils.Resource
import com.dicoding.storyapp.core.utils.asFlowStateEvent
import com.dicoding.storyapp.core.utils.asFlowStateEventWithAction
import com.dicoding.storyapp.data.local.DataPreferences
import com.dicoding.storyapp.data.paging.StoriesPagingSource
import com.dicoding.storyapp.data.remote.Api
import com.dicoding.storyapp.data.response.login.LoginResponse.Companion.toDomain
import com.dicoding.storyapp.data.response.story.StoriesResponse.Companion.toDomain
import com.dicoding.storyapp.domain.model.Story
import com.dicoding.storyapp.domain.model.User
import com.dicoding.storyapp.domain.repository.Repository
import com.dicoding.storyapp.domain.request.AddStoryRequest
import com.dicoding.storyapp.domain.request.LoginRequest
import com.dicoding.storyapp.domain.request.RegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class RepositoryImpl(
    val api: Api,
    val dataStore: DataPreferences,
    val pagingSource: StoriesPagingSource
) : Repository {
    override suspend fun doRegister(request: RegisterRequest): Flow<Resource<String>> =
        api.register(
            name = request.name,
            email = request.email,
            password = request.password
        ).asFlowStateEvent(
            mapper = { it.message ?: "User Created" }
        )

    override suspend fun doLogin(request: LoginRequest): Flow<Resource<User>> =
        api.login(
            email = request.email,
            password = request.password
        ).asFlowStateEventWithAction(
            mapper = { it.toDomain() },
            action = { dataStore.saveToken(it.loginResult?.token.orEmpty()) }
        )

    override suspend fun doLogout(): Flow<String> {
        dataStore.clearToken()
        return dataStore.isLogin
    }

    override suspend fun doAddStory(request: AddStoryRequest): Flow<Resource<String>> {
        val token = dataStore.token.first()
        val description = request.description.toRequestBody("text/plain".toMediaType())
        val photo = MultipartBody.Part.createFormData(
            name = "photo",
            filename = request.photo.name,
            body = request.photo.asRequestBody("image/*".toMediaType())
        )
        val lat = request.lat
        val lon = request.lon

        return if (token.isEmpty()) {
            api.addStoryGuest(description, photo, lat, lon).asFlowStateEvent(
                mapper = { it.message ?: "Story Added" }
            )
        } else {
            api.addStory("Bearer $token", description, photo, lat, lon).asFlowStateEvent(
                mapper = { it.message ?: "Story Added" }
            )
        }
    }

    override fun getStories(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { pagingSource }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}