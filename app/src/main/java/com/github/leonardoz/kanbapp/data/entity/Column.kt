package com.github.leonardoz.kanbapp.data.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*

enum class ColumnType {
    TODO_COLUMN, DONE_COLUMN, WORK_COLUMN
}

@Entity(
    tableName = "columns",
    foreignKeys = [
        ForeignKey(
            entity = Board::class,
            parentColumns = ["id"],
            childColumns = ["board_id"],
            onDelete = CASCADE
        )],
    indices = [Index(name = "idx_board_id_order", value = ["board_id", "order"])]
)
data class Column(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "type") var type: ColumnType,
    @ColumnInfo(name = "order") var order: Int = 1,
    @ColumnInfo(name = "board_id") var boardId: Long
) {
    @ColumnInfo(name = "created_at")
    var createdAt: Date = Date()
    @ColumnInfo(name = "updated_at")
    var updatedAt: Date = createdAt
}

object ColumnRestrictions {
    const val titleSizeLimit = 25
}