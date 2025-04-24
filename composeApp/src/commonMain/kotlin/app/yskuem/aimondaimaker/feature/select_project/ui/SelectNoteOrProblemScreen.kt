package app.yskuem.aimondaimaker.feature.select_project.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator

class SelectNoteOrProblemScreen: Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF0F4FF),
                            Color(0xFFE0E8FF)
                        )
                    )
                ),
        ){
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.Black,
                            navigationIconContentColor = Color.Black,
                            actionIconContentColor = Color.Black
                        ),
                        title = { Text("モードの選択") },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navigator?.pop()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "戻る"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                // 既存の StudyModeSelector を表示
                Box(modifier = Modifier.padding(innerPadding)) {
                    StudyModeSelector()
                }
            }
        }
    }


    @Composable
    private fun StudyModeSelector() {
        var hoveredCard by remember { mutableStateOf<String?>(null) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // タイトルセクション
                Column(
                    modifier = Modifier.padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "モードの選択",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3730A3)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "画像からあなたの学習をサポートします",
                        fontSize = 16.sp,
                        color = Color(0xFF6B7280)
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // カードセクション - 縦に並べる
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    // クイズ作成カード
                    FeatureCard(
                        title = "画像からクイズを作成",
                        description = "ノートや教科書の画像をアップロードして、内容に基づいた確認テストを自動生成します。",
                        icon = Icons.Filled.Psychology,
                        isHovered = hoveredCard == "quiz",
                        onHoverChange = { isHovered ->
                            hoveredCard = if (isHovered) "quiz" else null
                        },
                        onClick = {
                            // クイズ作成機能に遷移するロジック
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 要約作成カード
                    FeatureCard(
                        title = "画像からノートを要約",
                        description = "ノートや参考書の画像から重要なポイントを抽出し、要約とまとめを作成します。",
                        icon = Icons.Filled.Book,
                        isHovered = hoveredCard == "summary",
                        onHoverChange = { isHovered ->
                            hoveredCard = if (isHovered) "summary" else null
                        },
                        onClick = {
                            // ノート要約機能に遷移するロジック
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    @Composable
    private fun FeatureCard(
        title: String,
        description: String,
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        isHovered: Boolean,
        onHoverChange: (Boolean) -> Unit,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val elevation by animateDpAsState(targetValue = if (isHovered) 8.dp else 4.dp, label = "")
        val translationY by animateDpAsState(targetValue = if (isHovered) (-8).dp else 0.dp, label = "")

        Box(
            modifier = modifier
                .offset(y = translationY)
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(
                    width = if (isHovered) 2.dp else 0.dp,
                    color = if (isHovered) Color(0xFF818CF8) else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onClick() }
                .padding(24.dp)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Enter -> onHoverChange(true)
                                PointerEventType.Exit -> onHoverChange(false)
                            }
                        }
                    }
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // アイコン
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEF2FF))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // タイトル
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 説明文
                    Text(
                        text = description,
                        fontSize = 16.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }

    // デスクトップ向けのホバー検出（必要に応じて使用）
    @Composable
    private fun rememberIsHovered(): Pair<MutableInteractionSource, Boolean> {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered = interactionSource.collectIsHoveredAsState().value
        return interactionSource to isHovered
    }
}