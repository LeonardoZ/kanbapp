package com.github.leonardoz.kanbapp.util

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object BindingAdapters {

    @BindingAdapter("errorMsg")
    @JvmStatic
    fun TextInputLayout.setError(message: String?) {
        if (this.isErrorEnabled && message != null && !message.isBlank())
            this.error = message
    }

    @BindingAdapter("hasError")
    @JvmStatic
    fun TextInputLayout.hasError(hasError: Boolean) {
        this.isErrorEnabled = hasError
    }
}