package app.yskuem.aimondaimaker

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreen
import cafe.adriel.voyager.navigator.Navigator

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(SelectProjectScreen())
    }
}