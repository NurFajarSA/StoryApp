package com.dicoding.storyapp.view.register

import com.dicoding.storyapp.core.utils.UIText

sealed class RegisterState {
    data class OnLoading(val state: Boolean = true) : RegisterState()
    data class ShowMessage(val uiText: UIText) : RegisterState()
    data object GoToLogin : RegisterState()
    data object Idle : RegisterState()
}