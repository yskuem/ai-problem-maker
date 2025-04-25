package app.yskuem.aimondaimaker.feature.problem.ui

import PastelAppleStyleLoading
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.feature.problem.viewmodel.ShowProblemScreenViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import io.github.vinceglb.filekit.PlatformFile

data class ShowProblemScreen(
    val image: PlatformFile
): Screen {
    @Composable
    override fun Content() {
        val viewmodel = koinScreenModel<ShowProblemScreenViewModel> ()
        val state by viewmodel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewmodel.onFetchProblems(image)
        }

        when(val problems = state.problems) {
            is DataUiState.Error -> {
                Text(problems.throwable.toString())
            }
            is DataUiState.Loading -> {
                PastelAppleStyleLoading()
            }
            is DataUiState.Success -> {
                Text(problems.data.toString())
            }
        }
    }
}