package com.github.leonardoz.kanbapp.util

import android.content.Context
import com.github.leonardoz.kanbapp.R
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

fun mockContext() =
    mockMessages(mock(Context::class.java))


fun mockMessages(context: Context): Context {
    `when`(context.getString(R.string.required)).thenReturn("Required field")
    `when`(context.getString(R.string.max_length)).thenReturn("Must be less than %s chars")
    `when`(context.getString(R.string.min_length)).thenReturn("Must have at least %s chars")
    `when`(context.getString(R.string.is_negative)).thenReturn("Value must be positive")
    `when`(context.getString(R.string.past_date)).thenReturn("Date shouldn't be in the past")
    `when`(context.getString(R.string.future_date)).thenReturn("Date shouldn't be in the future")
    return context
}

