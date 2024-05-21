package com.dicoding.storyapp.core.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener

class EmailEditText(context: Context, attributeSet: AttributeSet) :
    AppCompatEditText(context, attributeSet) {
    init {
        addTextChangedListener {
            it?.let { text ->
                if (text.isNotEmpty()) {
                    this.error = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                        "Email not valid"
                    } else null
                }
            }
        }
    }
}