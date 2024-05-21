package com.dicoding.storyapp.domain.request

import java.io.File

data class AddStoryRequest (
    val description: String,
    val photo: File,
    val lat: Float,
    val lon: Float
)