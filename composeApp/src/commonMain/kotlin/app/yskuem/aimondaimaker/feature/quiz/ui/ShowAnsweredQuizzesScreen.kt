package app.yskuem.aimondaimaker.feature.quiz.ui

import androidx.compose.runtime.Composable
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
        val viewmodel = koinScreenModel<ShowQuizScreenViewModel>()
        val navigator = LocalNavigator.current

        BackHandler {
            viewmodel.showInterstitialAd()
            navigator?.pop()
        }
        QuizApp(quizList = quizList) {
            viewmodel.showInterstitialAd()
            navigator?.pop()
        }
    }
}
