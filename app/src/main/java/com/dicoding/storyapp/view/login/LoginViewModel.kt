package com.dicoding.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.request.LoginRequest
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.core.utils.Resource
import com.dicoding.storyapp.data.model.User
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel (
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState> = _state

    fun doLogin(request: LoginRequest) {
        _state.value = LoginState.OnLoading(true)
        viewModelScope.launch {
            repository.doLogin(request).onEach { result ->
                when (result) {
                    is Resource.OnLoading -> {
                        _state.value = LoginState.OnLoading(true)
                    }

                    is Resource.OnSuccess -> {
                        _state.value = LoginState.OnLoading(false)
                        _state.value = LoginState.GoToStories
                    }

                    is Resource.OnError -> {
                        _state.value = LoginState.OnLoading(false)
                        _state.value = LoginState.ShowMessage(result.uiText)
                    }
                }
            }.launchIn(this)
        }
    }
}