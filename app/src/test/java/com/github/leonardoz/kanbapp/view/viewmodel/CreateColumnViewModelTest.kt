package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.BoardRestrictions
import com.github.leonardoz.kanbapp.data.entity.ColumnRestrictions
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
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
class CreateColumnViewModelTest {

    private val repository = mock(ColumnsRepository::class.java)
    private val context = mockContext()
    private val validationFactory = FormValidatorFactory(context)
    private val afterColumnCreated =
        mock(SingleLiveEvent::class.java) as SingleLiveEvent<Any>
    private val cancelColumnCreation =
        mock(SingleLiveEvent::class.java) as SingleLiveEvent<Any>
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
        val createColumn = CreateColumn()
        val createColumnError = CreateColumnError()
        assertEquals(createColumn, viewModel.createColumn.value)
        assertEquals(createColumnError, viewModel.createColumnError.value)
        assertEquals(fakeId, viewModel.boardId)
    }

    @Test
    fun should_ReactToChangeText_And_ValidateErrors() {
        val viewModel = createViewModel()
        val newTitle = "Doing a long and irregular text-length phase"

        // Has errors scenario
        viewModel.onTitleChanged(newTitle, 0, 0, 0)
        val postedChangeTitleError = viewModel.createColumnError.value
        val expected = format(
            context.getString(R.string.max_length),
            ColumnRestrictions.titleSizeLimit
        )
        assertEquals(expected, postedChangeTitleError?.titleError)
        assertEquals(true, postedChangeTitleError?.titleIsWrong)
        assertEquals(false, postedChangeTitleError?.valid)
    }

    @Test
    fun should_ReactToChangeText_And_Validate_WithoutErrors() {
        val viewModel = createViewModel()
        val newTitle = "Doing"

        // Has no errors scenario
        viewModel.onTitleChanged(newTitle, 0, 0, 0)
        val postedChangeTitleError = viewModel.createColumnError.value

        assertEquals("", postedChangeTitleError?.titleError)
        assertEquals(false, postedChangeTitleError?.titleIsWrong)
        assertEquals(true, postedChangeTitleError?.valid)
    }

    @Test
    fun should_CallSave_Column() {
        val viewModel = createViewModel()
        viewModel.onTitleChanged("Doing", 0, 0, 0)
        viewModel.createColumn.postValue(CreateColumn("Doing"))

        doNothing().`when`(repository).saveColumnChangingOrder(safeAny())

        viewModel.saveColumn()

        verify(repository).saveColumnChangingOrder(safeAny())
        verify(afterColumnCreated).call()
    }

    @Test
    fun should_NotTryTo_CreateColumn() {
        val viewModel = createViewModel()
        viewModel.setToInitialState()
        // Without post
        viewModel.saveColumn()

        verifyZeroInteractions(repository)
        verifyZeroInteractions(afterColumnCreated)
    }

    @Test
    fun should_NotTryTo_CreateColumn_WithoutId() {
        val viewModel = createViewModel()
        viewModel.onTitleChanged("Doing", 0, 0, 0)
        viewModel.createColumn.postValue(CreateColumn("Doing"))

        viewModel.boardId = 0

        // Without id
        viewModel.saveColumn()

        verifyZeroInteractions(repository)
        verifyZeroInteractions(afterColumnCreated)
    }


    @Test
    fun should_Cancel() {
        val viewModel = createViewModel()

        viewModel.cancelChange()

        verify(cancelColumnCreation).call()
    }

    private fun createViewModel(): CreateColumnViewModel {
        val viewModel = CreateColumnViewModel(repository, validationFactory)
        viewModel.boardId = fakeId
        viewModel.afterColumnCreated = afterColumnCreated
        viewModel.cancelColumnCreation = cancelColumnCreation
        return viewModel
    }
}