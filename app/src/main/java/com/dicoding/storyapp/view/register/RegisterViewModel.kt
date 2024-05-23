package com.dicoding.storyapp.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.core.utils.Resource
import com.dicoding.storyapp.core.utils.UIText
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.request.RegisterRequest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RegisterViewModel (
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableLiveData<RegisterState>()
    val state: LiveData<RegisterState> = _state

    fun doRegister(request: RegisterRequest) {
        viewModelScope.launch {
            repository.doRegister(request).onEach { result ->
                when (result) {
                    is Resource.OnLoading -> {
                        _state.value = RegisterState.OnLoading(true)
                    }
                    is Resource.OnSuccess -> {
                        _state.value = RegisterState.OnLoading(false)
                        _state.value = RegisterState.ShowMessage(UIText.DynamicText(result.data))
                        _state.value = RegisterState.GoToLogin
                    }
                    is Resource.OnError -> {
                        _state.value = RegisterState.OnLoading(false)
                        _state.value = RegisterState.ShowMessage(result.uiText)
                    }
                }
            }.launchIn(this)
        }
    }
}