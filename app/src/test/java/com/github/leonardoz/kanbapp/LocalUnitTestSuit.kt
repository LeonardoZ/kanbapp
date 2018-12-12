package com.github.leonardoz.kanbapp

import com.github.leonardoz.kanbapp.data.repository.BoardsRepositoryTest
import com.github.leonardoz.kanbapp.view.form.ValidationTest
import com.github.leonardoz.kanbapp.view.viewmodel.BoardsViewModel
import com.github.leonardoz.kanbapp.view.viewmodel.BoardsViewModelTest
import com.github.leonardoz.kanbapp.view.viewmodel.ChangeBoardViewModelTest
import com.github.leonardoz.kanbapp.view.viewmodel.CreateBoardViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BasicUnitTest::class,
    BoardsRepositoryTest::class,
    BoardsViewModelTest::class,
    ValidationTest::class,
    CreateBoardViewModelTest::class,
    ChangeBoardViewModelTest::class
)
class LocalUnitTestSuit