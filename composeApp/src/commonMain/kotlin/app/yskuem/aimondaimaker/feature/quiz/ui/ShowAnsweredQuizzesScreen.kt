package app.yskuem.aimondaimaker.feature.quiz.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import app.yskuem.aimondaimaker.domain.entity.Quiz
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

data class ShowAnsweredQuizzesScreen(
    val quizList: List<Quiz>,
) : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        BackHandler {
            navigator?.pop()
        }
        QuizApp(quizList = quizList) {
            navigator?.pop()
        }
    }
}
