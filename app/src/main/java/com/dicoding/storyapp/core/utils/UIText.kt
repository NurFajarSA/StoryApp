package com.dicoding.storyapp.core.utils

import androidx.annotation.StringRes

sealed class UIText {
    data class DynamicText(val value: String?) : UIText()
    data class StringResource(@StringRes val id: Int) : UIText()
}