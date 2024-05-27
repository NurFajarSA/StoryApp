package com.dicoding.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.core.utils.Resource
import com.dicoding.storyapp.data.model.Story
import com.dicoding.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MapsViewModel(
    private val repository: StoryRepository
) : ViewModel() {

    private val _resultStories = MutableLiveData<List<Story>?>()
    val resultStories: LiveData<List<Story>?> = _resultStories

    private val _state = MutableLiveData<MapsState>()
    val state: LiveData<MapsState> = _state

    fun getStories() {
        _state.value = MapsState.OnLoading(true)
        viewModelScope.launch {
            repository.getStoriesWithLocation().onEach { result ->
                when (result) {
                    is Resource.OnSuccess -> {
                        _state.value = MapsState.OnLoading(false)
                        _resultStories.value = result.data
                    }
                    is Resource.OnError -> {
                        _state.value = MapsState.OnLoading(false)
                        _state.value = MapsState.ShowMessage(result.uiText)
                    }

                    else -> {}
                }
            }.launchIn(this)
        }
    }
}