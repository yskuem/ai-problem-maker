import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.update_icon_description
import ai_problem_maker.composeapp.generated.resources.update_now
import ai_problem_maker.composeapp.generated.resources.update_toast_message
import ai_problem_maker.composeapp.generated.resources.update_toast_title
import ai_problem_maker.composeapp.generated.resources.update_toast_title_with_version
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

enum class ToastPosition {
    TOP,
    BOTTOM,
    CENTER,
}

// パステル紫のカラーパレット
object PastelPurpleTheme {
    val primaryBackground = Color(0xFFE6E0FF)
    val secondaryBackground = Color(0xFFF3F0FF)
    val accentPurple = Color(0xFFB8A9FF)
    val darkPurple = Color(0xFF7C6FFF)
    val textPrimary = Color(0xFF4A4458)
    val textSecondary = Color(0xFF7A7A8A)
    val buttonBackground = Color(0xFFFFFFFF)
    val buttonText = Color(0xFF6B5CFF)
}

@Composable
fun UpdateToast(
    version: String,
    onUpdate: () -> Unit = {},
    onDismiss: () -> Unit = {},
    duration: Long = 5000L,
    modifier: Modifier = Modifier,
) {
    var isVisible by remember { mutableStateOf(true) }
    var offsetX by remember { mutableStateOf(0f) }
    var isDismissing by remember { mutableStateOf(false) }

    val titleText =
        if (version.isNotBlank()) {
            stringResource(Res.string.update_toast_title_with_version, version)
        } else {
            stringResource(Res.string.update_toast_title)
        }
    val messageText = stringResource(Res.string.update_toast_message)
    val buttonLabel = stringResource(Res.string.update_now)
    val iconDescription = stringResource(Res.string.update_icon_description)

    // フェードインアニメーション
    val alpha by animateFloatAsState(
        targetValue = if (isVisible && !isDismissing) 1f else 0f,
        animationSpec = tween(300, easing = EaseOutCubic),
        finishedListener = {
            if (it == 0f) onDismiss()
        },
    )

    // スライドアニメーション
    val slideOffset by animateIntAsState(
        targetValue = if (isDismissing) -1000 else offsetX.roundToInt(),
        animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
    )

    // 自動消去タイマー
    LaunchedEffect(Unit) {
        delay(duration)
        if (!isDismissing) {
            isDismissing = true
            isVisible = false
        }
    }

    fun dismissToast() {
        if (!isDismissing) {
            isDismissing = true
            isVisible = false
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter =
            slideInVertically(
                initialOffsetY = { -it },
                animationSpec =
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium,
                    ),
            ) +
                fadeIn(animationSpec = tween(300)),
        exit =
            slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300),
            ) +
                fadeOut(animationSpec = tween(300)),
    ) {
        Card(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .offset { IntOffset(slideOffset, 0) }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX < -100) {
                                    dismissToast()
                                } else {
                                    offsetX = 0f
                                }
                            },
                        ) { _, dragAmount ->
                            if (!isDismissing) {
                                offsetX = (offsetX + dragAmount).coerceAtMost(0f)
                            }
                        }
                    }.shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = PastelPurpleTheme.accentPurple.copy(alpha = 0.3f),
                    ),
            shape = RoundedCornerShape(16.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                ),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            brush =
                                androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors =
                                        listOf(
                                            PastelPurpleTheme.primaryBackground,
                                            PastelPurpleTheme.secondaryBackground,
                                        ),
                                ),
                            shape = RoundedCornerShape(16.dp),
                        ).clip(RoundedCornerShape(16.dp)),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // アイコン部分
                    Box(
                        modifier =
                            Modifier
                                .size(48.dp)
                                .background(
                                    PastelPurpleTheme.accentPurple.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp),
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.SystemUpdate,
                            contentDescription = iconDescription,
                            tint = PastelPurpleTheme.darkPurple,
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // テキスト部分
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = titleText,
                            color = PastelPurpleTheme.textPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = messageText,
                            color = PastelPurpleTheme.textSecondary,
                            fontSize = 12.sp,
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // アップデートボタン
                    Button(
                        onClick = {
                            onUpdate()
                            dismissToast()
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = PastelPurpleTheme.buttonBackground,
                                contentColor = PastelPurpleTheme.buttonText,
                            ),
                        shape = RoundedCornerShape(12.dp),
                        elevation =
                            ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 2.dp,
                            ),
                        modifier = Modifier.height(40.dp),
                    ) {
                        Text(
                            text = buttonLabel,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                        )
                    }
                }

                // 装飾的な要素
                Box(
                    modifier =
                        Modifier
                            .size(80.dp)
                            .offset(x = (-20).dp, y = (-20).dp)
                            .background(
                                PastelPurpleTheme.accentPurple.copy(alpha = 0.1f),
                                RoundedCornerShape(40.dp),
                            ).align(Alignment.TopEnd),
                )
            }
        }
    }
}

@Composable
fun ToastContainer(
    position: ToastPosition = ToastPosition.TOP,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment =
            when (position) {
                ToastPosition.TOP -> Alignment.TopCenter
                ToastPosition.BOTTOM -> Alignment.BottomCenter
                ToastPosition.CENTER -> Alignment.Center
            },
    ) {
        content()
    }
}

// Example usage
@Composable
fun UpdateToastExample() {
    var showToast by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { showToast = true },
        ) {
            Text("Show toast")
        }
    }

    if (showToast) {
        ToastContainer(position = ToastPosition.TOP) {
            UpdateToast(
                version = "2.1.0",
                onUpdate = {
                    println("Update started")
                    // Add update process here
                },
                onDismiss = {
                    showToast = false
                },
                duration = 5000L,
            )
        }
    }
}

// Singleton manager
object UpdateToastManager {
    private val _toastState = mutableStateOf<ToastData?>(null)
    val toastState: State<ToastData?> = _toastState

    data class ToastData(
        val version: String,
        val onUpdate: () -> Unit = {},
        val onDismiss: () -> Unit = {},
        val duration: Long = 5000L,
        val position: ToastPosition = ToastPosition.TOP,
    )

    fun show(
        version: String,
        onUpdate: () -> Unit = {},
        onDismiss: () -> Unit = {},
        duration: Long = 5000L,
        position: ToastPosition = ToastPosition.TOP,
    ) {
        _toastState.value =
            ToastData(
                version = version,
                onUpdate = onUpdate,
                onDismiss = {
                    dismiss()
                    onDismiss()
                },
                duration = duration,
                position = position,
            )
    }

    fun dismiss() {
        _toastState.value = null
    }

    val isShowing: Boolean
        get() = _toastState.value != null
}

// Global toast container
@Composable
fun GlobalToastContainer(content: @Composable () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        content()
        val toastData by UpdateToastManager.toastState
        toastData?.let { data ->
            ToastContainer(position = data.position) {
                UpdateToast(
                    version = data.version,
                    onUpdate = data.onUpdate,
                    onDismiss = data.onDismiss,
                    duration = data.duration,
                )
            }
        }
    }
}
