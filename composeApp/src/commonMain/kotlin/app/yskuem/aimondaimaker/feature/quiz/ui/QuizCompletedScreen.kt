package app.yskuem.aimondaimaker.feature.quiz.ui

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.export_quiz_pdf
import ai_problem_maker.composeapp.generated.resources.quiz_finished
import ai_problem_maker.composeapp.generated.resources.score_summary
import ai_problem_maker.composeapp.generated.resources.share_quiz
import ai_problem_maker.composeapp.generated.resources.try_again
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.ui.PdfDocument
import app.yskuem.aimondaimaker.core.ui.PdfPreviewerOverlayDialog
import app.yskuem.aimondaimaker.core.ui.PdfViewerDownloadViewModel
import app.yskuem.aimondaimaker.core.ui.components.ShareDialog
import app.yskuem.aimondaimaker.core.util.LaunchStoreReview
import app.yskuem.aimondaimaker.data.api.response.PdfResponse
import app.yskuem.aimondaimaker.domain.entity.Quiz
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import org.jetbrains.compose.resources.stringResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QuizCompletedScreen(
    score: Int,
    totalQuestions: Int,
    groupId: String,
    quizList: List<Quiz>,
    onRestart: () -> Unit,
    onPdfExport: () -> Unit,
    onClosePdfViewer: () -> Unit,
    pdfResponse: DataUiState<PdfResponse>,
) {

    val percentage = (score.toFloat() / totalQuestions * 100).toInt()
    var showShareDialog by remember { mutableStateOf(false) }

    val exportPdfLabel = stringResource(Res.string.export_quiz_pdf)

    val pdfDownloadViewModel: PdfViewerDownloadViewModel = koinInject()
    val downloadState by pdfDownloadViewModel.downloadState.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            pdfDownloadViewModel.clear()
        }
    }

    // アニメーション状態
    var isVisible by remember { mutableStateOf(false) }
    val animatedPercentage by animateIntAsState(
        targetValue = if (isVisible) percentage else 0,
        animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing)
    )
    val cardScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.96f,
        animationSpec = spring(dampingRatio = 0.7f)
    )
    val confettiAnim = rememberInfiniteTransition()
    val confettiRotation by confettiAnim.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000),
            repeatMode = RepeatMode.Restart
        )
    )

    LaunchedEffect(Unit) {
        delay(120)
        isVisible = true
    }

    // 完了時レビュー誘導
    LaunchStoreReview(
        trigger = true,
        onComplete = { result ->
            result.onSuccess { println("Store review requested successfully") }
                .onFailure { error -> println("Store review request failed: $error") }
        },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.04f)
                    )
                )
            )
    ) {
        // 背景コンフェッティ（背面）
        if (percentage >= 80) {
            ConfettiEffect(rotation = confettiRotation)
        }

        // スクロール可能なラッパー
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .padding(horizontal = 20.dp)
                    .scale(cardScale)
                    .shadow(
                        elevation = 18.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    // 結果アイコン
                    AnimatedResultIcon(percentage = percentage)

                    // タイトル
                    Text(
                        text = stringResource(Res.string.quiz_finished),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    // スコアサマリー
                    Text(
                        text = stringResource(Res.string.score_summary, score, totalQuestions),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    // パーセンテージ：カスタム円形リング（文言なし）
                    PercentageRing(
                        percentage = percentage,
                        animatedPercentage = animatedPercentage,
                        ringSize = 220.dp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // アクションボタン群（余白拡大）
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // 共有（プライマリ）
                        AnimatedActionButton(
                            onClick = { showShareDialog = true },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            icon = Icons.Default.Share,
                            text = stringResource(Res.string.share_quiz),
                            elevation = 4.dp
                        )

                        // PDFエクスポート
                        AnimatedActionButton(
                            onClick = onPdfExport,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            icon = Icons.Default.PictureAsPdf,
                            text = exportPdfLabel,
                            elevation = 2.dp
                        )

                        // もう一度挑戦（励まし文言は廃止）
                        AnimatedActionButton(
                            onClick = onRestart,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            icon = Icons.Default.Refresh,
                            text = stringResource(Res.string.try_again),
                            elevation = 2.dp
                        )
                    }
                }
            }

            // 画面下端の安全余白（ホームインジケータ等）
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    ShareDialog(
        isVisible = showShareDialog,
        groupId = groupId,
        quizList = quizList,
        onDismiss = { showShareDialog = false },
    )

    when(pdfResponse) {
        is DataUiState.Initial -> {}
        is DataUiState.Error -> {
        }
        is DataUiState.Loading -> {
            PdfGenerateLoading()
        }
        is DataUiState.Success -> {
            val pdfDocument = PdfDocument(
                bytes = pdfResponse.data.bytes,
                fileName = pdfResponse.data.filename,
            )
            PdfPreviewerOverlayDialog(
                pdf = pdfDocument,
                title = exportPdfLabel,
                onClickDownload = {
                    pdfDownloadViewModel.download(pdfDocument)
                },
                isDownloading = downloadState.isLoading,
                onCloseViewer = {
                    pdfDownloadViewModel.reset()
                    onClosePdfViewer()
                },
            )
        }
    }
}

@Composable
fun AnimatedActionButton(
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    icon: ImageVector,
    text: String,
    elevation: Dp = 2.dp,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.8f)
    )

    ElevatedButton(
        onClick = {
            pressed = true
            onClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = elevation,
            pressedElevation = elevation * 1.5f
        ),
        contentPadding = PaddingValues(
            horizontal = 18.dp,
            vertical = 12.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    LaunchedEffect(pressed) {
        if (pressed) {
            delay(110)
            pressed = false
        }
    }
}

@Composable
fun AnimatedResultIcon(percentage: Int) {
    val rotation by rememberInfiniteTransition().animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(84.dp)
            .clip(CircleShape)
            .background(
                when {
                    percentage >= 80 -> Color(0xFF4CAF50).copy(alpha = 0.10f)
                    percentage >= 60 -> Color(0xFFFFC107).copy(alpha = 0.10f)
                    else -> Color(0xFFFF5252).copy(alpha = 0.10f)
                }
            )
            .border(
                width = 3.dp,
                color = when {
                    percentage >= 80 -> Color(0xFF4CAF50)
                    percentage >= 60 -> Color(0xFFFFC107)
                    else -> Color(0xFFFF5252)
                },
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when {
                percentage >= 80 -> Icons.Default.EmojiEvents
                percentage >= 60 -> Icons.Default.ThumbUp
                else -> Icons.Default.SentimentDissatisfied
            },
            contentDescription = null,
            modifier = Modifier
                .size(42.dp)
                .rotate(if (percentage >= 80) rotation else 0f),
            tint = when {
                percentage >= 80 -> Color(0xFF4CAF50)
                percentage >= 60 -> Color(0xFFFFC107)
                else -> Color(0xFFFF5252)
            }
        )
    }
}

@Composable
fun PercentageRing(
    percentage: Int,
    animatedPercentage: Int,
    ringSize: Dp = 200.dp,
    strokeWidth: Dp = 14.dp
) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = when {
        percentage >= 80 -> Color(0xFF4CAF50)
        percentage >= 60 -> Color(0xFFFFC107)
        else -> Color(0xFFFF5252)
    }
    val sweep = (animatedPercentage / 100f) * 360f

    Box(
        modifier = Modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
            val inset = strokeWidth.toPx() / 2f

            // DrawScopeの描画領域サイズ（Px）
            val drawSize = this.size
            val diameterW = drawSize.width - inset * 2
            val diameterH = drawSize.height - inset * 2

            // トラック
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke,
                topLeft = Offset(inset, inset),
                size = Size(diameterW, diameterH)
            )
            // プログレス
            if (sweep > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = stroke,
                    topLeft = Offset(inset, inset),
                    size = Size(diameterW, diameterH)
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$animatedPercentage%",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp
                ),
                color = progressColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ConfettiEffect(rotation: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val colors = listOf(
            Color(0xFFFF6B6B),
            Color(0xFF4ECDC4),
            Color(0xFFFFE66D),
            Color(0xFF95E1D3),
            Color(0xFFF38181)
        )

        repeat(20) { i ->
            val angle = (i * 18f + rotation) * PI / 180
            val radius = size.minDimension * 0.3f
            val x = size.width / 2 + cos(angle).toFloat() * radius
            val y = size.height / 2 + sin(angle).toFloat() * radius

            rotate(rotation * (if (i % 2 == 0) 1 else -1), Offset(x, y)) {
                drawCircle(
                    color = colors[i % colors.size].copy(alpha = 0.6f),
                    radius = 8.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
    }
}
