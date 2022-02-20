package org.codingforanimals.veganacademy.server.features.model.repository.impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.codingforanimals.veganacademy.server.database.DatabaseFactory
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryForUnitTest
import org.codingforanimals.veganacademy.server.features.model.data.dao.User
import org.codingforanimals.veganacademy.server.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.server.utils.UserUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    private val source: UserSource = mockk()
    private val userUtils: UserUtils = mockk()

    private lateinit var database: DatabaseFactory

    private val email = "email"
    private val passwordHash = "password"
    private val displayName = "displayName"

    private val sut = UserRepositoryImpl(source, userUtils)

    @Before
    fun setup() {
        database = DatabaseFactoryForUnitTest()
        runBlocking {

        database.connect()
        }

        every { userUtils.hashPassword(any()) } returns passwordHash
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun `given user doesn't exist, when add user, then return user`() = runTest {
        val mockUser = createUser()
        every { source.findUserByEmail(any()) } returns null
        every { source.createUser(any(), any(), any()) } returns mockUser

        val user = sut.register(email, passwordHash, displayName)

        verify { source.findUserByEmail(email); source.createUser(email, passwordHash, displayName) }
        assertEquals(mockUser.id.value, user?.userId)
        assertEquals(mockUser.email, user?.email)
        assertEquals(mockUser.displayName, user?.displayName)
    }

    @Test
    fun `given user already exists, when add user, then return null`() = runTest {
        val mockUser = createUser()
        every { source.findUserByEmail(email) } returns mockUser

        val user = sut.register(email, displayName, passwordHash)

        verify { source.findUserByEmail(email) }
        verify(exactly = 0) { source.createUser(any(), any(), any()) }
        assertNull(user)
    }

    private fun createUser() = transaction {
        User.new {
            email = this@UserRepositoryImplTest.email
            displayName = this@UserRepositoryImplTest.displayName
            passwordHash = this@UserRepositoryImplTest.passwordHash
        }
    }
}