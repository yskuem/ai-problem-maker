package app.yskuem.aimondaimaker.feature.problem.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.yskuem.aimondaimaker.feature.problem.viewmodel.ProblemScreenViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel

class ProblemScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val scaffoldState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val problemScreenModel = koinScreenModel<ProblemScreenViewModel> ()
        val state by problemScreenModel.uiState.collectAsState()
        var selectedItem by remember { mutableStateOf(0) }
        val items = listOf("ホーム", "履歴", "設定")
        val icons = listOf(
            Icons.Outlined.Camera,
            Icons.Outlined.History,
            Icons.Outlined.Settings
        )

        val containerColor = MaterialTheme.colorScheme.primary
        val onContainerColor = MaterialTheme.colorScheme.onPrimary

        ModalNavigationDrawer(
            drawerState = scaffoldState,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(containerColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "AI問題作成",
                                    color = onContainerColor,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "あなたの学習をサポート",
                                    color = onContainerColor.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        items.forEachIndexed { index, item ->
                            NavigationDrawerItem(
                                icon = { Icon(icons[index], contentDescription = item) },
                                label = { Text(item) },
                                selected = index == selectedItem,
                                onClick = {
                                    selectedItem = index
                                },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("AI問題作成") },
                        navigationIcon = {
                            IconButton(onClick = {
                                // ドロワーを開く
                            }) {
                                Icon(Icons.Filled.Menu, contentDescription = "メニュー")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                },
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Camera,
                            contentDescription = "カメラアイコン",
                            modifier = Modifier.size(120.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "教材やノートを撮影して\nAIに問題を作成してもらいましょう",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { /* カメラを起動 */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Camera,
                                contentDescription = "カメラ"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("写真を撮影", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedButton(
                            onClick = {
                                problemScreenModel.onSelectImage()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("ギャラリーから選択", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(48.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "使い方",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "1. 教材やノートの写真を撮影\n" +
                                            "2. AIが自動的に問題を生成\n" +
                                            "3. 生成された問題を解いて学習しましょう",
                                    lineHeight = 24.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
