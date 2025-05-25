package app.yskuem.aimondaimaker.feature.select_alubum_or_camera.mode

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.note_mode_page_explanation
import ai_problem_maker.composeapp.generated.resources.note_mode_page_how_to_explanation
import ai_problem_maker.composeapp.generated.resources.note_mode_page_title
import ai_problem_maker.composeapp.generated.resources.quiz_mode_page_explanation
import ai_problem_maker.composeapp.generated.resources.quiz_mode_page_how_to_explanation
import ai_problem_maker.composeapp.generated.resources.quiz_mode_page_title
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Note
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

enum class NavCreateMode {
    Quiz,
    Note,
}

sealed class CreateMode(
    val title: StringResource,
    val contentDescription: StringResource,
    val usage: StringResource,
    val icon: ImageVector,
) {
    data object Quiz : CreateMode(
        title = Res.string.quiz_mode_page_title,
        contentDescription = Res.string.quiz_mode_page_explanation,
        usage = Res.string.quiz_mode_page_how_to_explanation,
        icon = Icons.AutoMirrored.Outlined.Help,
    )
    data object Note : CreateMode(
        title = Res.string.note_mode_page_title,
        contentDescription = Res.string.note_mode_page_explanation,
        usage = Res.string.note_mode_page_how_to_explanation,
        icon = Icons.AutoMirrored.Outlined.Note
    )
}

fun NavCreateMode.toCreateMode(): CreateMode =
    when (this) {
        NavCreateMode.Quiz -> CreateMode.Quiz
        NavCreateMode.Note -> CreateMode.Note
    }