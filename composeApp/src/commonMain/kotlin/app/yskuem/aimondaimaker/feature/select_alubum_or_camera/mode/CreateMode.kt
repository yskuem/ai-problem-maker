package app.yskuem.aimondaimaker.feature.select_alubum_or_camera.mode

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Note
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Note
import androidx.compose.ui.graphics.vector.ImageVector

sealed class CreateMode(
    val title: String,
    val contentDescription: String,
    val usage: String,
    val icon: ImageVector,
) {
    data object Quiz : CreateMode(
        title = "AI問題作成",
        contentDescription = "教材やノートを撮影して\n" + "AIに問題を作成してもらいましょう",
        usage = "1. 教材やノートの写真を撮影\n" + "2. AIが自動的に問題を生成\n" + "3. 生成された問題を解いて学習しましょう",
        icon = Icons.AutoMirrored.Outlined.Help,
    )
    data object Note : CreateMode(
        title = "AIノート要約",
        contentDescription = "教材やノートを撮影して\n" + "AIに教材やノートをまとめてもらいましょう",
        usage = "1. 教材やノートの写真を撮影\n" + "2. AIが自動的にノートをまとめる\n" + "3. 生成された電子ノートで学習しましょう",
        icon = Icons.AutoMirrored.Outlined.Note
    )
}