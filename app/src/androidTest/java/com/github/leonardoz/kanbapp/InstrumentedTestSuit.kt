package com.github.leonardoz.kanbapp

import org.junit.runner.RunWith
import org.junit.runners.Suite

// Runs all unit tests.
@RunWith(Suite::class)
@Suite.SuiteClasses(BasicInstrumentedTest::class, BoardsActivity::class)
class UnitTestSuite