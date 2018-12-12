package com.github.leonardoz.kanbapp.di

import android.app.Application
import androidx.room.Room
import com.github.leonardoz.kanbapp.data.KanbappDatabase
import com.github.leonardoz.kanbapp.data.MIGRATION_1_TO_2
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesDatabase(application: Application) = Room.databaseBuilder(
        application, KanbappDatabase::class.java,
        "kanbapp-database"
    ).addMigrations(MIGRATION_1_TO_2).build()

    @Singleton
    @Provides
    fun providesBoardsDao(database: KanbappDatabase) = database.boardsDao()


}