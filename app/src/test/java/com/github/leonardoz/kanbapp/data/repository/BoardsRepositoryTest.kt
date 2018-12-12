package com.github.leonardoz.kanbapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.util.FakeAsyncTask
import com.github.leonardoz.kanbapp.util.getValue
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class BoardsRepositoryTest () {
    private val boardsDao = mock(BoardsDao::class.java)
    private val repository = BoardsRepository(boardsDao, FakeAsyncTask())

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shouldLoad_Three_Boards_FromAll_Boards() {
        val dbData = listOf(
            Board(name = "Daily tasks"),
            Board(name = "School homework"),
            Board(name = "Supermarket")
        )
        val liveData: MutableLiveData<List<Board>> = MutableLiveData()
        liveData.postValue(dbData)

        `when`(boardsDao.getAllBoards()).thenReturn(liveData)
        val boards = getValue(repository.getAllBoards())
        assertNotNull(boards)
        assertEquals(3, boards.size)
        assertEquals("School homework", boards[1].name)
    }

    @Test
    fun shouldLoad_Correct_Board() {
        val liveData: MutableLiveData<Board> = MutableLiveData()
        liveData.postValue(Board(id = 56, name = "School homework"))

        `when`(boardsDao.getBoard(56)).thenReturn(liveData)
        val board = getValue(repository.getBoard(56))
        assertNotNull(board)
        verify(boardsDao).getBoard(56)
        assertEquals("School homework", board?.name)
        assertEquals(56L, board?.id)
    }

    @Test
    fun shouldTest_ifInsertIsCalled_Correctly() {
        val toBeInserted = Board(name = "Kitchen shopping")
        repository.saveBoard(toBeInserted)
        verify(boardsDao).insertBoard(toBeInserted)
    }

    @Test
    fun shouldTest_ifUpdateIsCalled_Correctly() {
        val toBeUpdated = Board(id = 12, name = "Kitchen shopping")
        toBeUpdated?.name = "School Homework"
        repository.updateBoard(toBeUpdated)
        verify(boardsDao).updateBoard(toBeUpdated)
    }

    @Test
    fun shouldTest_ifDeleteIsCalled_Correctly() {
        val toBeDeleted = Board(id = 12, name = "Kitchen shopping")
        repository.deleteBoard(toBeDeleted)
        verify(boardsDao).deleteBoard(toBeDeleted)
    }

}