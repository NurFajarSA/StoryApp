package com.dicoding.storyapp.view.addstory

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.core.utils.Resource
import com.dicoding.storyapp.core.utils.UIText
import com.dicoding.storyapp.data.repository.StoryRepository
import com.dicoding.storyapp.data.request.AddStoryRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddStoryViewModel (
    private val repository : StoryRepository
) : ViewModel(){

    private val _state = MutableLiveData<AddStoryState>()
    val state: LiveData<AddStoryState> = _state

    private val _description = MutableStateFlow("")
    private val _fileUri = MutableStateFlow<Uri?>(null)

    fun setDescription(description: String) {
        _description.value = description
    }

    fun setFileUri(uri: Uri) {
        _fileUri.value = uri
    }

    val isSubmitEnabled: Flow<Boolean> = combine(_fileUri, _description) { fileUri, description ->
        return@combine fileUri != null && description.isNotBlank()
    }

    fun uploadStory(request: AddStoryRequest) {
        viewModelScope.launch {
            repository.doAddStory(request).onEach {
                when (it) {
                    is Resource.OnSuccess -> {
                        _state.value = AddStoryState.OnLoading(false)
                        _state.value = AddStoryState.ShowMessage(UIText.DynamicText(it.data))
                        _state.value = AddStoryState.StoryUploaded
                    }
                    is Resource.OnLoading -> {
                        _state.value = AddStoryState.OnLoading(true)
                    }
                    is Resource.OnError -> {
                        _state.value = AddStoryState.OnLoading(false)
                        _state.value = AddStoryState.ShowMessage(it.uiText)
                    }
                }
            }.launchIn(this)
        }
    }
}