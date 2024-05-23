package com.dicoding.storyapp.data.response.login

import com.dicoding.storyapp.data.response.BaseResponse
import com.dicoding.storyapp.data.model.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("loginResult")
    val loginResult: LoginResult? = null
) : BaseResponse() {

    companion object {
        fun LoginResponse.toDomain() = User(
            userId = loginResult?.userId.orEmpty(),
            name = loginResult?.name.orEmpty(),
            token = loginResult?.token.orEmpty()
        )
    }

}