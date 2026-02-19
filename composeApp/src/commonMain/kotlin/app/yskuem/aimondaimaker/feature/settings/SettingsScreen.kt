package app.yskuem.aimondaimaker.feature.settings

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.cancel
import ai_problem_maker.composeapp.generated.resources.link_error
import ai_problem_maker.composeapp.generated.resources.link_success
import ai_problem_maker.composeapp.generated.resources.linking_account
import ai_problem_maker.composeapp.generated.resources.logout
import ai_problem_maker.composeapp.generated.resources.logout_confirm_message
import ai_problem_maker.composeapp.generated.resources.logout_confirm_title
import ai_problem_maker.composeapp.generated.resources.settings_title
import ai_problem_maker.composeapp.generated.resources.social_login
import ai_problem_maker.composeapp.generated.resources.social_login_description
import ai_problem_maker.composeapp.generated.resources.sign_in_with_apple
import ai_problem_maker.composeapp.generated.resources.sign_in_with_google
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.yskuem.aimondaimaker.feature.onboarding.WelcomeScreen
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
        var showLogoutDialog by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }

        val linkSuccessMessage = stringResource(Res.string.link_success)
        val linkErrorMessage = stringResource(Res.string.link_error)

        // ログアウト完了 → WelcomeScreen へ
        LaunchedEffect(uiState.loggedOut) {
            if (uiState.loggedOut) {
                navigator.replaceAll(WelcomeScreen())
            }
        }

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
                    title = {
                        Text(
                            text = stringResource(Res.string.settings_title),
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
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
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // --- アカウントセクション ---
                SectionCard {
                    if (uiState.isAnonymous) {
                        // 匿名ユーザー：サインイン導線
                        SettingsRow(
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonOutline,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(22.dp),
                                    )
                                }
                            },
                            title = stringResource(Res.string.social_login),
                            subtitle = stringResource(Res.string.social_login_description),
                            trailing = {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            onClick = { showLoginDialog = true },
                        )
                    } else {
                        // ログイン済み：プロバイダーとメール表示
                        val provider = uiState.linkedProvider
                        if (provider != null) {
                            val providerDisplayName = when (provider) {
                                "google" -> "Google"
                                "apple" -> "Apple"
                                else -> provider.replaceFirstChar { it.uppercase() }
                            }
                            val providerColor = when (provider) {
                                "google" -> Color(0xFF4285F4)
                                "apple" -> MaterialTheme.colorScheme.onSurface
                                else -> MaterialTheme.colorScheme.primary
                            }
                            SettingsRow(
                                icon = {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(providerColor.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = providerColor,
                                            modifier = Modifier.size(22.dp),
                                        )
                                    }
                                },
                                title = stringResource(Res.string.social_login),
                                subtitle = "$providerDisplayName: ${uiState.linkedEmail ?: ""}",
                                trailing = null,
                                onClick = null,
                            )
                        }
                    }
                }

                // --- ログアウトセクション（ソーシャルログイン済みのみ） ---
                if (!uiState.isAnonymous) {
                    SectionCard {
                        SettingsRow(
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.errorContainer),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Logout,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.size(22.dp),
                                    )
                                }
                            },
                            title = stringResource(Res.string.logout),
                            titleColor = MaterialTheme.colorScheme.error,
                            trailing = null,
                            onClick = { showLogoutDialog = true },
                        )
                    }
                }
            }
        }

        // --- ソーシャルログインダイアログ ---
        if (showLoginDialog) {
            Dialog(onDismissRequest = {
                if (!uiState.isLinking) showLoginDialog = false
            }) {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // ヘッダー
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(Res.string.social_login),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.align(Alignment.Center),
                            )
                            if (!uiState.isLinking) {
                                IconButton(
                                    onClick = { showLoginDialog = false },
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .size(36.dp),
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(Res.string.cancel),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(Res.string.social_login_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        if (uiState.isLinking) {
                            // ローディング
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 24.dp),
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(36.dp),
                                    strokeWidth = 3.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(Res.string.linking_account),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                // Apple サインイン（iOSのみ）
                                if (isIOS) {
                                    Button(
                                        onClick = { viewModel.linkWithApple() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF000000),
                                        ),
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            Text(
                                                text = "\uF8FF",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White,
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = stringResource(Res.string.sign_in_with_apple),
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White,
                                            )
                                        }
                                    }
                                }

                                // Google サインイン
                                OutlinedButton(
                                    onClick = { viewModel.linkWithGoogle() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                    ),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        Text(
                                            text = "G",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4285F4),
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = stringResource(Res.string.sign_in_with_google),
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- ログアウト確認ダイアログ ---
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(
                        text = stringResource(Res.string.logout_confirm_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                text = {
                    Text(text = stringResource(Res.string.logout_confirm_message))
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            viewModel.logout()
                        },
                    ) {
                        Text(
                            text = stringResource(Res.string.logout),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text(text = stringResource(Res.string.cancel))
                    }
                },
            )
        }
    }
}

// --- 共通コンポーネント ---

@Composable
private fun SectionCard(
    content: @Composable () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            content()
        }
    }
}

@Composable
private fun SettingsRow(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String? = null,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = titleColor,
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (trailing != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailing()
        }
    }
}
