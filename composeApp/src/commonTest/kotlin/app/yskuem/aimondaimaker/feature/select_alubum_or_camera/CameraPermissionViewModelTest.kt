package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import MainDispatcherTestBase
import app.cash.turbine.test
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.state.UiPermissionState
import dev.mokkery.answering.answers
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CameraPermissionViewModelTest : MainDispatcherTestBase() {
    private val permissionsController: PermissionsController = mock()

    private lateinit var viewModel: CameraPermissionViewModel

    @BeforeTest
    fun setup() {
        everySuspend { permissionsController.providePermission(Permission.CAMERA) } returns Unit
        every { permissionsController.openAppSettings() } returns Unit
        everySuspend { permissionsController.isPermissionGranted(Permission.CAMERA) } returns true

        viewModel = CameraPermissionViewModel(permissionsController)
    }

    /**
     * パーミッションリクエストで許可された時のパターン
     */
    @Test
    fun check_request_permission_on_granted() = runTest {
        viewModel.state.test {
            assertEquals(UiPermissionState.INITIAL, expectMostRecentItem().uiPermissionState)
            viewModel.requestPermission()
            testScheduler.advanceUntilIdle()
            assertEquals(UiPermissionState.GRANTED, awaitItem().uiPermissionState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * パーミッションリクエストで一度拒否された時のパターン
     */
    @Test
    fun check_request_permission_on_denied_once() = runTest {
        everySuspend { permissionsController.providePermission(Permission.CAMERA) } answers {
            throw DeniedException(Permission.CAMERA)
        }
        viewModel.state.test {
            viewModel.requestPermission()
            testScheduler.advanceUntilIdle()
            assertEquals(UiPermissionState.DENIED_ONCE, awaitItem().uiPermissionState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * パーミッションリクエストで常に拒否された時のパターン
     */
    @Test
    fun check_request_permission_on_denied_always() = runTest {
        everySuspend { permissionsController.providePermission(Permission.CAMERA) } answers {
            throw DeniedAlwaysException(Permission.CAMERA)
        }
        viewModel.state.test {
            viewModel.requestPermission()
            testScheduler.advanceUntilIdle()
            val result = awaitItem()
            assertEquals(UiPermissionState.ALWAYS_DENIED, result.uiPermissionState)
            assertTrue(result.isAlwaysDeniedDialogVisible)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * ダイアログを閉じる処理のテスト
     */
    @Test
    fun check_on_dismiss_dialog() = runTest {
        everySuspend { permissionsController.providePermission(Permission.CAMERA) } answers {
            throw DeniedAlwaysException(Permission.CAMERA)
        }
        viewModel.state.test {
            viewModel.requestPermission()
            testScheduler.advanceUntilIdle()
            assertTrue(awaitItem().isAlwaysDeniedDialogVisible)
            viewModel.onDismissDialog()
            assertFalse(awaitItem().isAlwaysDeniedDialogVisible)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * 設定画面を開く処理のテスト
     */
    @Test
    fun check_open_settings() = runTest {
        viewModel.openSettings()
        verify(exactly(1)) { permissionsController.openAppSettings() }
    }

    /**
     * パーミッション確認処理のテスト
     */
    @Test
    fun check_if_have_permission() = runTest {
        everySuspend { permissionsController.isPermissionGranted(Permission.CAMERA) } returns true
        assertTrue(viewModel.checkIfHavePermission())
        everySuspend { permissionsController.isPermissionGranted(Permission.CAMERA) } returns false
        assertFalse(viewModel.checkIfHavePermission())
    }
}

