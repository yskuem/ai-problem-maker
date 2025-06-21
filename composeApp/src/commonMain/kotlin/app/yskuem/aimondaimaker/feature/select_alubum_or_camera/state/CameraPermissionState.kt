package app.yskuem.aimondaimaker.feature.select_alubum_or_camera.state

data class CameraPermissionState(
    val uiPermissionState: UiPermissionState,
    val isAlwaysDeniedDialogVisible: Boolean = false,
)

enum class UiPermissionState {
    GRANTED,
    ALWAYS_DENIED,
    DENIED_ONCE,
    INITIAL,
}
