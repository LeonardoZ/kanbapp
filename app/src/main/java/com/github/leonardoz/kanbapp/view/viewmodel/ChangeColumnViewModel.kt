package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.data.entity.ColumnRestrictions
import com.github.leonardoz.kanbapp.data.entity.ColumnType
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import com.github.leonardoz.kanbapp.view.form.ChangeColumn
import com.github.leonardoz.kanbapp.view.form.ChangeColumnError
import com.github.leonardoz.kanbapp.view.form.FormValidatorFactory
import com.github.leonardoz.kanbapp.view.form.ValidationType
import javax.inject.Inject

class ChangeColumnViewModel @Inject constructor(
    private val columnsRepository: ColumnsRepository,
    formValidatorFactory: FormValidatorFactory
) : ViewModel() {

    companion object {
        const val FIELD_TITLE = "FIELD_TITLE"
    }

    @VisibleForTesting
    private val formValidator = formValidatorFactory.validator()

    var afterColumnChanged: SingleLiveEvent<Any> = SingleLiveEvent()

    var cancelColumnChanged: SingleLiveEvent<Any> = SingleLiveEvent()

    val changeColumn: MutableLiveData<ChangeColumn> by lazy {
        MutableLiveData<ChangeColumn>()
    }

    val changeColumnError: MutableLiveData<ChangeColumnError> by lazy {
        MutableLiveData<ChangeColumnError>()
    }

    val boardAndColumnIds = MutableLiveData<Pair<Long, Long>>()

    val column: LiveData<Column?> = Transformations.switchMap(boardAndColumnIds) {
        columnsRepository.getColumn(it.first, it.second)
    }

    fun setToInitialState() {
        changeColumn.postValue(ChangeColumn())
        changeColumnError.postValue(ChangeColumnError())
        formValidator.clear(FIELD_TITLE)
    }

    fun cancelChange() {
        cancelColumnChanged.call()
    }

    fun changeColumn() {
        if (!changeColumnError.value?.valid!!)
            return

        val savedColumn = column.value
        val changeColumn = changeColumn.value

        requireNotNull(savedColumn)
        requireNotNull(changeColumn)

        val column = Column(
            id = savedColumn.id,
            title = changeColumn.title,
            type = ColumnType.WORK_COLUMN,
            boardId = savedColumn.id
        )
        columnsRepository.updateColumnTitle(column)
        afterColumnChanged.call()
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
        val boardNameError = ChangeColumnError(
            titleIsWrong = !formValidator.isValid(FIELD_TITLE),
            titleError = formValidator.errorsFoundCompacted(FIELD_TITLE),
            valid = formValidator.isValid()
        )
        changeColumnError.postValue(boardNameError)
    }

}