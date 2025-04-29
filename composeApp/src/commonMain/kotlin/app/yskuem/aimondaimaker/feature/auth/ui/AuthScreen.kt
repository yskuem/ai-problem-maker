package app.yskuem.aimondaimaker.feature.auth.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator


class AuthScreen(): Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<AuthScreenViewModel>()
        val navigator = LocalNavigator.current
        val isLoginSuccess = viewModel.isLoginSuccess.collectAsState()
        val hasError = viewModel.hasError.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.login()
        }
        LaunchedEffect(isLoginSuccess.value) {
            if (isLoginSuccess.value) {
                navigator?.replace(SelectProjectScreen())
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(hasError.value) {
                Text("エラーが発生しました。")
            } else {
                CircularProgressIndicator()
            }
        }
    }
}