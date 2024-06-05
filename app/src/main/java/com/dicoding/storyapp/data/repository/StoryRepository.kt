package com.dicoding.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.dicoding.storyapp.core.utils.Resource
import com.dicoding.storyapp.core.utils.asFlowStateEvent
import com.dicoding.storyapp.data.local.DataPreferences
import com.dicoding.storyapp.data.model.Story
import com.dicoding.storyapp.data.paging.StoriesPagingSource
import com.dicoding.storyapp.data.remote.StoryService
import com.dicoding.storyapp.data.request.AddStoryRequest
import com.dicoding.storyapp.data.response.story.StoriesResponse.Companion.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class StoryRepository(
    val storyService: StoryService,
    val dataStore: DataPreferences,
    val pagingSource: StoriesPagingSource
) {
    suspend fun doAddStory(request: AddStoryRequest): Flow<Resource<String>> {
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
            storyService.addStoryGuest(description, photo, lat, lon).asFlowStateEvent(
                mapper = { it.message ?: "Story Added" }
            )
        } else {
            storyService.addStory("Bearer $token", description, photo, lat, lon).asFlowStateEvent(
                mapper = { it.message ?: "Story Added" }
            )
        }
    }

    fun getStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { pagingSource }
        ).liveData
    }

    suspend fun getStoriesWithLocation(): Flow<Resource<List<Story>>> {
        val bearerToken = "Bearer ${dataStore.token.first()}"
        return storyService.getStories(
            bearerToken = bearerToken,
            page = null,
            size = null,
            location = 1
        ).asFlowStateEvent(
            mapper = { it.listStory.toDomain() }
        )
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            dataPreference: DataPreferences,
            storyService: StoryService,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(
                    storyService = storyService,
                    dataStore = dataPreference,
                    pagingSource = StoriesPagingSource(storyService, dataPreference)
                )
            }.also { instance = it }
    }
}