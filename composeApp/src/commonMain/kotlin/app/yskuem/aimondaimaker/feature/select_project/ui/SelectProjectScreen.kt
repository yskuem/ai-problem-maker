package app.yskuem.aimondaimaker.feature.select_project.ui

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.change_project_name
import ai_problem_maker.composeapp.generated.resources.last_updated_project_date
import ai_problem_maker.composeapp.generated.resources.new_project
import ai_problem_maker.composeapp.generated.resources.no_project_message
import ai_problem_maker.composeapp.generated.resources.search_project
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.lexilabs.basic.ads.BannerAd
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.yskuem.aimondaimaker.core.ui.CreateNewButton
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.ui.EmptyProjectsUI
import app.yskuem.aimondaimaker.core.ui.ErrorScreen
import app.yskuem.aimondaimaker.core.ui.ErrorScreenType
import app.yskuem.aimondaimaker.core.ui.LoadingScreen
import app.yskuem.aimondaimaker.core.util.toJapaneseMonthDay
import app.yskuem.aimondaimaker.feature.ad.config.getAdmobBannerId
import app.yskuem.aimondaimaker.feature.show_project_info.ShowProjectInfoScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

class SelectProjectScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class, DependsOnGoogleMobileAds::class)
    @Composable
    override fun Content() {
        // どのプロジェクトのメニューが開いているか
        var expandedMenuFor by remember { mutableStateOf<String?>(null) }
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<SelectProjectScreenViewModel>()

        var searchTerm by remember { mutableStateOf("") }
        var editingId by remember { mutableStateOf<String?>(null) }
        var editingTitle by remember { mutableStateOf("") }

        // フォーカス用リクエスタ（1つだけでOK）
        val focusRequester = remember { FocusRequester() }

        // フォーカスマネージャーを取得
        val focusManager = LocalFocusManager.current

        val uiState by viewModel.projects.collectAsState()

        // 初回表示と前の画面に戻ってきたときにデータフェッチ
        LaunchedEffect(navigator) {
            snapshotFlow { navigator.lastEvent }
                .distinctUntilChanged()
                .filter { it == StackEvent.Idle }
                .collect {
                    viewModel.refreshProjectList()
                }
        }

        // フォーカス取得のトリガー
        LaunchedEffect(editingId) {
            if (editingId != null) {
                focusRequester.requestFocus()
            }
        }
        val gridState = rememberLazyGridState()

        Scaffold { padding ->
            when (val projectState = uiState) {
                is DataUiState.Loading -> {
                    LoadingScreen()
                }
                is DataUiState.Error -> {
                    ErrorScreen(
                        type = ErrorScreenType.RELOAD,
                    ) {
                        viewModel.refreshProjectList()
                    }
                }
                is DataUiState.Success -> {
                    val projects = projectState.data
                    val filtered =
                        projects.filter {
                            it.name.contains(searchTerm, ignoreCase = true)
                        }
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .systemBarsPadding()
                                .pointerInput(Unit) {
                                    // 画面全体のタップを検知してフォーカスをクリア
                                    detectTapGestures {
                                        focusManager.clearFocus()
                                    }
                                },
                    ) {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFF9FAFB))
                                    .padding(padding)
                                    .padding(16.dp),
                        ) {
                            Spacer(modifier = Modifier.height(30.dp))

                            // 検索バー
                            OutlinedTextField(
                                value = searchTerm,
                                onValueChange = { searchTerm = it },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                placeholder = { Text(stringResource(Res.string.search_project)) },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                                keyboardActions =
                                    KeyboardActions(
                                        onSearch = {
                                            // 検索実行時にフォーカスをクリア
                                            focusManager.clearFocus()
                                        },
                                    ),
                            )

                            if (projects.isEmpty()) {
                                EmptyProjectsUI(
                                    modifier = Modifier.weight(1f),
                                    message = stringResource(Res.string.no_project_message),
                                    iconVector = Icons.AutoMirrored.Filled.MenuBook,
                                )
                            } else {
                                // プロジェクトグリッド
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(1),
                                    state = gridState,
                                    contentPadding = PaddingValues(4.dp),
                                    modifier =
                                        Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                ) {
                                    items(filtered) { project ->
                                        // 最終更新日
                                        val updatedAt =
                                            project.updatedAt
                                                .toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
                                                .toJapaneseMonthDay()
                                        Card(
                                            onClick = {
                                                navigator.push(
                                                    ShowProjectInfoScreen(
                                                        projectId = project.id,
                                                    ),
                                                )
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            elevation = 4.dp,
                                            modifier =
                                                Modifier
                                                    .padding(8.dp)
                                                    .fillMaxWidth(),
                                        ) {
                                            Row(
                                                modifier =
                                                    Modifier
                                                        .padding(12.dp)
                                                        .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                // アイコン
                                                Box(
                                                    modifier =
                                                        Modifier
                                                            .size(40.dp)
                                                            .background(
                                                                color = Color(0xFFE0F2FF),
                                                                shape = RoundedCornerShape(8.dp),
                                                            ),
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(24.dp),
                                                        tint = Color(0xFF3B82F6),
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(12.dp))

                                                // タイトル＆最終編集日
                                                Column(modifier = Modifier.weight(1f)) {
                                                    if (editingId == project.id) {
                                                        // 編集モード
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            TextField(
                                                                value = editingTitle,
                                                                onValueChange = { editingTitle = it },
                                                                modifier =
                                                                    Modifier
                                                                        .weight(1f)
                                                                        .focusRequester(focusRequester)
                                                                        .padding(end = 8.dp),
                                                                singleLine = true,
                                                                colors =
                                                                    TextFieldDefaults.textFieldColors(
                                                                        backgroundColor = Color(0xFFE0F2FF),
                                                                    ),
                                                            )
                                                            IconButton(onClick = {
                                                                if (editingTitle.isNotBlank()) {
                                                                    viewModel.editProject(
                                                                        currentProjects = projects,
                                                                        targetProject =
                                                                            project.copy(
                                                                                name = editingTitle.trim(),
                                                                                updatedAt = Clock.System.now(),
                                                                            ),
                                                                    )
                                                                }
                                                                editingId = null
                                                                editingTitle = ""
                                                            }) {
                                                                Icon(Icons.Default.Check, contentDescription = "Save")
                                                            }
                                                            IconButton(onClick = {
                                                                editingId = null
                                                                editingTitle = ""
                                                            }) {
                                                                Icon(Icons.Default.Close, contentDescription = "Cancel")
                                                            }
                                                        }
                                                    } else {
                                                        // 通常モード
                                                        Text(
                                                            text = project.name,
                                                            fontSize = 16.sp,
                                                            color = MaterialTheme.colors.onSurface,
                                                        )
                                                        Text(
                                                            text = stringResource(Res.string.last_updated_project_date) + updatedAt,
                                                            fontSize = 12.sp,
                                                            color = Color.Gray,
                                                        )
                                                    }
                                                }

                                                // メニューボタン
                                                Box {
                                                    IconButton(onClick = {
                                                        expandedMenuFor =
                                                            if (expandedMenuFor == project.id) null else project.id
                                                    }) {
                                                        Icon(Icons.Default.MoreVert, contentDescription = "メニュー")
                                                    }
                                                    DropdownMenu(
                                                        expanded = (expandedMenuFor == project.id),
                                                        onDismissRequest = { expandedMenuFor = null },
                                                    ) {
                                                        DropdownMenuItem(onClick = {
                                                            // 編集モード開始
                                                            editingId = project.id
                                                            editingTitle = project.name
                                                            expandedMenuFor = null
                                                        }) {
                                                            Text(stringResource(Res.string.change_project_name))
                                                        }
                                                        // ここに他のメニュー項目を追加可能
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Column {
                                CreateNewButton(
                                    buttonText = stringResource(Res.string.new_project),
                                ) {
                                    navigator.push(
                                        SelectNoteOrQuizScreen(),
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                var bannerLoaded by remember { mutableStateOf(false) }
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(if (bannerLoaded) 50.dp else 0.dp)
                                            .background(
                                                color = if (bannerLoaded) Color.White else Color.Transparent,
                                            ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    BannerAd(
                                        adUnitId = getAdmobBannerId(),
                                        onLoad = { bannerLoaded = true },
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
