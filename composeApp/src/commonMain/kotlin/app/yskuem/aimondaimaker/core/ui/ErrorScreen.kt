package app.yskuem.aimondaimaker.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ErrorScreen(
    buttonText: String,
    onButtonClick: () -> Unit,
) {
    Box (
        modifier = Modifier.fillMaxSize()
    ){
        Column (
            modifier = Modifier.align(Alignment.Center)
        ){
            Text("エラーが発生しました。",
                fontSize = 20.sp
            )
            Button(
                onClick = onButtonClick
            ) {
                Text(buttonText)
            }
        }
    }
}