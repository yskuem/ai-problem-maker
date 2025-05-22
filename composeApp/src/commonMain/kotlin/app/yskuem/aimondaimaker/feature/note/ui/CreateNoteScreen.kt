package app.yskuem.aimondaimaker.feature.note.ui

import PastelAppleStyleLoading
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.yskuem.aimondaimaker.core.ui.DataUiState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

data class CreateNoteScreen(
    val imageByte: ByteArray,
    val fileName: String = "image",
    val extension: String
): Screen {
    @Composable
    override fun Content() {
        val viewmodel = koinScreenModel<ShowNoteScreenViewModel> ()
        val state by viewmodel.uiState.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(Unit) {
            viewmodel.onLoadPage(
                imageByte = imageByte,
                fileName = fileName,
                extension = extension
            )
        }

        when(val result = state.note) {
            is DataUiState.Error -> {
                Text(result.throwable.toString())
            }
            is DataUiState.Loading -> {
                PastelAppleStyleLoading("ノート")
            }
            is DataUiState.Success -> {
                NoteApp(note = result.data) {
                    navigator?.pop()
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CreateNoteScreen

        if (!imageByte.contentEquals(other.imageByte)) return false
        if (fileName != other.fileName) return false
        if (extension != other.extension) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageByte.contentHashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + extension.hashCode()
        return result
    }

}
