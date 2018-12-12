package com.github.leonardoz.kanbapp.view.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.BoardRestrictions
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import com.github.leonardoz.kanbapp.view.form.ChangeBoardName
import com.github.leonardoz.kanbapp.view.form.ChangeBoardNameError
import com.github.leonardoz.kanbapp.view.form.FormValidatorFactory
import com.github.leonardoz.kanbapp.view.form.ValidationType
import javax.inject.Inject


class ChangeNameViewModel @Inject constructor(
    private val boardsRepository: BoardsRepository,
    formValidatorFactory: FormValidatorFactory
) : ViewModel() {

    companion object {
        const val FIELD_CHANGE_NAME = "CHANGE_NAME"
    }

    @VisibleForTesting
    private val formValidator = formValidatorFactory.validator()
    var afterNameChange: SingleLiveEvent<Any> = SingleLiveEvent()
    var cancelNameChange: SingleLiveEvent<Any> = SingleLiveEvent()

    val changeBoardName: MutableLiveData<ChangeBoardName> by lazy {
        MutableLiveData<ChangeBoardName>()
    }

    val changeBoardNameError: MutableLiveData<ChangeBoardNameError> by lazy {
        MutableLiveData<ChangeBoardNameError>()
    }

    var boardIdToBeChanged: Long? = null


    fun setToInitialState() {
        changeBoardName.postValue(ChangeBoardName())
        changeBoardNameError.postValue(ChangeBoardNameError())
        formValidator.clear(FIELD_CHANGE_NAME)
    }

    fun cancelChange() {
        cancelNameChange.call()
    }


    fun updateBoardName() {
        if (!changeBoardNameError.value?.valid!!)
            return
        if (boardIdToBeChanged == null || boardIdToBeChanged === 0L)
            return

        changeBoardName.value?.let {
            val board = Board(id = boardIdToBeChanged!!, name = it.name)
            boardsRepository.updateBoard(board)
            afterNameChange.call()
        }
    }

    fun onNameChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        formValidator.clear(FIELD_CHANGE_NAME)
        formValidator
            .validate(
                FIELD_CHANGE_NAME,
                ValidationType.REQUIRED,
                input = s.toString()
            )
            .validate(
                FIELD_CHANGE_NAME,
                ValidationType.MAX_LENGTH,
                input = s.toString(),
                limit = BoardRestrictions.nameSizeLimit
            )
        val boardNameError = ChangeBoardNameError(
            nameIsWrong = !formValidator.isValid(FIELD_CHANGE_NAME),
            nameError = formValidator.errorsFoundCompacted(FIELD_CHANGE_NAME),
            valid = formValidator.isValid()
        )
        changeBoardNameError.postValue(boardNameError)
    }

}