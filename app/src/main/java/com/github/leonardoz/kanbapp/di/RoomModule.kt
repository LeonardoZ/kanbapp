package com.github.leonardoz.kanbapp.di

import android.app.Application
import androidx.room.Room
import com.github.leonardoz.kanbapp.data.KanbappDatabase
import com.github.leonardoz.kanbapp.data.migration.MIGRATION_1_TO_2
import com.github.leonardoz.kanbapp.data.migration.MIGRATION_2_TO_3
import com.github.leonardoz.kanbapp.data.migration.MIGRATION_3_TO_4
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
    ).addMigrations(
        MIGRATION_1_TO_2,
        MIGRATION_2_TO_3,
        MIGRATION_3_TO_4).build()

    @Singleton
    @Provides
    fun providesBoardsDao(database: KanbappDatabase) = database.boardsDao()

    @Singleton
    @Provides
    fun providesColumnsDao(database: KanbappDatabase) = database.columnsDao()


}