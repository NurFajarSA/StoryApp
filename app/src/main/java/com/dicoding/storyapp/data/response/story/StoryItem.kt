package com.dicoding.storyapp.data.response.story

import com.google.gson.annotations.SerializedName

data class StoryItem(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("photoUrl")
    val photoUrl: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("lat")
    val lat: Float? = null,

    @SerializedName("lon")
    val lon: Float? = null,
)
