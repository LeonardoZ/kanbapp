package com.github.leonardoz.kanbapp.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@SmallTest
@RunWith(AndroidJUnit4::class)
open class ColumnsDaoTest : BaseDaoTest() {

    private lateinit var columnsDao: ColumnsDao
    private lateinit var board: Board

    @Before
    fun createParentBoard() {
        val boardsDao = database.boardsDao()
        val bid = boardsDao.insertBoard(Board(name = "Daily Tasks"))
        columnsDao = database.columnsDao()
        board = getValue(boardsDao.getBoard(bid))!!
    }

    @Test
    fun shouldInsertAnd_GetColumn() {
        val column = Column(boardId = board.id, title = "TODO", type = TODO_COLUMN)

        val id = columnsDao.insertColumn(column)

        getValue(columnsDao.getColumn(board.id, id))?.let {
            assertEquals(column.title, it.title)
            assertEquals(column.boardId, it.boardId)
            assertEquals(column.type, it.type)
            assertEquals(it.createdAt, it.updatedAt)
        }
    }

    @Test
    fun shouldInsertAnd_GetColumnByType() {
        val column = Column(boardId = board.id, title = "TODO", type = TODO_COLUMN)

        val id = columnsDao.insertColumn(column)

        getValue(columnsDao.getColumnByType(board.id, TODO_COLUMN))?.let {
            assertEquals(column.title, it.title)
            assertEquals(column.boardId, it.boardId)
            assertEquals(column.type, it.type)
            assertEquals(it.createdAt, it.updatedAt)
        }
    }

    @Test
    fun shouldUpdateAnd_GetColumn() {
        val column = Column(boardId = board.id, title = "Doing", order = 2, type = WORK_COLUMN)

        val id = columnsDao.insertColumn(column)

        getValue(columnsDao.getColumn(board.id, id))?.let {
            it.title = "Doing it"
            columnsDao.updateColumnTitle(it.title, it.updatedAt, it.id)
        }

        getValue(columnsDao.getColumn(board.id, id))?.let {
            assertEquals("Doing it", it.title)
            columnsDao.updateColumnOrder(it.order + 1, Date(), it.id)
        }

        getValue(columnsDao.getColumn(board.id, id))?.let {
            assertEquals(it.order, 3)
        }
    }


    @Test
    fun shouldUpdateAnd_GetColumns() {
        val columnA = Column(boardId = board.id, title = "Doing", order = 2, type = WORK_COLUMN)
        val columnB = Column(boardId = board.id, title = "Testing", order = 3, type = WORK_COLUMN)

        val (idA, idB) = columnsDao.insertColumns(columnA, columnB)
        columnA.id = idA
        columnB.id = idB

        columnB.order = 2
        columnA.order = 3

        columnA.updatedAt = Date()
        columnB.updatedAt = Date()

        columnsDao.updateColumnOrders(columnA, columnB)

        getValue(columnsDao.getAllColumns(board.id)).let { (a, b) ->
            assertEquals("Testing", a.title)
            assertEquals(2, a.order)
            assertEquals("Doing", b.title)
            assertEquals(3, b.order)
        }
    }

    @Test
    fun shouldInsertAnd_DeleteColumn() {
        val column = Column(boardId = board.id, title = "TODO", type = TODO_COLUMN)

        val id = columnsDao.insertColumn(column)

        getValue(columnsDao.getColumn(board.id, id))?.let {
            columnsDao.deleteColumn(it)
        }

        val deletedColumn = getValue(columnsDao.getColumn(board.id, id))
        assertNull(deletedColumn)
    }

    @Test
    fun shouldGet_AllColumns_OfBoard() {
        val todo = Column(boardId = board.id, title = "TODO", order = 1, type = TODO_COLUMN)
        val working = Column(boardId = board.id, title = "DEVELOPMENT", order = 2, type = WORK_COLUMN)
        val done = Column(boardId = board.id, title = "CONCLUDED", order = 3, type = DONE_COLUMN)

        val ids = columnsDao.insertColumns(todo, working, done)

        assertEquals(3, ids.size)

        val (_todo, _work, _done) = getValue(
            columnsDao.getAllColumns(boardId = board.id)
        )

        assertEquals("TODO", _todo.title)
        assertEquals("DEVELOPMENT", _work.title)
        assertEquals("CONCLUDED", _done.title)
    }

    @Test
    fun shouldUpdate_NextColumns() {
        val columnA = Column(boardId = board.id, title = "TODO", order = 1, type = TODO_COLUMN)
        val columnB = Column(boardId = board.id, title = "WORK", order = 2, type = WORK_COLUMN)
        val columnC = Column(boardId = board.id, title = "TEST", order = 3, type = WORK_COLUMN)
        val columnD = Column(boardId = board.id, title = "DONE", order = 4, type = DONE_COLUMN)

        columnsDao.insertColumns(columnA, columnC, columnD)
        val id = columnsDao.insertColumn(columnB)
        columnB.id = id

        columnsDao.deleteAndUpdateOrders(columnB)

        getValue(columnsDao.getAllColumns(board.id)).let { columns ->
            assertEquals(3, columns.size)
            assertEquals(1, columns[0].order)
            assertEquals(2, columns[1].order)
            assertEquals(3, columns[2].order)
        }

    }

    @Test
    fun shouldInsertAnd_UpdateLast_Column() {
        val columnA = Column(boardId = board.id, title = "TODO", order = 1, type = TODO_COLUMN)
        val columnB = Column(boardId = board.id, title = "WORK", order = 2, type = WORK_COLUMN)
        val columnC = Column(boardId = board.id, title = "DONE", order = 3, type = DONE_COLUMN)

        val newColumn = Column(boardId = board.id, title = "TEST", type = WORK_COLUMN)
        columnsDao.insertColumns(columnA, columnB, columnC)

        columnsDao.insertColumnUpdatingOrder(newColumn)
        getValue(columnsDao.getAllColumns(board.id)).let { columns ->
            assertEquals(4, columns.size)
            assertEquals(1, columns[0].order)
            assertEquals(2, columns[1].order)
            assertEquals(3, columns[2].order)
            assertEquals(4, columns[3].order)
        }
    }


}