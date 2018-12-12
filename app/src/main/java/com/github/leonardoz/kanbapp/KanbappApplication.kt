package com.github.leonardoz.kanbapp

import android.app.Application
import com.github.leonardoz.kanbapp.di.AppComponent
import com.github.leonardoz.kanbapp.di.AppModule
import com.github.leonardoz.kanbapp.di.DaggerAppComponent
import com.github.leonardoz.kanbapp.di.RoomModule
import com.squareup.leakcanary.LeakCanary

class KanbappApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
//        LeakCanary.install(this)
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .roomModule(RoomModule())
            .build()
    }

}
