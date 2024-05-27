package com.dicoding.storyapp.view.maps

import com.dicoding.storyapp.core.utils.UIText

sealed class MapsState {
    data class OnLoading(val state: Boolean = true) : MapsState()
    data class ShowMessage(val uiText: UIText) : MapsState()
}