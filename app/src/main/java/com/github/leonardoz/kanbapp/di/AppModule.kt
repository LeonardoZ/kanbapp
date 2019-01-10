package com.github.leonardoz.kanbapp.di

import android.app.Application
import android.content.Context
import com.github.leonardoz.kanbapp.util.AndroidAsyncTask
import com.github.leonardoz.kanbapp.view.form.FormValidatorFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var application: Application) {

    @Provides
    @Singleton
    fun providedApplication(): Application = application

    @Provides
    @Singleton
    fun providesAsyncAction(): AndroidAsyncTask = AndroidAsyncTask()

    @Provides
    @Singleton
    fun providesFormValidation(application: Application) =
        FormValidatorFactory(application.applicationContext)

    @Provides
    @Singleton
    fun providesAppContext(): Context = application.applicationContext
}