package app.yskuem.aimondaimaker.feature.auth.ui

import app.yskuem.aimondaimaker.data.local_db.UserDataStore
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.UserRepository
import app.yskuem.aimondaimaker.domain.status.CheckUpdateStatus
import app.yskuem.aimondaimaker.domain.usecase.CheckUpdateUseCase
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthScreenViewModelTest {

    @Test
    fun `should initialize with default state`() {
        val mockAuthRepository = MockAuthRepository()
        val mockUserRepository = MockUserRepository()
        val mockUserDataStore = MockUserDataStore()
        val mockCheckUpdateUseCase = MockCheckUpdateUseCase()
        
        val viewModel = AuthScreenViewModel(
            authRepository = mockAuthRepository,
            userRepository = mockUserRepository,
            userDataStore = mockUserDataStore,
            checkUpdateUseCase = mockCheckUpdateUseCase
        )

        runTest {
            assertFalse(viewModel.hasError.first())
            assertFalse(viewModel.isLoginSuccess.first())
        }
    }

    @Test
    fun `should login successfully when user is not signed in`() {
        val mockAuthRepository = MockAuthRepository(currentUser = null)
        val mockUserRepository = MockUserRepository()
        val mockUserDataStore = MockUserDataStore()
        val mockCheckUpdateUseCase = MockCheckUpdateUseCase()
        
        val viewModel = AuthScreenViewModel(
            authRepository = mockAuthRepository,
            userRepository = mockUserRepository,
            userDataStore = mockUserDataStore,
            checkUpdateUseCase = mockCheckUpdateUseCase
        )

        runTest {
            viewModel.login()
            
            assertTrue(viewModel.isLoginSuccess.first())
            assertFalse(viewModel.hasError.first())
            assertEquals(1, mockAuthRepository.signInAnonymousCallCount)
            assertEquals(1, mockUserRepository.saveUserCallCount)
        }
    }

    @Test
    fun `should login successfully when user is already signed in`() {
        val mockUser = MockUserInfo("user-123")
        val mockAuthRepository = MockAuthRepository(currentUser = mockUser)
        val mockUserRepository = MockUserRepository()
        val mockUserDataStore = MockUserDataStore()
        val mockCheckUpdateUseCase = MockCheckUpdateUseCase()
        
        val viewModel = AuthScreenViewModel(
            authRepository = mockAuthRepository,
            userRepository = mockUserRepository,
            userDataStore = mockUserDataStore,
            checkUpdateUseCase = mockCheckUpdateUseCase
        )

        runTest {
            viewModel.login()
            
            assertTrue(viewModel.isLoginSuccess.first())
            assertFalse(viewModel.hasError.first())
            assertEquals(0, mockAuthRepository.signInAnonymousCallCount)
            assertEquals(0, mockUserRepository.saveUserCallCount)
        }
    }

    @Test
    fun `should handle login error when sign in fails`() {
        val mockAuthRepository = MockAuthRepository(shouldThrowOnSignIn = true)
        val mockUserRepository = MockUserRepository()
        val mockUserDataStore = MockUserDataStore()
        val mockCheckUpdateUseCase = MockCheckUpdateUseCase()
        
        val viewModel = AuthScreenViewModel(
            authRepository = mockAuthRepository,
            userRepository = mockUserRepository,
            userDataStore = mockUserDataStore,
            checkUpdateUseCase = mockCheckUpdateUseCase
        )

        runTest {
            viewModel.login()
            
            assertTrue(viewModel.hasError.first())
            assertFalse(viewModel.isLoginSuccess.first())
        }
    }

    @Test
    fun `should handle login error when save user fails`() {
        val mockAuthRepository = MockAuthRepository(currentUser = null)
        val mockUserRepository = MockUserRepository(shouldThrowOnSave = true)
        val mockUserDataStore = MockUserDataStore()
        val mockCheckUpdateUseCase = MockCheckUpdateUseCase()
        
        val viewModel = AuthScreenViewModel(
            authRepository = mockAuthRepository,
            userRepository = mockUserRepository,
            userDataStore = mockUserDataStore,
            checkUpdateUseCase = mockCheckUpdateUseCase
        )

        runTest {
            viewModel.login()
            
            assertTrue(viewModel.hasError.first())
            assertFalse(viewModel.isLoginSuccess.first())
        }
    }

    private class MockAuthRepository(
        private val currentUser: UserInfo? = null,
        private val shouldThrowOnSignIn: Boolean = false
    ) : AuthRepository {
        var signInAnonymousCallCount = 0
            private set

        override suspend fun signInAnonymous() {
            signInAnonymousCallCount++
            if (shouldThrowOnSignIn) {
                throw Exception("Sign in failed")
            }
        }

        override suspend fun getUser(): UserInfo? {
            return currentUser
        }

        override suspend fun getUserId(): String {
            return currentUser?.id ?: "user-123"
        }
    }

    private class MockUserRepository(
        private val shouldThrowOnSave: Boolean = false
    ) : UserRepository {
        var saveUserCallCount = 0
            private set

        override suspend fun saveUser() {
            saveUserCallCount++
            if (shouldThrowOnSave) {
                throw Exception("Save user failed")
            }
        }
    }

    @OptIn(ExperimentalSettingsApi::class)
    private class MockUserDataStore : UserDataStore() {
        override val flowSettings: FlowSettings = object : FlowSettings {
            override fun getStringFlow(key: String, defaultValue: String): Flow<String> {
                return MutableStateFlow(defaultValue)
            }

            override suspend fun putString(key: String, value: String) {
                // Mock implementation
            }
        }
    }

    private class MockCheckUpdateUseCase : CheckUpdateUseCase {
        override suspend fun checkUpdate(): CheckUpdateStatus {
            return CheckUpdateStatus.NONE
        }
    }

    private class MockUserInfo(override val id: String) : UserInfo {
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