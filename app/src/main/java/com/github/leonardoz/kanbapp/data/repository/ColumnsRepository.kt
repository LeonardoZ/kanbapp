package com.github.leonardoz.kanbapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.data.dao.ColumnsDao
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType.DONE_COLUMN
import com.github.leonardoz.kanbapp.util.AsyncAction
import java.util.*

open class ColumnsRepository(
    private val columnsDao: ColumnsDao,
    private val asyncAction: AsyncAction
) : ViewModel() {

    open fun saveColumnChangingOrder(vararg columns: Column) = asyncAction.execute {
        columns.forEach { c ->
            columnsDao.insertColumnUpdatingOrder(c)
        }
    }

    open fun saveColumnChangingOrder(column: Column) = asyncAction.execute {
        columnsDao.insertColumnUpdatingOrder(column)
    }

    open fun saveColumns(vararg columns: Column) = asyncAction.execute {
        columnsDao.insertColumns(*columns)
    }

    open fun deleteColumn(column: Column) = asyncAction.execute {
        columnsDao.deleteAndUpdateOrders(column)
    }

    open fun updateColumnTitle(column: Column) = asyncAction.execute {
        column.updatedAt = Date()
        columnsDao.updateColumnTitle(column.title, column.updatedAt, column.id)
    }

    open fun updateColumnOrder(column: Column) = asyncAction.execute {
        column.updatedAt = Date()
        columnsDao.updateColumnOrder(column.order, column.updatedAt, column.id)
    }

    open fun updateColumnOrders(from: Column, to: Column) = asyncAction.execute {
        from.updatedAt = Date()
        to.updatedAt = Date()
        columnsDao.updateColumnOrders(from, to)
    }

    open fun getColumn(boardId: Long, id: Long) = columnsDao.getColumn(boardId, id)

    open fun getAllColumns(boardId: Long): LiveData<List<Column>> {
        return columnsDao.getAllColumns(boardId)
    }


}