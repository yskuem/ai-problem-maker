package app.yskuem.aimondaimaker.feature.quiz.ui

import androidx.compose.runtime.Composable
import app.yskuem.aimondaimaker.domain.entity.Quiz
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

data class ShowAnsweredQuizzesScreen(
    val quizList: List<Quiz>
): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        QuizApp(quizList = quizList) {
            navigator?.pop()
        }
    }
}
