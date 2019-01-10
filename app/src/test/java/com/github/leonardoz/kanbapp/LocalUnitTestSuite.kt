package com.github.leonardoz.kanbapp

import com.github.leonardoz.kanbapp.data.repository.BoardsRepositoryTest
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepository
import com.github.leonardoz.kanbapp.data.repository.ColumnsRepositoryTest
import com.github.leonardoz.kanbapp.view.form.ValidationTest
import com.github.leonardoz.kanbapp.view.viewmodel.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BasicUnitTest::class,
    BoardsRepositoryTest::class,
    ColumnsRepositoryTest::class,
    BoardsViewModelTest::class,
    ValidationTest::class,
    CreateBoardViewModelTest::class,
    ChangeBoardViewModelTest::class,
    BoardViewModelTest::class,
    CreateColumnViewModelTest::class,
    ChangeColumnViewModelTest::class,
    ColumnViewModelTest::class,
    ManageColumnsViewModelTest::class
)
class LocalUnitTestSuite