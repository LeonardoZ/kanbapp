package com.github.leonardoz.kanbapp.util

import org.mockito.Mockito

fun <T> safeAny(): T {
    Mockito.any<T>()
    return uninitialized()
}

private fun <T> uninitialized(): T = null as T