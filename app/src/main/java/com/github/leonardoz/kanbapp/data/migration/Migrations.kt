package com.github.leonardoz.kanbapp.data.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_TO_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(SqlReference.createColumnTable)
    }
}

val MIGRATION_2_TO_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(SqlReference.addFieldOrderToColumn)
    }
}

val MIGRATION_3_TO_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(SqlReference.changeColumnTableName)
        database.execSQL(SqlReference.addSearchIndexToColumnTable)
    }
}