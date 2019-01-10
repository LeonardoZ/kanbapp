package com.github.leonardoz.kanbapp.data.converter

import androidx.room.TypeConverter
import com.github.leonardoz.kanbapp.data.entity.ColumnType
import java.util.*

class Converters {

    companion object {

        @JvmStatic
        @TypeConverter
        fun fromTimestamp(value: Long): Date = value.let { Date(it) }

        @JvmStatic
        @TypeConverter
        fun dateToTimestamp(date: Date): Long? = date.time

        @JvmStatic
        @TypeConverter
        fun columTypeToString(type: ColumnType) = type.name

        @JvmStatic
        @TypeConverter
        fun stringToColumnType(type: String) = ColumnType.valueOf(type)

    }
}