package com.dicoding.storyapp.data.remote

import com.dicoding.storyapp.data.response.BaseResponse
import com.dicoding.storyapp.data.response.login.LoginResponse
import com.dicoding.storyapp.data.response.story.StoriesResponse
import com.dicoding.storyapp.data.response.story.StoryItemResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @POST("register")
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Response<BaseResponse>

    @POST("login")
    suspend fun login(
        email: String,
        password: String
    ): Response<LoginResponse>

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") bearerToken: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ): Response<BaseResponse>

    @Multipart
    @POST("stories/guest")
    suspend fun addStoryGuest(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ): Response<BaseResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): Response<StoriesResponse>

    @GET("stories/{id}")
    suspend fun getStory(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String
    ): Response<StoryItemResponse>

}