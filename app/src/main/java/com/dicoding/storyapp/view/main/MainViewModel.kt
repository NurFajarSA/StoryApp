package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.model.Story
import com.dicoding.storyapp.data.model.User
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
): ViewModel() {

    fun getStories(): Flow<PagingData<Story>> {
        return storyRepository.getStories().cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<User> {
        return userRepository.getUser().asLiveData()
    }
    fun logout(){
        viewModelScope.launch {
            userRepository.doLogout()
        }
    }

}