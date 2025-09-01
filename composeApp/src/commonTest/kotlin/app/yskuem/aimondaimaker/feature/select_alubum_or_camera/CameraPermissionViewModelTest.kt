package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import MainDispatcherTestBase
import app.cash.turbine.test
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.state.UiPermissionState
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CameraPermissionViewModelTest : MainDispatcherTestBase() {
    private val permissionsController: PermissionsController = mock()
    private lateinit var viewModel: CameraPermissionViewModel

    @BeforeTest
    fun setup() {
        everySuspend { permissionsController.providePermission(Permission.CAMERA) } returns Unit
        every { permissionsController.openAppSettings() } returns Unit
        everySuspend { permissionsController.isPermissionGranted(Permission.CAMERA) } returns false
        viewModel = CameraPermissionViewModel(
            permissionsController = permissionsController,
        )
    }

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

    @Test
    fun check_request_permission_on_denied_once() = runTest {
        everySuspend { permissionsController.providePermission(Permission.CAMERA) } throws DeniedException(Permission.CAMERA)

        viewModel.state.test {
            viewModel.requestPermission()
            testScheduler.advanceUntilIdle()

            assertEquals(UiPermissionState.DENIED_ONCE, awaitItem().uiPermissionState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun check_request_permission_on_denied_always_and_dismiss_dialog() = runTest {
        everySuspend { permissionsController.providePermission(Permission.CAMERA) } throws DeniedAlwaysException(Permission.CAMERA)

        viewModel.state.test {
            viewModel.requestPermission()
            testScheduler.advanceUntilIdle()

            val deniedState = awaitItem()
            assertEquals(UiPermissionState.ALWAYS_DENIED, deniedState.uiPermissionState)
            assertEquals(true, deniedState.isAlwaysDeniedDialogVisible)

            viewModel.onDismissDialog()

            assertFalse(awaitItem().isAlwaysDeniedDialogVisible)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun check_check_if_have_permission() = runTest {
        everySuspend { permissionsController.isPermissionGranted(Permission.CAMERA) } returns true
        val result = viewModel.checkIfHavePermission()
        assertEquals(true, result)
    }
}

