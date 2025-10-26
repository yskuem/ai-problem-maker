package app.yskuem.aimondaimaker.feature.quiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.feature.quiz.viewmodel.ShowQuizScreenViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

data class ShowAnsweredQuizzesScreen(
    val quizList: List<Quiz>,
) : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewmodel = koinScreenModel<ShowQuizScreenViewModel>()
        val state by viewmodel.uiState.collectAsState()

        BackHandler {
            navigator?.pop()
        }
        QuizApp(
            quizList = quizList,
            pdfResponse = state.pdfData,
            onCreatePdf = {
                viewmodel.onCreatePdf(
                    quizList = quizList
                )
            },
            onClosePdfViewer = {
                viewmodel.onClosePdfViewer()
            },
            isSavingPdf = state.pdfSaveState.isLoading,
            onBack = {
                navigator?.pop()
            },
            onSavePdf = {
                viewmodel.onSavePdf(it)
            }
        )
    }
}
