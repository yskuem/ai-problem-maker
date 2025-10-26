package app.yskuem.aimondaimaker.feature.quiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.feature.quiz.viewmodel.ShowQuizScreenViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ShowAnsweredQuizzesScreen private constructor(
    private val quizListJson: String,
) : Screen {

    constructor(quizList: List<Quiz>) : this(
        quizListJson = serializer.encodeToString(ListSerializer(Quiz.serializer()), quizList),
    )

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewmodel = koinScreenModel<ShowQuizScreenViewModel>()
        val state by viewmodel.uiState.collectAsState()
        val quizList = remember(quizListJson) {
            serializer.decodeFromString(ListSerializer(Quiz.serializer()), quizListJson)
        }

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
            onSavePdf = { pdfDate, pdfName ->
                viewmodel.onSavePdf(
                    pdfData = pdfDate,
                    pdfName = pdfName,
                )
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShowAnsweredQuizzesScreen) return false

        return quizListJson == other.quizListJson
    }

    override fun hashCode(): Int = quizListJson.hashCode()

    companion object {
        private val serializer = Json { encodeDefaults = true }
    }
}
