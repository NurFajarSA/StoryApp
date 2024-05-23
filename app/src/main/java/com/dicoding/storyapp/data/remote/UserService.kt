package com.dicoding.storyapp.data.remote

import com.dicoding.storyapp.data.request.LoginRequest
import com.dicoding.storyapp.data.request.RegisterRequest
import com.dicoding.storyapp.data.response.BaseResponse
import com.dicoding.storyapp.data.response.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<BaseResponse>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

}

