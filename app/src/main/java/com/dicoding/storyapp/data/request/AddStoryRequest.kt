package com.dicoding.storyapp.data.request

import java.io.File

data class AddStoryRequest (
    val description: String,
    val photo: File,
    val lat: Float?,
    val lon: Float?
)