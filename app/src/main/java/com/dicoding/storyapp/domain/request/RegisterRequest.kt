package com.dicoding.storyapp.domain.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
