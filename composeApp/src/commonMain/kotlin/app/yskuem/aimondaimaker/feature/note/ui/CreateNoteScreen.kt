package app.yskuem.aimondaimaker.feature.note.ui

import PastelAppleStyleLoading
import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.note_generating
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.yskuem.aimondaimaker.core.ui.DataUiState
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateNoteScreen(
    val imageByte: ByteArray,
    val fileName: String = "image",
    val extension: String,
    val projectId: String? = null,
    navController: NavController,
) {
    val viewmodel = koinInject<ShowNoteScreenViewModel>()
    val state by viewmodel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewmodel.onLoadPage(
                imageByte = imageByte,
                fileName = fileName,
                extension = extension,
                projectId = projectId,
            )
        }

        LaunchedEffect(Unit) {
            viewmodel.showInterstitialAd()
        }

        when(val result = state.note) {
            is DataUiState.Error -> {
                Text(result.throwable.toString())
            }
            is DataUiState.Loading -> {
                PastelAppleStyleLoading(
                    loadingTitle = stringResource(Res.string.note_generating)
                )
            }
            is DataUiState.Success -> {
                NoteApp(note = result.data) {
                    navController.popBackStack()
                }
            }
        }
}
