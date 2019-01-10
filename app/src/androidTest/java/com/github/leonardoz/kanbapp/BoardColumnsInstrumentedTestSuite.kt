package com.github.leonardoz.kanbapp

import com.github.leonardoz.kanbapp.view.dialog.ChangeColumnDialogTest
import com.github.leonardoz.kanbapp.view.dialog.ChangeNameDialogTest
import com.github.leonardoz.kanbapp.view.dialog.CreateColumnDialogTest
import com.github.leonardoz.kanbapp.view.fragment.BoardFragmentTest
import com.github.leonardoz.kanbapp.view.fragment.BoardsListFragmentTest
import com.github.leonardoz.kanbapp.view.fragment.CreateBoardFragmentTest
import com.github.leonardoz.kanbapp.view.fragment.ManageColumnsFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BoardFragmentTest::class,
    CreateColumnDialogTest::class,
    ManageColumnsFragmentTest::class,
    ChangeColumnDialogTest::class
)
class BoardInstrumentedTest