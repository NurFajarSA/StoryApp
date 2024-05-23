package com.dicoding.storyapp.data.response.story

import com.dicoding.storyapp.data.response.BaseResponse
import com.dicoding.storyapp.data.model.Story

data class StoryItemResponse(
    val story: Story? = null
): BaseResponse() {
    companion object {
        fun StoryItemResponse.toDomain() : Story = Story(
            id = story?.id.orEmpty(),
            name = story?.name.orEmpty(),
            description = story?.description.orEmpty(),
            photoUrl = story?.photoUrl.orEmpty(),
            createdAt = story?.createdAt.orEmpty(),
            lat = story?.lat ?: 0.0F,
            lon = story?.lon ?: 0.0F
        )
    }
}