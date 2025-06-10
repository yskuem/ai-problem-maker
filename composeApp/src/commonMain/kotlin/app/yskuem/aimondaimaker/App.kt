package app.yskuem.aimondaimaker

import GlobalToastContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import app.yskuem.aimondaimaker.feature.update_check.UpdateCheckScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        GlobalToastContainer {
            Navigator(UpdateCheckScreen())
        }
    }
}