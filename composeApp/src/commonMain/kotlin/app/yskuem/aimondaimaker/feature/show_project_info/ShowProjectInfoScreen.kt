package app.yskuem.aimondaimaker.feature.show_project_info

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.create_new_note
import ai_problem_maker.composeapp.generated.resources.create_new_quiz
import ai_problem_maker.composeapp.generated.resources.note_tab_name
import ai_problem_maker.composeapp.generated.resources.quiz_tab_name
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.yskuem.aimondaimaker.core.ui.CreateNewButton
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.util.toJapaneseMonthDay
import app.yskuem.aimondaimaker.feature.note.ui.ShowNoteAppScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf


data class ShowProjectInfoScreen(
    private val projectId: String,
): Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val tabs = listOf(
            stringResource(Res.string.quiz_tab_name),
            stringResource(Res.string.note_tab_name),
        )
        val viewModel = koinScreenModel<ShowProjectInfoScreenViewModel>(
            parameters = { parametersOf(projectId) }
        )
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.current
        Scaffold(
            topBar = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                    TabRow(
                        selectedTabIndex = uiState.selectedTabIndex,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = uiState.selectedTabIndex == index,
                                onClick = {
                                    viewModel.onTapTab(index)
                                },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 16.sp,
                                        fontWeight = if (uiState.selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            // コンテンツエリア
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                when (uiState.selectedTabIndex) {
                    0 -> {
                        when (val quizInfoList = uiState.quizInfoList) {
                            is DataUiState.Loading -> {
                                LoadingContent()
                            }
                            is DataUiState.Success -> {
                                ContentList(
                                    items = quizInfoList.data.map { it.name },
                                    icon = Icons.Filled.QuestionAnswer,
                                    contentType = ContentType.QUIZ,
                                    updateAtList = quizInfoList.data.map {
                                        it.updatedAt
                                            .toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
                                            .toJapaneseMonthDay()
                                    },
                                    itemGroupIds = quizInfoList.data.map { it.groupId },
                                    onTapCard = { groupId ->
                                        viewModel.onTapQuizInfo(
                                            groupId = groupId,
                                            navigator = navigator,
                                        )
                                    }
                                )
                                CreateNewButton(
                                    buttonText = stringResource(Res.string.create_new_quiz),
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                ) {

                                }
                            }
                            is DataUiState.Error -> {
                                ErrorContent(message = quizInfoList.throwable.message)
                            }
                        }
                    }
                    1 -> {
                        when (val noteList = uiState.noteList) {
                            is DataUiState.Loading -> {
                                LoadingContent()
                            }
                            is DataUiState.Success -> {
                                ContentList(
                                    items = noteList.data.map { it.title },
                                    icon = Icons.AutoMirrored.Filled.Assignment,
                                    contentType = ContentType.NOTE,
                                    updateAtList = noteList.data.map {
                                        it.updatedAt
                                            .toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
                                            .toJapaneseMonthDay()
                                    },
                                    itemGroupIds = noteList.data.map { it.id },
                                    onTapCard = { id ->
                                        val targetNote = noteList.data.first {
                                            it.id == id
                                        }
                                        navigator?.push(ShowNoteAppScreen(targetNote))
                                    }
                                )
                                CreateNewButton(
                                    buttonText = stringResource(Res.string.create_new_note),
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                ) {

                                }
                            }
                            is DataUiState.Error -> {
                                ErrorContent(message = noteList.throwable.message)
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class ContentType {
    QUIZ, NOTE
}

@Composable
fun LoadingContent() {
    // TODO まとめる
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorContent(message: String?) {
    // TODO まとめる
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "エラーが発生しました: ${message ?: "不明なエラー"}")
    }
}

@Composable
fun ContentList(
    items: List<String>,
    updateAtList: List<String>,
    itemGroupIds: List<String>,
    onTapCard: (String) -> Unit,
    icon: ImageVector,
    contentType: ContentType
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp) // 間隔を広げる
    ) {
        itemsIndexed(items) { index, item ->
            Card(
                onClick = {
                    onTapCard(itemGroupIds[index])
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // カードを大きくする
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface, // 白い背景を維持
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp // 影を少し強くして立体感を出す
                ),
                shape = RoundedCornerShape(12.dp) // 角を少し丸くする
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // アイコン部分を円形の背景で装飾
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (contentType == ContentType.QUIZ) "クイズ" else "ノート",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // テキスト
                    Column {
                        Text(
                            text = item,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface // テキスト色をはっきりさせる
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "最終更新: ${updateAtList[index]}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // もう少しはっきりした色に
                            )
                        )
                    }
                }
            }
        }
    }
}