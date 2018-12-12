package com.github.leonardoz.kanbapp

import com.github.leonardoz.kanbapp.data.dao.BoardsDaoTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(BasicInstrumentedTest::class, BoardsDaoTest::class)
class InstrumentedSmallTestSuit