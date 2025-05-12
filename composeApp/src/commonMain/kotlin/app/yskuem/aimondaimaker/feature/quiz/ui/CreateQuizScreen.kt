package app.yskuem.aimondaimaker.feature.quiz.ui

import PastelAppleStyleLoading
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.feature.quiz.viewmodel.ShowQuizScreenViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

data class CreateQuizScreen(
    val imageByte: ByteArray,
    val fileName: String = "image",
    val extension: String
): Screen {
    @Composable
    override fun Content() {
        val viewmodel = koinScreenModel<ShowQuizScreenViewModel> ()
        val state by viewmodel.uiState.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(Unit) {
            viewmodel.onLoadPage(
                imageByte = imageByte,
                fileName = fileName,
                extension = extension
            )
        }

        when(val quizList = state.quizList) {
            is DataUiState.Error -> {
                Text(quizList.throwable.toString())
            }
            is DataUiState.Loading -> {
                PastelAppleStyleLoading()
            }
            is DataUiState.Success -> {
                QuizApp(quizList.data) {
                    navigator?.pop()
                }
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

