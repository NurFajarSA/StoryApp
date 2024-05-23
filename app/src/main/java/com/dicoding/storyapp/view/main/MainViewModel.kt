package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.model.User
import com.dicoding.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    fun getUser(): LiveData<User> {
        return userRepository.getUser().asLiveData()
    }
    fun logout(){
        viewModelScope.launch {
            userRepository.doLogout()
        }
    }
}