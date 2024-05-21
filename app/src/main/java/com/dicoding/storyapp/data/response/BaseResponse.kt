package com.dicoding.storyapp.data.response

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
