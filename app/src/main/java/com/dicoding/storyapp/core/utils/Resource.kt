package com.dicoding.storyapp.core.utils

sealed class Resource<T> {
    data class OnSuccess<T>(val data: T?) : Resource<T>()
    data class OnLoading<T>(val state: Boolean = true) : Resource<T>()
    data class OnError<T>(val uiText: UIText) : Resource<T>()
}