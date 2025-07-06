package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.user.UserInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AuthRepositoryImplTest {
    @Test
    fun `should sign in anonymously`() {
        val mockAuth = MockAuth()
        val mockSupabaseClient = MockSupabaseClient(mockAuth)
        val repository: AuthRepository = AuthRepositoryImpl(mockSupabaseClient)

        kotlinx.coroutines.test.runTest {
            repository.signInAnonymous()
        }

        assertEquals(1, mockAuth.signInAnonymouslyCallCount)
    }

    @Test
    fun `should return user when user is signed in`() {
        val mockUser = MockUserInfo("user-123")
        val mockAuth = MockAuth(currentUser = mockUser)
        val mockSupabaseClient = MockSupabaseClient(mockAuth)
        val repository: AuthRepository = AuthRepositoryImpl(mockSupabaseClient)

        val result =
            kotlinx.coroutines.test.runTest {
                repository.getUser()
            }

        assertEquals(mockUser, result)
        assertEquals(1, mockAuth.awaitInitializationCallCount)
    }

    @Test
    fun `should return null when no user is signed in`() {
        val mockAuth = MockAuth(currentUser = null)
        val mockSupabaseClient = MockSupabaseClient(mockAuth)
        val repository: AuthRepository = AuthRepositoryImpl(mockSupabaseClient)

        val result =
            kotlinx.coroutines.test.runTest {
                repository.getUser()
            }

        assertNull(result)
        assertEquals(1, mockAuth.awaitInitializationCallCount)
    }

    @Test
    fun `should return user ID when user is signed in`() {
        val mockUser = MockUserInfo("user-123")
        val mockAuth = MockAuth(currentUser = mockUser)
        val mockSupabaseClient = MockSupabaseClient(mockAuth)
        val repository: AuthRepository = AuthRepositoryImpl(mockSupabaseClient)

        val result =
            kotlinx.coroutines.test.runTest {
                repository.getUserId()
            }

        assertEquals("user-123", result)
    }

    @Test
    fun `should throw exception when getting user ID and no user is signed in`() {
        val mockAuth = MockAuth(currentUser = null)
        val mockSupabaseClient = MockSupabaseClient(mockAuth)
        val repository: AuthRepository = AuthRepositoryImpl(mockSupabaseClient)

        assertFailsWith<IllegalStateException> {
            kotlinx.coroutines.test.runTest {
                repository.getUserId()
            }
        }
    }

    @Test
    fun `should throw exception with correct message when getting user ID and no user is signed in`() {
        val mockAuth = MockAuth(currentUser = null)
        val mockSupabaseClient = MockSupabaseClient(mockAuth)
        val repository: AuthRepository = AuthRepositoryImpl(mockSupabaseClient)

        val exception =
            assertFailsWith<IllegalStateException> {
                kotlinx.coroutines.test.runTest {
                    repository.getUserId()
                }
            }

        assertEquals("User is not signed in", exception.message)
    }

    private class MockSupabaseClient(
        private val mockAuth: Auth,
    ) : SupabaseClient {
        override val auth: Auth = mockAuth

        // We only need to implement the auth property for our tests
        override fun close() = Unit

        override val supabaseUrl: String = "https://mock.supabase.co"
        override val supabaseKey: String = "mock-key"
    }

    private class MockAuth(
        private val currentUser: UserInfo? = null,
    ) : Auth {
        var signInAnonymouslyCallCount = 0
            private set
        var awaitInitializationCallCount = 0
            private set

        override suspend fun signInAnonymously() {
            signInAnonymouslyCallCount++
        }

        override suspend fun awaitInitialization() {
            awaitInitializationCallCount++
        }

        override fun currentUserOrNull(): UserInfo? = currentUser
    }

    private class MockUserInfo(
        override val id: String,
    ) : UserInfo {
        override val email: String? = null
        override val phone: String? = null
        override val createdAt: String = ""
        override val confirmedAt: String? = null
        override val emailConfirmedAt: String? = null
        override val phoneConfirmedAt: String? = null
        override val lastSignInAt: String? = null
        override val role: String? = null
        override val updatedAt: String? = null
        override val identities: List<Any> = emptyList()
        override val userMetadata: Map<String, Any?> = emptyMap()
        override val appMetadata: Map<String, Any?> = emptyMap()
        override val aud: String = ""
        override val recoveryToken: String? = null
        override val emailChangeToken: String? = null
        override val phoneChangeToken: String? = null
        override val factors: List<Any>? = null
    }
}
