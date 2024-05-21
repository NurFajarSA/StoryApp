package com.dicoding.storyapp.core.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener

class PasswordEditText(context: Context, attributeSet: AttributeSet) :
    AppCompatEditText(context, attributeSet) {

    init {
        addTextChangedListener {
            it?.let { text ->
                if (text.isNotEmpty()){
                    this.error = if (text.length < MIN_LENGTH) {
                        "Password must be longer than 8 characters"
                    } else null
                }
            }
        }
    }

    companion object {
        const val MIN_LENGTH = 8
    }
}