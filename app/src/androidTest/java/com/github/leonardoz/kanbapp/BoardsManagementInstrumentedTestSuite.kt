package com.github.leonardoz.kanbapp

import com.github.leonardoz.kanbapp.view.dialog.ChangeNameDialogTest
import com.github.leonardoz.kanbapp.view.fragment.BoardsListFragmentTest
import com.github.leonardoz.kanbapp.view.fragment.CreateBoardFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BasicInstrumentedTest::class,
    BoardsListFragmentTest::class,
    CreateBoardFragmentTest::class,
    ChangeNameDialogTest::class
)
class BoardsInstrumentedTest