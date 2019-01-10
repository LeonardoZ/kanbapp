package com.github.leonardoz.kanbapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.leonardoz.kanbapp.data.dao.ColumnsDao
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType.*
import com.github.leonardoz.kanbapp.util.FakeAsyncTask
import junit.framework.TestCase.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import java.util.*

@RunWith(JUnit4::class)
class ColumnsRepositoryTest {

    private val columnsDao = mock(ColumnsDao::class.java)
    private val repository = ColumnsRepository(columnsDao, FakeAsyncTask())
    private val board = Board(id = 42, name = "Cleaning")

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shouldCall_getAllColumns_InDao() {
        repository.getAllColumns(board.id)
        verify(columnsDao).getAllColumns(board.id)
    }

    @Test
    fun shouldCall_getColumn_InDao() {
        repository.getColumn(board.id, 12)
        verify(columnsDao).getColumn(board.id, 12)
    }

    @Test
    fun shouldTest_ifInsertIsCalled_Correctly() {
        val toBeInserted = Column(boardId = board.id, title = "Doing", type = WORK_COLUMN)
        repository.saveColumnChangingOrder(toBeInserted)
        verify(columnsDao).insertColumnUpdatingOrder(toBeInserted)
    }

    @Test
    fun shouldTest_ifUpdateTitleIsCalled_Correctly() {
        val toBeUpdated = Column(boardId = board.id, title = "TODO", type = TODO_COLUMN)

        assertTrue(toBeUpdated.createdAt == toBeUpdated.updatedAt)

        val calendar = GregorianCalendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK) - 1)
        repository.updateColumnTitle(toBeUpdated)
        toBeUpdated.createdAt = calendar.time

        verify(columnsDao).updateColumnTitle("TODO", toBeUpdated.updatedAt, toBeUpdated.id)
        assertTrue(toBeUpdated.createdAt.before(toBeUpdated.updatedAt))
    }

    @Test
    fun shouldTest_ifUpdateOrderIsCalled_Correctly() {
        val toBeUpdated = Column(boardId = board.id, title = "Doing", order = 2, type = WORK_COLUMN)

        assertTrue(toBeUpdated.createdAt == toBeUpdated.updatedAt)

        val calendar = GregorianCalendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK) - 1)

        toBeUpdated.order = 3

        repository.updateColumnOrder(toBeUpdated)
        toBeUpdated.createdAt = calendar.time

        verify(columnsDao).updateColumnOrder(3, toBeUpdated.updatedAt, toBeUpdated.id)
        assertTrue(toBeUpdated.createdAt.before(toBeUpdated.updatedAt))
    }

    @Test
    fun shouldTest_ifUpdateOrdersIsCalled_Correctly() {
        val toBeUpdated = Column(boardId = board.id, title = "Doing", order = 2, type = WORK_COLUMN)
        val toBeUpdated2 = Column(boardId = board.id, title = "Testing", order = 3, type = WORK_COLUMN)

        assertTrue(toBeUpdated.createdAt == toBeUpdated.updatedAt)
        assertTrue(toBeUpdated2.createdAt == toBeUpdated2.updatedAt)

        val calendar = GregorianCalendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK) - 1)

        toBeUpdated.order = 3
        toBeUpdated2.order = 2

        repository.updateColumnOrders(toBeUpdated, toBeUpdated2)
        toBeUpdated.createdAt = calendar.time
        toBeUpdated2.createdAt = calendar.time

        verify(columnsDao).updateColumnOrders(toBeUpdated, toBeUpdated2)
        assertTrue(toBeUpdated.createdAt.before(toBeUpdated.updatedAt))
        assertTrue(toBeUpdated2.createdAt.before(toBeUpdated2.updatedAt))
    }

    @Test
    fun shouldTest_ifDeleteIsCalled_Correctly() {
        val toBeDeleted = Column(boardId = board.id, title = "TODO", type = TODO_COLUMN)
        repository.deleteColumn(toBeDeleted)
        verify(columnsDao).deleteAndUpdateOrders(toBeDeleted)
    }


}