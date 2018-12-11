package com.github.leonardoz.kanbapp.di

import com.github.leonardoz.kanbapp.BoardsActivity
import com.github.leonardoz.kanbapp.KanbappApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class])
interface AppComponent {
    fun inject(app: KanbappApplication)

    fun injectActivity(app: BoardsActivity)

}