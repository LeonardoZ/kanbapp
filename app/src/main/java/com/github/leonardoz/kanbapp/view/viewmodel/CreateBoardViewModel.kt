package com.github.leonardoz.kanbapp.view.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.data.entity.BoardRestrictions
import com.github.leonardoz.kanbapp.data.repository.BoardsRepository
import com.github.leonardoz.kanbapp.util.SingleLiveEvent
import com.github.leonardoz.kanbapp.view.form.CreateBoard
import com.github.leonardoz.kanbapp.view.form.CreateBoardError
import com.github.leonardoz.kanbapp.view.form.FormValidatorFactory
import com.github.leonardoz.kanbapp.view.form.ValidationType
import javax.inject.Inject


class CreateBoardViewModel @Inject constructor(
    private val boardsRepository: BoardsRepository,
    formValidatorFactory: FormValidatorFactory
) : ViewModel() {

    companion object {
        const val FIELD_NAME = "NAME"
    }

    @VisibleForTesting
    private val formValidator = formValidatorFactory.validator()
    var navigateToBoardsFragment: SingleLiveEvent<Any> = SingleLiveEvent()

    val createBoard: MutableLiveData<CreateBoard> by lazy {
        MutableLiveData<CreateBoard>()
    }

    val createBoardError: MutableLiveData<CreateBoardError> by lazy {
        MutableLiveData<CreateBoardError>()
    }

    init {
        createBoard.postValue(CreateBoard())
        createBoardError.postValue(CreateBoardError())
    }

    fun saveNewBoard() {
        if (!createBoardError.value?.valid!!)
            return

        createBoard.value?.let {
            val board = Board(name = it.name)
            boardsRepository.saveBoard(board)
            navigateToBoardsFragment.call()
        }
    }

    fun onNameChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        formValidator.clear(FIELD_NAME)
        formValidator
            .validate(
                FIELD_NAME,
                ValidationType.REQUIRED,
                input = s.toString()
            )
            .validate(
                FIELD_NAME,
                ValidationType.MAX_LENGTH,
                input = s.toString(),
                limit = BoardRestrictions.nameSizeLimit
            )

        createBoardError.postValue(
            CreateBoardError(
                nameIsWrong = !formValidator.isValid(FIELD_NAME),
                nameError = formValidator.errorsFoundCompacted(FIELD_NAME),
                valid = formValidator.isValid()
            )
        )
    }

}