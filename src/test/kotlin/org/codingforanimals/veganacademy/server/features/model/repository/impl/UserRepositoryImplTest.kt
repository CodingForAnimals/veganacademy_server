package org.codingforanimals.veganacademy.server.features.model.repository.impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.codingforanimals.veganacademy.server.database.DatabaseFactory
import org.codingforanimals.veganacademy.server.database.DatabaseFactoryForUnitTest
import org.codingforanimals.veganacademy.server.features.model.data.dao.User
import org.codingforanimals.veganacademy.server.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.server.features.model.repository.UserRepository
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
    private lateinit var repository: UserRepository

    private val email = "email"
    private val passwordHash = "password"
    private val displayName = "displayName"

    @Before
    fun setup() {
        database = DatabaseFactoryForUnitTest()
        database.connect()
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun `given user doesn't exist, when add user, then return user`() = runTest {
        val mockUser = transaction {
            User.new {
                email = "email"
                displayName = "displayName"
                passwordHash = "passwordHash"
            }
        }
        every { source.findUserByEmail(email) } returns null
        every { source.createUser(email, displayName, passwordHash) } returns mockUser
        repository = UserRepositoryImpl(source, userUtils)

        val user = repository.register(email, displayName, passwordHash)

        verify { source.findUserByEmail(email); source.createUser(email, displayName, passwordHash) }
        assertEquals(mockUser.id.value, user?.userId)
        assertEquals(mockUser.email, user?.email)
        assertEquals(mockUser.displayName, user?.displayName)
    }

    @Test
    fun `given user already exists, when add user, then return null`() = runTest {
        val mockUser = transaction {
            User.new {
                email = "email"
                displayName = "displayName"
                passwordHash = "passwordHash"
            }
        }
        every { source.findUserByEmail(email) } returns mockUser
        repository = UserRepositoryImpl(source, userUtils)

        val user = repository.register(email, displayName, passwordHash)

        verify { source.findUserByEmail(email) }
        verify(exactly = 0) { source.createUser(any(), any(), any()) }
        assertNull(user)
    }
}