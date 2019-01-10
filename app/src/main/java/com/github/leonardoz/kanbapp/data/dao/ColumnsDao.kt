package com.github.leonardoz.kanbapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType
import java.util.*

@Dao
interface ColumnsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertColumn(column: Column): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertColumns(vararg columns: Column): List<Long>

    //    Getting "A/libc: Fatal signal 11 (SIGSEGV), code 1, fault addr 0x0 in tid 16241 (AsyncTask #1)"
    //     while inside async task
    //
    //    @Update(onConflict = OnConflictStrategy.REPLACE)
    //    fun updateColumnTitle(column: Column)

    @Query("UPDATE columns SET title = :title, updated_at = :updateAt WHERE id = :columnId")
    fun updateColumnTitle(title: String, updateAt: Date, columnId: Long)

    @Query("UPDATE columns SET `order`= :order, updated_at = :updateAt WHERE id = :columnId")
    fun updateColumnOrder(order: Int, updateAt: Date, columnId: Long)

    @Transaction
    fun updateColumnOrders(columnA: Column, columnB: Column) {
        updateColumnOrder(columnA.order, columnA.updatedAt, columnA.id)
        updateColumnOrder(columnB.order, columnB.updatedAt, columnB.id)
    }

    @Transaction
    fun insertColumnUpdatingOrder(newColumn: Column) {
        getColumnByTypeBlocking(newColumn.boardId, ColumnType.DONE_COLUMN)?.let { last ->
            val oldLast = last.order
            newColumn.order = oldLast
            last.order = oldLast + 1
            newColumn.id = insertColumn(newColumn)
            updateColumnOrder(last.order, Date(), last.id)
        }
    }

    @Query(
        "UPDATE columns SET `order`= `order` - 1, updated_at = :updateAt " +
                "WHERE board_id = :boardId AND `order` > :order"
    )
    fun updateColumnOrders(boardId: Long, order: Int, updateAt: Date)

    @Delete
    fun deleteColumn(column: Column)

    @Transaction
    fun deleteAndUpdateOrders(column: Column) {
        deleteColumn(column)
        updateColumnOrders(column.boardId, column.order, Date())
    }

    @Query("SELECT * FROM columns WHERE board_id = :boardId ORDER BY board_id, `order` ASC")
    fun getAllColumns(boardId: Long): LiveData<List<Column>>

    @Query("SELECT * FROM columns WHERE board_id = :boardId AND id = :id")
    fun getColumn(boardId: Long, id: Long): LiveData<Column?>

    @Query("SELECT * FROM columns WHERE board_id = :boardId AND id = :id")
    fun getColumnBlocking(boardId: Long, id: Long): Column?

    @Query("SELECT * FROM columns WHERE board_id = :boardId AND type = :type")
    fun getColumnByType(boardId: Long, type: ColumnType): LiveData<Column?>

    @Query("SELECT * FROM columns WHERE board_id = :boardId AND type = :type")
    fun getColumnByTypeBlocking(boardId: Long, type: ColumnType): Column?


}