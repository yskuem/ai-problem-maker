package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.yskuem.aimondaimaker.feature.note.ui.CreateNoteScreen
import app.yskuem.aimondaimaker.feature.quiz.ui.CreateQuizScreen
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.mode.CreateMode
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.mode.NavCreateMode
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.state.CameraPermissionState
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.state.UiPermissionState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.core.parameter.parametersOf

data class CameraPermissionScreen(
    val mode: NavCreateMode
) : Screen {
    @Composable
    override fun Content() {
        val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
        val permissionsController: PermissionsController =
            remember(factory) { factory.createPermissionsController() }

        BindEffect(permissionsController)

        val viewModel: CameraPermissionViewModel = koinScreenModel<CameraPermissionViewModel>(
            parameters = { parametersOf(permissionsController) }
        )
        val state by viewModel.state.collectAsState()
        var hasPermissionAlready by rememberSaveable { mutableStateOf(false) }
        /*
        * hasPermissionAlready can be take time to return the value as it is suspend
        * so we should show place holder like CircularProgressIndicator
        * */
        var isPermissionChecked by rememberSaveable { mutableStateOf(false) }

        val navigator = LocalNavigator.current

        val onImageReady: (ByteArray) -> Unit = { bytes ->
            when(mode) {
                NavCreateMode.Note -> {
                    navigator?.push(
                        CreateNoteScreen(
                            imageByte = bytes,
                            extension = "jpg"
                        )
                    )
                }
                NavCreateMode.Quiz -> {
                    navigator?.push(
                        CreateQuizScreen(
                            imageByte = bytes,
                            extension = "jpg"
                        )
                    )
                }
            }
        }


        LaunchedEffect(Unit) {
            hasPermissionAlready = viewModel.checkIfHavePermission()
            isPermissionChecked = true
        }

        AnimatedContent(isPermissionChecked) { checked ->
            if (checked) {
                AnimatedContent(hasPermissionAlready) { hasPermission ->
                    if (hasPermission) {
                        CameraPickerView(
                            upLoadImage = onImageReady
                        )
                    } else {
                        CameraPermission(
                            state = state,
                            onRequestPermission = viewModel::requestPermission,
                            openSettings = viewModel::openSettings,
                            onDismiss = viewModel::onDismissDialog,
                            onImageReady = onImageReady
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun CameraPermission(
    modifier: Modifier = Modifier,
    state: CameraPermissionState,
    onRequestPermission: () -> Unit,
    openSettings: () -> Unit,
    onDismiss: () -> Unit,
    onImageReady: (ByteArray) -> Unit
) {

    LaunchedEffect(Unit) {
        if (state.uiPermissionState == UiPermissionState.INITIAL) {
            onRequestPermission()
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "カメラのアクセスを許可してください。",
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
            )
        }
    }

    when {
        state.uiPermissionState == UiPermissionState.GRANTED -> CameraPickerView(
            upLoadImage = onImageReady
        )
        state.isAlwaysDeniedDialogVisible -> AlwaysDeniedDialog(
            onOpenSettings = openSettings,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun AlwaysDeniedDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onOpenSettings()
                onDismiss()

            }) {
                Text("Open app settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(text = "Camera permission required") },
        text = { Text("We need camera permission in order to use the camera") }
    )

}