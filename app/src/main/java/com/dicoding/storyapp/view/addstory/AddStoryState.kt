package com.dicoding.storyapp.view.addstory

import com.dicoding.storyapp.core.utils.UIText

sealed class AddStoryState {

    data class OnLoading(val state: Boolean = true) : AddStoryState()
    data class ShowMessage(val uiText: UIText) : AddStoryState()
    data object StoryUploaded : AddStoryState()

}