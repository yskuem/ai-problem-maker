package app.yskuem.aimondaimaker

import GlobalToastContainer
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import app.yskuem.aimondaimaker.feature.update_check.UpdateCheckScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition

@Composable
@Preview
fun App() {
    MaterialTheme {
        GlobalToastContainer {
            Navigator(UpdateCheckScreen()) { navigator ->
                FadeTransition(
                    navigator = navigator,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )
            }
        }
    }
}