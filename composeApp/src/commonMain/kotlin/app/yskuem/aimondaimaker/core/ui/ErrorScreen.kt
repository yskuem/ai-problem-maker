package app.yskuem.aimondaimaker.core.ui

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.error_occurred
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorScreen(
    buttonText: String,
    onButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(
                stringResource(Res.string.error_occurred),
                fontSize = 20.sp,
            )
            Button(
                onClick = onButtonClick,
            ) {
                Text(buttonText)
            }
        }
    }
}
