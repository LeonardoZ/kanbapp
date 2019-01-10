package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnRestrictions
import com.github.leonardoz.kanbapp.data.entity.ColumnType
import com.github.leonardoz.kanbapp.data.entity.ColumnType.*
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import com.github.leonardoz.kanbapp.view.form.CreateColumn
import com.github.leonardoz.kanbapp.view.form.CreateColumnError
import com.github.leonardoz.kanbapp.view.form.FormValidatorFactory
import com.github.leonardoz.kanbapp.view.form.ValidationType
import javax.inject.Inject

class CreateColumnViewModel @Inject constructor(
    private val columnsRepository: ColumnsRepository,
    formValidatorFactory: FormValidatorFactory
) : ViewModel() {

    companion object {
        const val FIELD_TITLE = "FIELD_TITLE"
    }

    @VisibleForTesting
    private val formValidator = formValidatorFactory.validator()
    var afterColumnCreated: SingleLiveEvent<Any> = SingleLiveEvent()
    var cancelColumnCreation: SingleLiveEvent<Any> = SingleLiveEvent()

    val createColumn: MutableLiveData<CreateColumn> by lazy {
        MutableLiveData<CreateColumn>()
    }

    val createColumnError: MutableLiveData<CreateColumnError> by lazy {
        MutableLiveData<CreateColumnError>()
    }

    var boardId: Long = 0

    fun setToInitialState() {
        createColumn.postValue(CreateColumn())
        createColumnError.postValue(CreateColumnError())
        formValidator.clear(FIELD_TITLE)
    }

    fun cancelChange() {
        cancelColumnCreation.call()
    }

    fun saveColumn() {
        if (!createColumnError.value?.valid!!)
            return

        if (boardId == 0L)
            return

        createColumn.value?.let {
            val column = Column(title = it.title, type = WORK_COLUMN, boardId = boardId)
            columnsRepository.saveColumnChangingOrder(column)
            afterColumnCreated.call()
        }
    }


    fun onTitleChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        formValidator.clear(FIELD_TITLE)
        formValidator
            .validate(
                FIELD_TITLE,
                ValidationType.REQUIRED,
                input = s.toString()
            )
            .validate(
                FIELD_TITLE,
                ValidationType.MAX_LENGTH,
                input = s.toString(),
                limit = ColumnRestrictions.titleSizeLimit
            )
        val boardNameError = CreateColumnError(
            titleIsWrong = !formValidator.isValid(FIELD_TITLE),
            titleError = formValidator.errorsFoundCompacted(FIELD_TITLE),
            valid = formValidator.isValid()
        )
        createColumnError.postValue(boardNameError)
    }
}