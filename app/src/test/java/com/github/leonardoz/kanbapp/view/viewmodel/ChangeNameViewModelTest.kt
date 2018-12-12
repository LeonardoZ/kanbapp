package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.BoardRestrictions
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.util.*
import com.github.leonardoz.kanbapp.view.form.*
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class ChangeBoardViewModelTest {

    private val repository = mock(BoardsRepository::class.java)
    private val context = mockContext()
    private val validationFactory = FormValidatorFactory(context)
    private val afterNameChange = mock(SingleLiveEvent::class.java) as SingleLiveEvent<Any>
    private val cancelNameChange = mock(SingleLiveEvent::class.java) as SingleLiveEvent<Any>
    private val fakeId = 10L

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        mockMessages(context)
    }

    @Test
    fun should_InitiateCorrectly() {
        val viewModel = createViewModel()
        viewModel.setToInitialState()
        val changeName = ChangeBoardName()
        val changeNameError = ChangeBoardNameError()
        assertEquals(changeName, viewModel.changeBoardName.value)
        assertEquals(changeNameError, viewModel.changeBoardNameError.value)
        assertEquals(fakeId, viewModel.boardIdToBeChanged)
    }

    @Test
    fun should_ReactToChangeText_And_ValidateErrors() {
        val viewModel = createViewModel()
        val newName = "A new hope starts with our dear and liked board!!!"

        // Has errors scenario
        viewModel.onNameChanged(newName, 0, 0, 0)
        val postedChangeNameError = viewModel.changeBoardNameError.value
        val expected = format(context.getString(R.string.max_length), BoardRestrictions.nameSizeLimit)
        assertEquals(expected, postedChangeNameError?.nameError)
        assertEquals(true, postedChangeNameError?.nameIsWrong)
        assertEquals(false, postedChangeNameError?.valid)
    }

    @Test
    fun should_ReactToChangeText_And_Validate_WithoutErrors() {
        val viewModel = createViewModel()
        val newName = "A new hope"

        // Has no errors scenario
        viewModel.onNameChanged(newName, 0, 0, 0)
        val postedChangeNameError = viewModel.changeBoardNameError.value

        assertEquals("", postedChangeNameError?.nameError)
        assertEquals(false, postedChangeNameError?.nameIsWrong)
        assertEquals(true, postedChangeNameError?.valid)
    }

    @Test
    fun should_CallUpdate_BoardName() {
        val viewModel = createViewModel()
        viewModel.onNameChanged("Daily tasks", 0, 0, 0)
        viewModel.changeBoardName.postValue(ChangeBoardName("Daily tasks"))

        doNothing().`when`(repository).updateBoard(safeAny())

        viewModel.updateBoardName()

        verify(repository).updateBoard(safeAny())
        verify(afterNameChange).call()
    }

    @Test
    fun should_NotTryTo_ChangeBoardName() {
        val viewModel = createViewModel()
        viewModel.setToInitialState()
        // Without post
        viewModel.updateBoardName()

        verifyZeroInteractions(repository)
        verifyZeroInteractions(afterNameChange)
    }

    @Test
    fun should_NotTryTo_ChangeBoardName_WithoutId() {
        val viewModel = createViewModel()
        viewModel.onNameChanged("Daily tasks", 0, 0, 0)
        viewModel.changeBoardName.postValue(ChangeBoardName("Daily tasks"))

        viewModel.boardIdToBeChanged = null

        // Without id
        viewModel.updateBoardName()

        verifyZeroInteractions(repository)
        verifyZeroInteractions(afterNameChange)
    }


    @Test
    fun should_Cancel() {
        val viewModel = createViewModel()

        viewModel.cancelChange()

        verify(cancelNameChange).call()
    }

    private fun createViewModel(): ChangeNameViewModel {
        val viewModel = ChangeNameViewModel(repository, validationFactory)
        viewModel.boardIdToBeChanged = fakeId
        viewModel.afterNameChange = afterNameChange
        viewModel.cancelNameChange = cancelNameChange
        return viewModel
    }
}