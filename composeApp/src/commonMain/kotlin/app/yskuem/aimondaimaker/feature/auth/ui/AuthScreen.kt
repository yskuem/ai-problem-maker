package app.yskuem.aimondaimaker.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.yskuem.aimondaimaker.core.ui.theme.ComponentSpacing
import app.yskuem.aimondaimaker.core.ui.theme.Spacing
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

class AuthScreen : Screen {
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
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            if (hasError.value) {
                Text(
                    text = "エラーが発生しました。",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(ComponentSpacing.iconXLarge),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = Spacing.xs,
                )
            }
        }
    }
}
