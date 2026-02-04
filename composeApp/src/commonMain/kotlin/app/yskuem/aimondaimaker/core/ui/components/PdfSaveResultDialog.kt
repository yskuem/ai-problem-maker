package app.yskuem.aimondaimaker.core.ui.components

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.close
import ai_problem_maker.composeapp.generated.resources.pdf_save_failure_message
import ai_problem_maker.composeapp.generated.resources.pdf_save_failure_title
import ai_problem_maker.composeapp.generated.resources.pdf_save_success_message
import ai_problem_maker.composeapp.generated.resources.pdf_save_success_title
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.stringResource

enum class PdfSaveResultDialogType {
    Success,
    Failure,
}

@Composable
fun PdfSaveResultDialog(
    type: PdfSaveResultDialogType,
    onDismiss: () -> Unit,
) {
    val (titleRes, messageRes) =
        when (type) {
            PdfSaveResultDialogType.Success -> {
                Res.string.pdf_save_success_title to Res.string.pdf_save_success_message
            }

            PdfSaveResultDialogType.Failure -> {
                Res.string.pdf_save_failure_title to Res.string.pdf_save_failure_message
            }
        }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
        },
        text = {
            Text(
                text = stringResource(messageRes),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.close))
            }
        },
    )
}
