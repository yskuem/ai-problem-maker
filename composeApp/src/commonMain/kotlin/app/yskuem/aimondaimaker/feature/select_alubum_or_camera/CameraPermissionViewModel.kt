package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.state.CameraPermissionState
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.state.UiPermissionState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.camera.CAMERA
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraPermissionViewModel(
    private val permissionsController: PermissionsController,
) : ScreenModel {
    private val _state = MutableStateFlow(CameraPermissionState(UiPermissionState.INITIAL))
    val state = _state.asStateFlow()

    fun requestPermission() {
        screenModelScope.launch {
            try {
                permissionsController.providePermission(Permission.CAMERA)
                _state.update { it.copy(uiPermissionState = UiPermissionState.GRANTED) }
            } catch (deniedAlways: DeniedAlwaysException) {
                _state.update {
                    it.copy(
                        uiPermissionState = UiPermissionState.ALWAYS_DENIED,
                        isAlwaysDeniedDialogVisible = true,
                    )
                }
            } catch (denied: DeniedException) {
                _state.update { it.copy(uiPermissionState = UiPermissionState.DENIED_ONCE) }
            }
        }
    }

    fun openSettings() {
        permissionsController.openAppSettings()
    }

    fun onDismissDialog() {
        _state.update { it.copy(isAlwaysDeniedDialogVisible = false) }
    }

    suspend fun checkIfHavePermission(): Boolean = permissionsController.isPermissionGranted(Permission.CAMERA)
}
