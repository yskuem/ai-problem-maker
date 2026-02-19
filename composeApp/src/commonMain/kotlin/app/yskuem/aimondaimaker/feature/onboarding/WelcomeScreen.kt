package app.yskuem.aimondaimaker.feature.onboarding

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.cancel
import ai_problem_maker.composeapp.generated.resources.ic_splash_icon
import ai_problem_maker.composeapp.generated.resources.link_error
import ai_problem_maker.composeapp.generated.resources.linking_account
import ai_problem_maker.composeapp.generated.resources.sign_in_with_apple
import ai_problem_maker.composeapp.generated.resources.sign_in_with_google
import ai_problem_maker.composeapp.generated.resources.social_login
import ai_problem_maker.composeapp.generated.resources.social_login_description
import ai_problem_maker.composeapp.generated.resources.welcome_get_started
import ai_problem_maker.composeapp.generated.resources.welcome_sign_in_to_start
import ai_problem_maker.composeapp.generated.resources.welcome_subtitle
import ai_problem_maker.composeapp.generated.resources.welcome_title
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreen
import app.yskuem.aimondaimaker.getPlatform
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class WelcomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<WelcomeScreenViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val isIOS = getPlatform().name.startsWith("iOS")
        var showLoginDialog by remember { mutableStateOf(false) }
        var isVisible by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }

        val linkErrorMessage = stringResource(Res.string.link_error)

        LaunchedEffect(Unit) {
            isVisible = true
        }

        // ログイン成功 → メイン画面へ
        LaunchedEffect(uiState.loginSuccess) {
            if (uiState.loginSuccess) {
                showLoginDialog = false
                navigator.replace(SelectProjectScreen())
            }
        }

        // エラー時 → スナックバー
        LaunchedEffect(uiState.loginError) {
            uiState.loginError?.let {
                showLoginDialog = false
                snackbarHostState.showSnackbar("$linkErrorMessage: $it")
                viewModel.clearError()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        ),
                    ),
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))

                // アプリアイコン
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { -40 },
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_splash_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // タイトル
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { -20 },
                ) {
                    Text(
                        text = stringResource(Res.string.welcome_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // サブタイトル
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { -10 },
                ) {
                    Text(
                        text = stringResource(Res.string.welcome_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // ボタンエリア
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { 40 },
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // 「はじめる」（匿名ログイン）
                        Button(
                            onClick = { viewModel.startAnonymous() },
                            enabled = !uiState.isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                        ) {
                            if (uiState.isLoading && !uiState.isSocialSigningIn) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp,
                                )
                            } else {
                                Text(
                                    text = stringResource(Res.string.welcome_get_started),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 「ログインしてはじめる」
                        TextButton(
                            onClick = { showLoginDialog = true },
                            enabled = !uiState.isLoading,
                        ) {
                            Text(
                                text = stringResource(Res.string.welcome_sign_in_to_start),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }

            // スナックバー
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

        // --- ソーシャルログインダイアログ ---
        if (showLoginDialog) {
            Dialog(onDismissRequest = {
                if (!uiState.isSocialSigningIn) showLoginDialog = false
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
                            if (!uiState.isSocialSigningIn) {
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

                        if (uiState.isSocialSigningIn) {
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
                                // Apple（iOSのみ）
                                if (isIOS) {
                                    Button(
                                        onClick = { viewModel.signInWithApple() },
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

                                // Google
                                OutlinedButton(
                                    onClick = { viewModel.signInWithGoogle() },
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
    }
}
