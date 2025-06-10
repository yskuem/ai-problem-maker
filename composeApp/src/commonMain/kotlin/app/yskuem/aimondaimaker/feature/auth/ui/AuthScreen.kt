package app.yskuem.aimondaimaker.feature.auth.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreen
import androidx.navigation.NavController
import org.koin.compose.koinInject


@Composable
fun AuthScreen(
    navController: NavController,
) {
    val viewModel = koinInject<AuthScreenViewModel>()
    val isLoginSuccess = viewModel.isLoginSuccess.collectAsState()
    val hasError = viewModel.hasError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.login()
    }
    LaunchedEffect(isLoginSuccess.value) {
        if (isLoginSuccess.value) {
            navController.navigate("select_project") {
                popUpTo("auth") { inclusive = true }
            }
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