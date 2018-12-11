package com.github.leonardoz.kanbapp

import android.app.Application
import com.github.leonardoz.kanbapp.di.AppComponent
import com.github.leonardoz.kanbapp.di.AppModule
import com.github.leonardoz.kanbapp.di.DaggerAppComponent
import com.github.leonardoz.kanbapp.di.RoomModule

class KanbappApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .roomModule(RoomModule())
            .build()
    }

}
