package com.dicoding.storyapp.data.response.story

import com.dicoding.storyapp.data.response.BaseResponse
import com.dicoding.storyapp.data.model.Story
import com.google.gson.annotations.SerializedName

class StoriesResponse : BaseResponse() {

    @SerializedName("listStory")
    var listStory: List<StoryItem>? = null

    companion object {
        fun StoryItem.toDomain() : Story = Story(
            id = id.orEmpty(),
            name = name.orEmpty(),
            description = description.orEmpty(),
            photoUrl = photoUrl.orEmpty(),
            createdAt = createdAt.orEmpty(),
            lat = lat ?: 0.0F,
            lon = lon ?: 0.0F
        )

        fun List<StoryItem>?.toDomain() : List<Story> = this?.map {
            Story(
                id = it.id.orEmpty(),
                name = it.name.orEmpty(),
                description = it.description.orEmpty(),
                photoUrl = it.photoUrl.orEmpty(),
                createdAt = it.createdAt.orEmpty(),
                lat = it.lat ?: 0.0F,
                lon = it.lon ?: 0.0F
            )
        } ?: emptyList()
    }
}