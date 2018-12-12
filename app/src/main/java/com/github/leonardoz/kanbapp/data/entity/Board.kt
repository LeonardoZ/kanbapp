package com.github.leonardoz.kanbapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "boards")
data class Board(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "created_at") var createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at") var updatedAt: Date = Date()
)

object BoardRestrictions {
    const val nameSizeLimit = 25
}