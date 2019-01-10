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

data class CreateColumn(var title: String = "")

data class CreateColumnError(
    var titleIsWrong: Boolean = true,
    var titleError: String = "",
    var valid: Boolean = false
)

data class ChangeColumn(var title: String = "")

data class ChangeColumnError(
    var titleIsWrong: Boolean = true,
    var titleError: String = "",
    var valid: Boolean = false
)

