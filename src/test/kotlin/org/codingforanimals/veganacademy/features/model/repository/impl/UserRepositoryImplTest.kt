package org.codingforanimals.veganacademy.features.model.repository.impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.codingforanimals.veganacademy.database.DatabaseFactory
import org.codingforanimals.veganacademy.database.DatabaseFactoryForUnitTest
import org.codingforanimals.veganacademy.features.model.dao.User
import org.codingforanimals.veganacademy.features.model.data.source.UserSource
import org.codingforanimals.veganacademy.features.model.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class UserRepositoryImplTest {

    private val source: UserSource = mockk()

//    @get:Rule
//    val koinTestRule = KoinTestRule.create {
//        modules(module {
//            single<UserRepository> { UserRepositoryImpl(mockk()) }
//        })
//    }

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
    fun `given user doesn't exist, when add user, then return user`() = runBlockingTest {
        val mockUser =  transaction {
            User.new {
                email = "email"
                displayName = "displayName"
                passwordHash = "passwordHash"
            }
        }
        every { source.findUserByEmail(email) } returns null
        every { source.createUser(email, displayName, passwordHash) } returns mockUser
        repository = UserRepositoryImpl(source)

        val user = repository.addUser(email, displayName, passwordHash)

        verify { source.findUserByEmail(email); source.createUser(email, displayName, passwordHash) }
        assertEquals(mockUser.id.value, user?.id?.value)
        assertEquals(mockUser.email, user?.email)
        assertEquals(mockUser.displayName, user?.displayName)
        assertEquals(mockUser.passwordHash, user?.passwordHash)
    }

    @Test
    fun `given user already exists, when add user, then return null`() = runBlockingTest {
        val mockUser =  transaction {
            User.new {
                email = "email"
                displayName = "displayName"
                passwordHash = "passwordHash"
            }
        }
        every { source.findUserByEmail(email) } returns mockUser
        repository = UserRepositoryImpl(source)

        val user = repository.addUser(email, displayName, passwordHash)

        verify { source.findUserByEmail(email) }
        verify(exactly = 0) { source.createUser(any(), any(), any()) }
        assertNull(user)
    }
}