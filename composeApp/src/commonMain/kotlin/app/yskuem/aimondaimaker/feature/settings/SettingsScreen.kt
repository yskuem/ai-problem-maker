package app.yskuem.aimondaimaker.feature.settings

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.cancel
import ai_problem_maker.composeapp.generated.resources.link_error
import ai_problem_maker.composeapp.generated.resources.link_success
import ai_problem_maker.composeapp.generated.resources.linking_account
import ai_problem_maker.composeapp.generated.resources.settings_title
import ai_problem_maker.composeapp.generated.resources.social_login
import ai_problem_maker.composeapp.generated.resources.social_login_description
import ai_problem_maker.composeapp.generated.resources.sign_in_with_apple
import ai_problem_maker.composeapp.generated.resources.sign_in_with_google
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.yskuem.aimondaimaker.getPlatform
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.stringResource

class SettingsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<SettingsScreenViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val isIOS = getPlatform().name.startsWith("iOS")
        var showLoginDialog by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }

        val linkSuccessMessage = stringResource(Res.string.link_success)
        val linkErrorMessage = stringResource(Res.string.link_error)

        // Show snackbar on success or error
        LaunchedEffect(uiState.linkSuccess, uiState.linkError) {
            if (uiState.linkSuccess) {
                showLoginDialog = false
                snackbarHostState.showSnackbar(linkSuccessMessage)
                viewModel.clearMessages()
            }
            uiState.linkError?.let {
                snackbarHostState.showSnackbar("$linkErrorMessage: $it")
                viewModel.clearMessages()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(Res.string.settings_title)) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                // ソーシャルログイン項目
                if (uiState.isAnonymous) {
                    // 匿名ユーザー：ログインオプションを表示
                    ListItem(
                        headlineContent = { Text(stringResource(Res.string.social_login)) },
                        supportingContent = {
                            Text(
                                text = stringResource(Res.string.social_login_description),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showLoginDialog = true },
                    )
                    HorizontalDivider()
                } else {
                    // ログイン済み：プロバイダーとメールアドレスを表示
                    val provider = uiState.linkedProvider
                    if (provider != null) {
                        val providerDisplayName = when (provider) {
                            "google" -> "Google"
                            "apple" -> "Apple"
                            else -> provider.replaceFirstChar { it.uppercase() }
                        }
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = stringResource(Res.string.social_login),
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = "$providerDisplayName: ${uiState.linkedEmail ?: ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                        )
                        HorizontalDivider()
                    }
                }
            }

            // ソーシャルログイン選択ダイアログ
            if (showLoginDialog) {
                Dialog(onDismissRequest = {
                    if (!uiState.isLinking) showLoginDialog = false
                }) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            // ヘッダー：タイトルと閉じるボタン
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                // タイトル
                                Text(
                                    text = stringResource(Res.string.social_login),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.align(Alignment.Center),
                                )

                                // 閉じるボタン（リンク中は無効）
                                if (!uiState.isLinking) {
                                    IconButton(
                                        onClick = { showLoginDialog = false },
                                        modifier = Modifier.align(Alignment.CenterEnd),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = stringResource(Res.string.cancel),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 説明テキスト
                            Text(
                                text = stringResource(Res.string.social_login_description),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp),
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            if (uiState.isLinking) {
                                // ローディング表示
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(vertical = 16.dp),
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = stringResource(Res.string.linking_account),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            } else {
                                // Apple サインインボタン（iOS のみ）
                                if (isIOS) {
                                    Button(
                                        onClick = { viewModel.linkWithApple() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF000000),
                                        ),
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            // Apple アイコン（Unicode）
                                            Text(
                                                text = "\uF8FF",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = Color.White,
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = stringResource(Res.string.sign_in_with_apple),
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.White,
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                // Google サインインボタン
                                OutlinedButton(
                                    onClick = { viewModel.linkWithGoogle() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                    ),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        // Google "G" アイコン
                                        Text(
                                            text = "G",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4285F4),
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = stringResource(Res.string.sign_in_with_google),
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
