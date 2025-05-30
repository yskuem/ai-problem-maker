package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import app.yskuem.aimondaimaker.core.ui.ErrorScreen
import app.yskuem.aimondaimaker.core.ui.LoadingScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun CameraPickerView(
    upLoadImage: (ByteArray) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val navigator = LocalNavigator.current
    val isLoading = remember { mutableStateOf(false) }
    val hasError = remember { mutableStateOf(false) }
    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            isLoading.value = true
            val bytes = withContext(Dispatchers.Default) {
                it?.toByteArray()
            }
            if (bytes == null) {
                navigator?.pop()
                return@launch
            }
            upLoadImage(bytes)
            isLoading.value = false
        }
    }

    LaunchedEffect(Unit) {
        try {
            cameraManager.launch()
        } catch (e: Exception) {
            hasError.value = true
        }
    }
    if (isLoading.value) {
        LoadingScreen()
    }
    if (hasError.value) {
        ErrorScreen(
            buttonText = "戻る"
        ) {
            navigator?.pop()
        }
    }
}