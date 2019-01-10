package com.github.leonardoz.kanbapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.util.FakeAsyncTask
import com.github.leonardoz.kanbapp.util.getValue
import junit.framework.TestCase.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class BoardsRepositoryTest() {
    private val boardsDao = mock(BoardsDao::class.java)
    private val repository = BoardsRepository(boardsDao, FakeAsyncTask())

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shouldCall_getAllBoard_InDao() {
        repository.getAllBoards()
        verify(boardsDao).getAllBoards()
    }

    @Test
    fun shouldCall_getBoard_InDao() {
        repository.getBoard(56)
        verify(boardsDao).getBoard(56)
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
        toBeUpdated.name = "School Homework"

        assertTrue(toBeUpdated.createdAt == toBeUpdated.updatedAt)

        repository.updateBoard(toBeUpdated)
        verify(boardsDao).updateBoard(toBeUpdated)
        assertTrue(toBeUpdated.createdAt.before(toBeUpdated.updatedAt))
    }

    @Test
    fun shouldTest_ifDeleteIsCalled_Correctly() {
        val toBeDeleted = Board(id = 12, name = "Kitchen shopping")
        repository.deleteBoard(toBeDeleted)
        verify(boardsDao).deleteBoard(toBeDeleted)
    }

}