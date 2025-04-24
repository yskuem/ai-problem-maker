package app.yskuem.aimondaimaker

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(SelectProjectScreen()) { navigator ->
            FadeTransition(
                navigator = navigator,
                animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
            )
        }
    }
}