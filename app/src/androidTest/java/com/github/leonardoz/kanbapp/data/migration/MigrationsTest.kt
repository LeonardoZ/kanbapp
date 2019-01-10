package com.github.leonardoz.kanbapp.data.migration

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.github.leonardoz.kanbapp.data.KanbappDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class MigrationsTest {

    @get:Rule
    val testHelper: MigrationTestHelper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            KanbappDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
        )
    private val database = "kanbapp-database"

    @Test
    fun shouldMigrate1_to_2_AddingColumnsTable() {
        val db1: SupportSQLiteDatabase = testHelper.createDatabase(database, 1)
        db1.close()

        testHelper.runMigrationsAndValidate(
            database, 2, true,
            MIGRATION_1_TO_2
        )

        testHelper.closeWhenFinished(db1)
    }

    @Test
    fun shouldMigrate2_to_3_AddingColumnsTable() {
        val db1: SupportSQLiteDatabase = testHelper.createDatabase(database, 2)
        db1.close()

        testHelper.runMigrationsAndValidate(
            database, 3, true,
            MIGRATION_2_TO_3
        )

        testHelper.closeWhenFinished(db1)
    }

    @Test
    fun shouldMigrate3_to_4_AddingColumnsTable() {
        val db1: SupportSQLiteDatabase = testHelper.createDatabase(database, 3)
        db1.close()

        testHelper.runMigrationsAndValidate(
            database, 4, true,
            MIGRATION_3_TO_4
        )

        testHelper.closeWhenFinished(db1)
    }
}