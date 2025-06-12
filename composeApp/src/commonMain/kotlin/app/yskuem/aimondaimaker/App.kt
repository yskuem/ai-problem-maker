package app.yskuem.aimondaimaker

import GlobalToastContainer
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import app.yskuem.aimondaimaker.feature.update_check.UpdateCheckScreen
import app.yskuem.aimondaimaker.feature.auth.ui.AuthScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
@Preview
fun App() {
    MaterialTheme {
        GlobalToastContainer {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "update_check",
            ) {
                composable("update_check") {
                    UpdateCheckScreen(navController)
                }
                composable("auth") {
                    AuthScreen(navController)
                }
            }
        }
    }
}