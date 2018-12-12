package com.github.leonardoz.kanbapp.view.form

data class CreateBoard(var name: String = "")

data class CreateBoardError(
    var nameIsWrong: Boolean = true,
    var nameError: String = "",
    var valid: Boolean = false
)

data class ChangeBoardName(
    var name: String = ""
)

data class ChangeBoardNameError(
    var nameIsWrong: Boolean = true,
    var nameError: String = "",
    var valid: Boolean = false
)
