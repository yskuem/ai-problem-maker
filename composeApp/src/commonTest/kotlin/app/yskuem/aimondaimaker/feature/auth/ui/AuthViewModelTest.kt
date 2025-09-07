package app.yskuem.aimondaimaker.feature.auth.ui

import MainDispatcherTestBase
import app.cash.turbine.test
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.UserRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throwsErrorWith
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class AuthViewModelTest : MainDispatcherTestBase() {
    private val authRepository: AuthRepository = mock()
    private val userRepository: UserRepository = mock()

    private lateinit var viewModel: AuthScreenViewModel

    @BeforeTest
    fun setup() {
        everySuspend { authRepository.signInAnonymous() } returns Unit
        everySuspend { userRepository.saveUser() } returns Unit
        everySuspend { authRepository.getUserId() } returns ""
        viewModel =
            AuthScreenViewModel(
                authRepository = authRepository,
                userRepository = userRepository,
            )
    }

    /**
     * 初回ログインで成功した時のパターン
     */
    @Test
    fun check_first_login_on_success() =
        runTest {
            everySuspend { authRepository.getUser() } returns null
            viewModel.login()
            testScheduler.advanceUntilIdle()

            // isLoginSuccessがtrueかどうか
            viewModel.isLoginSuccess.test {
                val result = expectMostRecentItem()
                assertTrue(result)
                cancelAndIgnoreRemainingEvents()
            }

            // 以下のメソッドが一回ずつ呼ばれているかどうか
            verifySuspend(exactly(1)) {
                authRepository.getUser()
                authRepository.signInAnonymous()
                userRepository.saveUser()
            }
        }

    /**
     * 2回目移行のログインで成功した時のパターン
     */
    @Test
    fun check_re_login_on_success() =
        runTest {
            everySuspend { authRepository.getUser() } returns
                UserInfo(
                    id = "",
                    aud = "",
                )
            viewModel.login()
            testScheduler.advanceUntilIdle()

            // isLoginSuccessがtrueかどうか
            viewModel.isLoginSuccess.test {
                val result = expectMostRecentItem()
                assertTrue(result)
                cancelAndIgnoreRemainingEvents()
            }

            // 以下のメソッドが一回ずつ呼ばれているかどうか
            verifySuspend(exactly(1)) {
                authRepository.getUser()
            }

            // 以下のメソッドが呼ばれていないかどうか
            verifySuspend(exactly(0)) {
                authRepository.signInAnonymous()
                userRepository.saveUser()
            }
        }

    /**
     * ログインで失敗した時のパターン
     */
    @Test
    fun check_login_on_failed() =
        runTest {
            everySuspend { authRepository.getUser() } throwsErrorWith "Failed!"
            viewModel.login()
            testScheduler.advanceUntilIdle()

            // isLoginSuccessがfalseかどうか
            viewModel.isLoginSuccess.test {
                val result = expectMostRecentItem()
                assertFalse(result)
                cancelAndIgnoreRemainingEvents()
            }

            // 以下のメソッドが一回ずつ呼ばれているかどうか
            verifySuspend(exactly(1)) {
                authRepository.getUser()
            }

            // 以下のメソッドが呼ばれていないかどうか
            verifySuspend(exactly(0)) {
                authRepository.signInAnonymous()
                userRepository.saveUser()
            }
        }
}
