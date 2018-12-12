package com.github.leonardoz.kanbapp

import com.github.leonardoz.kanbapp.view.fragment.BoardsListFragmentTest
import com.github.leonardoz.kanbapp.view.fragment.ChangeNameDialogTest
import com.github.leonardoz.kanbapp.view.fragment.CreateBoardFragment
import com.github.leonardoz.kanbapp.view.fragment.CreateBoardFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BoardsListFragmentTest::class,
    ChangeNameDialogTest::class,
    CreateBoardFragmentTest::class
)
class InstrumentedLargeTestSuit