package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.BoardRestrictions
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.util.*
import com.github.leonardoz.kanbapp.view.form.CreateBoard
import com.github.leonardoz.kanbapp.view.form.CreateBoardError
import com.github.leonardoz.kanbapp.view.form.FormValidatorFactory
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class CreateBoardViewModelTest {

    private val repository = mock(BoardsRepository::class.java)
    private val context = mockContext()
    private val validationFactory = FormValidatorFactory(context)
    private val liveEvent = mock(SingleLiveEvent::class.java) as SingleLiveEvent<Any>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        mockMessages(context)
    }

    @Test
    fun should_InitiateCorrectly() {
        val viewModel = createViewModel()
        val createBoard = CreateBoard()
        val createBoardError = CreateBoardError()
        assertEquals(createBoard, viewModel.createBoard.value)
        assertEquals(createBoardError, viewModel.createBoardError.value)
    }

    @Test
    fun should_ReactToChangeText_And_ValidateErrors() {
        val viewModel = createViewModel()
        val newName = "A new hope starts with our dear and liked board!!!"

        // Has errors scenario
        viewModel.onNameChanged(newName, 0, 0, 0)
        val postedCreateBoardError = viewModel.createBoardError.value
        val expected = format(context.getString(R.string.max_length), BoardRestrictions.nameSizeLimit)
        assertEquals(expected, postedCreateBoardError?.nameError)
        assertEquals(true, postedCreateBoardError?.nameIsWrong)
        assertEquals(false, postedCreateBoardError?.valid)
    }

    @Test
    fun should_ReactToChangeText_And_Validate_WithoutErrors() {
        val viewModel = createViewModel()
        val newName = "A new hope"

        // Has no errors scenario
        viewModel.onNameChanged(newName, 0, 0, 0)
        val postedCreateBoardError = viewModel.createBoardError.value

        assertEquals("", postedCreateBoardError?.nameError)
        assertEquals(false, postedCreateBoardError?.nameIsWrong)
        assertEquals(true, postedCreateBoardError?.valid)
    }

    @Test
    fun should_CallInsert_NewBoard() {
        val viewModel = createViewModel()
        viewModel.onNameChanged("Daily tasks", 0, 0, 0)
        viewModel.createBoard.postValue(CreateBoard("Daily tasks"))
        doNothing().`when`(repository).saveBoard(safeAny())
        viewModel.saveNewBoard()

        verify(repository).saveBoard(safeAny())
        verify(liveEvent).call()
    }

    @Test
    fun should_NotTryTo_InsertNewBoard() {
        val viewModel = createViewModel()

        // Without post
        viewModel.saveNewBoard()

        verifyZeroInteractions(repository)
        verifyZeroInteractions(liveEvent)
    }


    private fun createViewModel(): CreateBoardViewModel {
        val viewModel = CreateBoardViewModel(repository, validationFactory)
        viewModel.navigateToBoardsFragment = liveEvent
        return viewModel
    }
}