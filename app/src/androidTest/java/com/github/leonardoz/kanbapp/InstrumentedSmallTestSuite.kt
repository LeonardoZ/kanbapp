package com.github.leonardoz.kanbapp

import com.github.leonardoz.kanbapp.data.dao.BoardsDaoTest
import com.github.leonardoz.kanbapp.data.dao.ColumnsDaoTest
import com.github.leonardoz.kanbapp.data.migration.MigrationsTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BasicInstrumentedTest::class,
    MigrationsTest::class,
    BoardsDaoTest::class,
    ColumnsDaoTest::class
)
class InstrumentedSmallTestSuite