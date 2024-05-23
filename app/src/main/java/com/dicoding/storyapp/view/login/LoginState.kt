package com.dicoding.storyapp.view.login

import com.dicoding.storyapp.core.utils.UIText

sealed class LoginState {
    data class OnLoading(val state: Boolean = true) : LoginState()
    data class ShowMessage(val uiText: UIText) : LoginState()
    data object GoToStories : LoginState()
    data object Idle : LoginState()
}