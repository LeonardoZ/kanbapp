package com.github.leonardoz.kanbapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.leonardoz.kanbapp.data.converter.Converters
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.dao.BoardsDao

@TypeConverters(Converters::class)
@Database(entities = [Board::class], version = 1, exportSchema = false)
abstract class KanbappDatabase : RoomDatabase() {
    abstract fun boardsDao(): BoardsDao
}

val MIGRATION_1_TO_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
    }
}