package com.github.leonardoz.kanbapp.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnType
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
open class BoardsDaoTest : BaseDaoTest() {

    @Test
    fun shouldInsertAndGet_NewBoard() {
        val boardName = "My daily task"
        val board = Board(name = boardName)
        val boardsDao = database.boardsDao()
        val insertedId = boardsDao.insertBoard(board)

        getValue(boardsDao.getBoard(insertedId)).let { savedBoard ->
            assertNotNull(savedBoard)
            assertEquals(boardName, savedBoard?.name)
            assertEquals(insertedId, savedBoard?.id)
        }
    }

    @Test
    fun shouldUpdateAndGet_a_Board() {
        val boardName = "Market Board"
        val newBoardName = "Super-Market Board"

        val board = Board(name = boardName)
        val boardsDao = database.boardsDao()
        val id = boardsDao.insertBoard(board)
        assertNotNull(id)

        getValue(boardsDao.getBoard(id = id)).let { retrievedBoard ->
            assertNotNull(retrievedBoard)
            retrievedBoard?.name = newBoardName
            boardsDao.updateBoard(retrievedBoard!!)

            val updatedBoard = getValue(boardsDao.getBoard(id = retrievedBoard.id))
            assertNotNull(updatedBoard)
            assertEquals(newBoardName, updatedBoard?.name)
        }
    }

    @Test
    fun shouldInsertAndDelete_a_Board() {
        val boardName = "Weekly Goals"

        val board = Board(name = boardName)
        val boardsDao = database.boardsDao()
        val id = boardsDao.insertBoard(board)
        assertNotNull(id)
        val insertedBoard = getValue(boardsDao.getBoard(id))

        boardsDao.deleteBoard(insertedBoard!!)

        val deletedBoard = getValue(boardsDao.getBoard(id))
        assertNull(deletedBoard)
    }

    @Test
    fun shouldDeleteBoard_CascadingDelete() {
        val board = Board(name = "Weekly Goals")
        val boardsDao = database.boardsDao()
        val id = boardsDao.insertBoard(board)

        val insertedBoard = getValue(boardsDao.getBoard(id))!!

        assertNotNull(insertedBoard)

        val columnsDao = database.columnsDao()

        val column = Column(boardId = insertedBoard.id, title = "TODO", type = ColumnType.TODO_COLUMN)
        columnsDao.insertColumn(column)

        // should cascade delete
        boardsDao.deleteBoard(insertedBoard)
        val deletedBoard = getValue(boardsDao.getBoard(insertedBoard.id))

        assertNull(deletedBoard)
    }


    @Test
    fun shouldGet_Three_Boards() {
        val boardsDao = database.boardsDao()
        val nameA = "Dish-washing Kanban"
        val nameC = "Weekly Goals"
        val nameB = "Task Board"

        val ids = boardsDao.insertBoards(Board(name = nameA), Board(name = nameB), Board(name = nameC))
        assertNotNull(ids)
        assertEquals(3, ids.size)

        val boards = getValue(boardsDao.getAllBoards())
        assertEquals(3, boards.size)
        assertEquals(nameA, boards[0].name)
        assertEquals(nameB, boards[1].name)
        assertEquals(nameC, boards[2].name)
    }

    @Test
    fun shouldGet_Two_Boards_FilteredByName() {
        val boardsDao = database.boardsDao()
        val nameA = "Daily Board"
        val nameB = "Weekly Board"
        val nameC = "Dish-washing Kanban"

        boardsDao.insertBoards(
            Board(name = nameA),
            Board(name = nameB),
            Board(name = nameC)
        ).let {
            assertNotNull(it)
            assertEquals(3, it.size)

            val boards = getValue(boardsDao.getAllBoards("Board"))
            assertEquals(2, boards.size)
            assertEquals(nameA, boards[0].name)
            assertEquals(nameB, boards[1].name)
        }
    }
}

