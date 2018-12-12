package com.github.leonardoz.kanbapp.util

import android.os.AsyncTask

interface AsyncAction {
    fun execute(action: () -> Unit)
}

class AndroidAsyncTask : AsyncAction {
    override fun execute(action: () -> Unit) = AsyncTask.execute {
        action()
    }

}

class FakeAsyncTask : AsyncAction {
    override fun execute(action: () -> Unit) {
        action()
    }
}