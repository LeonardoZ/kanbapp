package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import com.github.leonardoz.kanbapp.util.getValue
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class BoardsViewModelTest {

    private val repository = mock(BoardsRepository::class.java)
    private val navigateToCreateBoard = mock(SingleLiveEvent::class.java)
            as SingleLiveEvent<Any>
    private val informBoardWasRemoved = mock(SingleLiveEvent::class.java)
            as SingleLiveEvent<Board>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun should_Goto_newFrag() {
        val viewModel = BoardsViewModel(repository)
        viewModel.navigateToCreateBoardFragment = navigateToCreateBoard
        viewModel.onNewBoardClicked()
        verify(navigateToCreateBoard).call()
    }

    @Test
    fun shouldReturn_TwoBoards() {
        val dbData = listOf(
            Board(name = "Daily tasks"),
            Board(name = "Supermarket")
        )
        val liveData: MutableLiveData<List<Board>> = MutableLiveData()
        liveData.postValue(dbData)

        `when`(repository.getAllBoards()).thenReturn(liveData)
        val viewModel = BoardsViewModel(repository)
        val boards = getValue(viewModel.boards)
        assertEquals(2, boards.size)
        assertEquals("Daily tasks", boards[0].name)
    }

    @Test
    fun shouldRemove_Board() {
        val board = Board(name = "Daily tasks")
        val viewModel = BoardsViewModel(repository)
        viewModel.informBoardWasRemoved = informBoardWasRemoved

        viewModel.removeBoard(board)
        verify(repository).deleteBoard(board)
        verify(informBoardWasRemoved).value = board
    }


}