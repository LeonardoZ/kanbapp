package com.github.leonardoz.kanbapp.util

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.AfterClass
import org.junit.BeforeClass

open class BaseUiTest {
    companion object {
        @JvmStatic
        @BeforeClass
        fun goToFragmentUnderTest() {
            InstrumentationRegistry.getInstrumentation().targetContext
                .deleteDatabase("kanbapp-database")
        }

        @JvmStatic
        @AfterClass
        fun destroyDb() {
            InstrumentationRegistry.getInstrumentation().targetContext
                .deleteDatabase("kanbapp-database")
        }
    }
}