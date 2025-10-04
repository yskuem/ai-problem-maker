package app.yskuem.aimondaimaker.feature.show_project_info

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.create_new_note
import ai_problem_maker.composeapp.generated.resources.create_new_quiz
import ai_problem_maker.composeapp.generated.resources.delete_note
import ai_problem_maker.composeapp.generated.resources.delete_quiz
import ai_problem_maker.composeapp.generated.resources.last_updated_date
import ai_problem_maker.composeapp.generated.resources.no_note_info
import ai_problem_maker.composeapp.generated.resources.no_quiz_info
import ai_problem_maker.composeapp.generated.resources.note_tab_name
import ai_problem_maker.composeapp.generated.resources.quiz_tab_name
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.composable.BannerAd
import app.yskuem.aimondaimaker.core.ui.CreateNewButton
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.ui.EmptyProjectsUI
import app.yskuem.aimondaimaker.core.ui.ErrorScreen
import app.yskuem.aimondaimaker.core.ui.ErrorScreenType
import app.yskuem.aimondaimaker.core.ui.LoadingScreen
import app.yskuem.aimondaimaker.core.util.toJapaneseMonthDay
import app.yskuem.aimondaimaker.feature.ad.config.getAdmobBannerId
import app.yskuem.aimondaimaker.feature.note.ui.ShowNoteAppScreen
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.SelectAlbumOrCameraScreen
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.mode.NavCreateMode
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf
import kotlin.time.ExperimentalTime

data class ShowProjectInfoScreen(
    private val projectId: String,
) : Screen {
    @OptIn(
        ExperimentalMaterial3Api::class,
        DependsOnGoogleMobileAds::class,
        ExperimentalComposeUiApi::class,
        ExperimentalTime::class,
    )
    @Composable
    override fun Content() {
        val tabs =
            listOf(
                stringResource(Res.string.quiz_tab_name),
                stringResource(Res.string.note_tab_name),
            )
        val viewModel =
            koinScreenModel<ShowProjectInfoScreenViewModel>(
                parameters = { parametersOf(projectId) },
            )
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.current

        BackHandler {
            navigator?.pop()
        }

        // 初回表示と前の画面に戻ってきたときにデータフェッチ
        LaunchedEffect(navigator) {
            snapshotFlow { navigator?.lastEvent }
                .distinctUntilChanged()
                .filter { it == StackEvent.Idle }
                .collect {
                    viewModel.refreshQuizInfo()
                    viewModel.refreshNoteList()
                }
        }
        Scaffold(
            topBar = {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            onClick = {
                                navigator?.pop()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "戻る",
                            )
                        }
                    }
                    TabRow(
                        selectedTabIndex = uiState.selectedTabIndex,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp),
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
                                        fontWeight = if (uiState.selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    )
                                },
                            )
                        }
                    }
                }
            },
        ) { paddingValues ->
            // コンテンツエリア
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
            ) {
                when (uiState.selectedTabIndex) {
                    0 -> {
                        when (val quizInfoList = uiState.quizInfoList) {
                            is DataUiState.Loading -> {
                                LoadingScreen()
                            }
                            is DataUiState.Success -> {
                                if (quizInfoList.data.isEmpty()) {
                                    EmptyProjectsUI(
                                        message = stringResource(Res.string.no_quiz_info),
                                        modifier = Modifier.fillMaxSize(),
                                        iconVector = Icons.Default.QuestionAnswer,
                                    )
                                } else {
                                    ContentList(
                                        items = quizInfoList.data.map { it.name },
                                        icon = Icons.Filled.QuestionAnswer,
                                        contentType = ContentType.QUIZ,
                                        updateAtList =
                                            quizInfoList.data.map {
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
                                        },
                                        onDeleteItem = { groupId ->
                                            viewModel.deleteQuizInfo(groupId)
                                        },
                                    )
                                }
                                BottomContent(
                                    modifier = Modifier.align(alignment = Alignment.BottomEnd),
                                    buttonText = stringResource(Res.string.create_new_quiz),
                                ) {
                                    navigator?.push(
                                        SelectAlbumOrCameraScreen(
                                            navMode = NavCreateMode.Quiz,
                                            projectId = projectId,
                                        ),
                                    )
                                }
                            }
                            is DataUiState.Error -> {
                                ErrorScreen(
                                    type = ErrorScreenType.RELOAD,
                                    errorMessage = quizInfoList.throwable.message ?: "Unknown Error",
                                ) {
                                    viewModel.refreshQuizInfo()
                                }
                            }
                        }
                    }
                    1 -> {
                        when (val noteList = uiState.noteList) {
                            is DataUiState.Loading -> {
                                LoadingScreen()
                            }
                            is DataUiState.Success -> {
                                if (noteList.data.isEmpty()) {
                                    EmptyProjectsUI(
                                        message = stringResource(Res.string.no_note_info),
                                        modifier = Modifier.fillMaxSize(),
                                        iconVector = Icons.AutoMirrored.Filled.Assignment,
                                    )
                                } else {
                                    ContentList(
                                        items = noteList.data.map { it.title },
                                        icon = Icons.AutoMirrored.Filled.Assignment,
                                        contentType = ContentType.NOTE,
                                        updateAtList =
                                            noteList.data.map {
                                                it.updatedAt
                                                    .toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
                                                    .toJapaneseMonthDay()
                                            },
                                        itemGroupIds = noteList.data.map { it.id },
                                        onTapCard = { id ->
                                            val targetNote =
                                                noteList.data.first {
                                                    it.id == id
                                                }
                                            navigator?.push(ShowNoteAppScreen(targetNote))
                                        },
                                        onDeleteItem = { id ->
                                            viewModel.deleteNote(id)
                                        },
                                    )
                                }
                                BottomContent(
                                    modifier = Modifier.align(alignment = Alignment.BottomEnd),
                                    buttonText = stringResource(Res.string.create_new_note),
                                ) {
                                    navigator?.push(
                                        SelectAlbumOrCameraScreen(
                                            navMode = NavCreateMode.Note,
                                            projectId = projectId,
                                        ),
                                    )
                                }
                            }
                            is DataUiState.Error -> {
                                ErrorScreen(
                                    type = ErrorScreenType.RELOAD,
                                    errorMessage = noteList.throwable.message ?: "Unknown Error",
                                ) {
                                    viewModel.refreshNoteList()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContentList(
    items: List<String>,
    updateAtList: List<String>,
    itemGroupIds: List<String>,
    onTapCard: (String) -> Unit,
    onDeleteItem: (String) -> Unit,
    icon: ImageVector,
    contentType: ContentType,
) {
    var expandedMenuFor by remember { mutableStateOf<String?>(null) }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp), // 間隔を広げる
    ) {
        itemsIndexed(items) { index, item ->
            Card(
                onClick = {
                    onTapCard(itemGroupIds[index])
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp) // カードを大きくする
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp),
                        ),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface, // 白い背景を維持
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 3.dp, // 影を少し強くして立体感を出す
                    ),
                shape = RoundedCornerShape(12.dp), // 角を少し丸くする
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // アイコン部分を円形の背景で装飾
                    Box(
                        modifier =
                            Modifier
                                .size(48.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = CircleShape,
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (contentType == ContentType.QUIZ) "クイズ" else "ノート",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // テキスト
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item,
                            style =
                                TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface, // テキスト色をはっきりさせる
                                ),
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${stringResource(Res.string.last_updated_date)}: ${updateAtList[index]}",
                            style =
                                TextStyle(
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // もう少しはっきりした色に
                                ),
                        )
                    }

                    // メニューボタン
                    Box {
                        IconButton(onClick = {
                            expandedMenuFor = if (expandedMenuFor == itemGroupIds[index]) null else itemGroupIds[index]
                        }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "メニュー")
                        }
                        DropdownMenu(
                            expanded = (expandedMenuFor == itemGroupIds[index]),
                            onDismissRequest = { expandedMenuFor = null },
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        stringResource(
                                            if (contentType == ContentType.QUIZ) {
                                                Res.string.delete_quiz
                                            } else {
                                                Res.string.delete_note
                                            },
                                        ),
                                    )
                                },
                                onClick = {
                                    onDeleteItem(itemGroupIds[index])
                                    expandedMenuFor = null
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(DependsOnGoogleMobileAds::class)
@Composable
private fun BottomContent(
    modifier: Modifier,
    buttonText: String,
    onTapButton: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        CreateNewButton(
            buttonText = buttonText,
        ) {
            onTapButton()
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = Color.White),
            contentAlignment = Alignment.Center,
        ) {
            BannerAd(
                adUnitId = getAdmobBannerId(),
            )
        }
    }
}

enum class ContentType {
    QUIZ,
    NOTE,
}
