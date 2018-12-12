package com.github.leonardoz.kanbapp.util

fun format(message: String, param: Int) =
    message.replace("%s", param.toString(), false)

fun format(message: String, param: String) =
    message.replace("%s", param, false)


fun formatNullable(message: String?, param: Int) =
    message?.replace("%s", param.toString(), false) ?: ""

