package org.codingforanimals.veganacademy.server.features.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryForUnitTest
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.junit.After
import org.junit.Before
import org.koin.test.KoinTest

@ExperimentalCoroutinesApi
open class UnitKoinTest: KoinTest {

    private lateinit var databaseFactory: DatabaseFactoryForUnitTest

    @Before
    fun setup() {
        databaseFactory = DatabaseFactoryForUnitTest()
        databaseFactory.connect()
    }

    @After
    fun tearDown() {
        databaseFactory.close()
    }

    protected fun runTestWithTransaction(callback: suspend TestScope.(Transaction) -> Unit): TestResult =
        runTest { newSuspendedTransaction { callback(this) } }
}