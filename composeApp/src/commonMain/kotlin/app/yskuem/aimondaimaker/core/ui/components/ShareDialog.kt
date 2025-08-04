package app.yskuem.aimondaimaker.core.ui.components

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.close
import ai_problem_maker.composeapp.generated.resources.copy_link
import ai_problem_maker.composeapp.generated.resources.link_copied
import ai_problem_maker.composeapp.generated.resources.share_dialog_title
import ai_problem_maker.composeapp.generated.resources.share_with_others
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.yskuem.aimondaimaker.core.util.ShareManager
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShareDialog(
    isVisible: Boolean,
    quizUrl: String,
    shareManager: ShareManager,
    groupId: String,
    quizList: List<app.yskuem.aimondaimaker.domain.entity.Quiz>,
    userId: String,
    onDismiss: () -> Unit,
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
        ) {
            ShareDialogContent(
                quizUrl = quizUrl,
                shareManager = shareManager,
                groupId = groupId,
                quizList = quizList,
                userId = userId,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun ShareDialogContent(
    quizUrl: String,
    shareManager: ShareManager,
    groupId: String,
    quizList: List<app.yskuem.aimondaimaker.domain.entity.Quiz>,
    userId: String,
    onDismiss: () -> Unit,
) {
    var showCopiedMessage by remember { mutableStateOf(false) }
    val shareDialogTitle = stringResource(Res.string.share_dialog_title)
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = shareDialogTitle,
                style =
                    MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (showCopiedMessage) {
                Text(
                    text = stringResource(Res.string.link_copied),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        shareManager.saveQuizToSupabase(groupId, quizList, userId)
                    }
                    shareManager.copyToClipboard(quizUrl)
                    showCopiedMessage = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.copy_link),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        shareManager.saveQuizToSupabase(groupId, quizList, userId)
                    }
                    shareManager.shareText(
                        text = quizUrl,
                        title = shareDialogTitle,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.share_with_others),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(Res.string.close),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}