package app.yskuem.aimondaimaker.feature.quiz.ui

import PastelAppleStyleLoading
import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.quiz_generating
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.ui.ErrorScreen
import app.yskuem.aimondaimaker.core.ui.ErrorScreenType
import app.yskuem.aimondaimaker.feature.ad.ui.InterstitialHost
import app.yskuem.aimondaimaker.feature.quiz.viewmodel.ShowQuizScreenViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.stringResource

data class CreateQuizScreen(
    val imageByte: ByteArray,
    val fileName: String = "image",
    val extension: String,
    val projectId: String? = null,
) : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val viewmodel = koinScreenModel<ShowQuizScreenViewModel>()
        val state by viewmodel.uiState.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(Unit) {
            viewmodel.onLoadPage(
                imageByte = imageByte,
                fileName = fileName,
                extension = extension,
                projectId = projectId,
            )
        }

        InterstitialHost()

        BackHandler {
            navigator?.pop()
        }

        when (val quizList = state.quizList) {
            is DataUiState.Initial -> {}
            is DataUiState.Error -> {
                ErrorScreen(
                    type = ErrorScreenType.BACK,
                    errorMessage = quizList.throwable.message ?: "Unknown Error",
                )
            }
            is DataUiState.Loading -> {
                PastelAppleStyleLoading(
                    loadingTitle = stringResource(Res.string.quiz_generating),
                )
            }
            is DataUiState.Success -> {
                QuizApp(
                    quizList = quizList.data,
                    onCreatePdf = {
                        viewmodel.onCreatePdf(
                            quizList = quizList.data
                        )
                    },
                    onClosePdfViewer = {
                        viewmodel.onClosePdfViewer()
                    },
                    pdfResponse = state.pdfData,
                    isSavingPdf = state.pdfSaveState.isLoading,
                    onBack = {
                        navigator?.pop()
                    },
                    onSavePdf = { pdfData, pdfName ->
                        viewmodel.onSavePdf(
                            pdfData = pdfData,
                            pdfName = pdfName,
                        )
                    }
                )
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CreateQuizScreen

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
