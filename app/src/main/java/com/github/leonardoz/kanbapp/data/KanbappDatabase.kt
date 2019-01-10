package com.github.leonardoz.kanbapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.leonardoz.kanbapp.data.converter.Converters
import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.data.dao.ColumnsDao
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.Column

@TypeConverters(Converters::class)
@Database(entities = [Board::class, Column::class], version = 4, exportSchema = true)
abstract class KanbappDatabase : RoomDatabase() {
    abstract fun boardsDao(): BoardsDao
    abstract fun columnsDao(): ColumnsDao
}

