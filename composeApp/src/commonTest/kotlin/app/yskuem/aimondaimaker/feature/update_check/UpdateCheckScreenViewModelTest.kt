package app.yskuem.aimondaimaker.feature.update_check

import MainDispatcherTestBase
import app.cash.turbine.test
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.util.OpenUrl
import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import app.yskuem.aimondaimaker.domain.status.CheckUpdateStatus
import app.yskuem.aimondaimaker.domain.usecase.CheckUpdateUseCase
import dev.mokkery.answering.returns
import dev.mokkery.answering.throwsErrorWith
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class UpdateCheckScreenViewModelTest : MainDispatcherTestBase() {
    private val checkUpdateUseCase: CheckUpdateUseCase = mock()
    private val openUrl: OpenUrl = mock()
    private val versionRepository: VersionRepository = mock()

    private lateinit var viewModel: UpdateCheckScreenViewModel

    @BeforeTest
    fun setup() {
        everySuspend { checkUpdateUseCase.checkUpdate() } returns CheckUpdateStatus.NONE
        everySuspend { versionRepository.fetchStoreUrl() } returns "https://example.com"
        every { openUrl.handle(any()) } returns Unit

        viewModel = UpdateCheckScreenViewModel(
            checkUpdateUseCase = checkUpdateUseCase,
            openUrl = openUrl,
            versionRepository = versionRepository,
        )
    }

    /**
     * アップデートチェックが成功した時のパターン
     */
    @Test
    fun check_check_update_on_success() = runTest {
        viewModel.updateState.test {
            assertTrue(expectMostRecentItem().isLoading)

            viewModel.checkUpdate()
            testScheduler.advanceUntilIdle()

            val result = awaitItem()
            assertTrue(result is DataUiState.Success<CheckUpdateStatus>)
            cancelAndIgnoreRemainingEvents()
        }

        verifySuspend(exactly(1)) { checkUpdateUseCase.checkUpdate() }
    }

    /**
     * アップデートチェックが失敗した時のパターン
     */
    @Test
    fun check_check_update_on_failed() = runTest {
        viewModel.updateState.test {
            assertTrue(expectMostRecentItem().isLoading)

            everySuspend { checkUpdateUseCase.checkUpdate() } throwsErrorWith "Failed!"

            viewModel.checkUpdate()
            testScheduler.advanceUntilIdle()

            val result = expectMostRecentItem()
            assertTrue(result is DataUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }

        verifySuspend(exactly(1)) { checkUpdateUseCase.checkUpdate() }
    }

    /**
     * ストアページを開くメソッドが呼ばれた時のパターン
     */
    @Test
    fun check_open_store_page() = runTest {
        val url = "https://example.com"
        everySuspend { versionRepository.fetchStoreUrl() } returns url

        viewModel.openStorePage()
        testScheduler.advanceUntilIdle()

        verifySuspend(exactly(1)) { versionRepository.fetchStoreUrl() }
        verify(exactly(1)) { openUrl.handle(url) }
    }
}

