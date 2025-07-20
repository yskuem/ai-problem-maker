package app.yskuem.aimondaimaker.feature.select_project.ui

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.change_project_name
import ai_problem_maker.composeapp.generated.resources.delete_project
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.yskuem.aimondaimaker.core.ui.theme.ComponentSpacing
import app.yskuem.aimondaimaker.core.ui.theme.Spacing
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
    @OptIn(DependsOnGoogleMobileAds::class)
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

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
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
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    focusManager.clearFocus()
                                }
                            },
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(padding)
                                .padding(ComponentSpacing.screenPadding),
                        ) {
                            Spacer(modifier = Modifier.height(ComponentSpacing.screenTopPadding))

                            // Modern search bar
                            OutlinedTextField(
                                value = searchTerm,
                                onValueChange = { searchTerm = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = ComponentSpacing.fieldSpacing),
                                placeholder = { 
                                    Text(
                                        stringResource(Res.string.search_project),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                leadingIcon = { 
                                    Icon(
                                        Icons.Default.Search, 
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                singleLine = true,
                                shape = MaterialTheme.shapes.medium,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                textStyle = MaterialTheme.typography.bodyMedium,
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
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
                                // Modern project grid
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(1),
                                    state = gridState,
                                    contentPadding = PaddingValues(Spacing.xs),
                                    verticalArrangement = Arrangement.spacedBy(ComponentSpacing.cardSpacing),
                                    modifier = Modifier
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
                                            shape = MaterialTheme.shapes.medium,
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = Spacing.xs,
                                                pressedElevation = Spacing.sm
                                            ),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surface,
                                                contentColor = MaterialTheme.colorScheme.onSurface
                                            ),
                                            modifier = Modifier
                                                .padding(horizontal = Spacing.xs)
                                                .fillMaxWidth(),
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .padding(ComponentSpacing.cardPadding)
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                // Modern icon with theme colors
                                                Box(
                                                    modifier = Modifier
                                                        .size(ComponentSpacing.iconXLarge)
                                                        .background(
                                                            color = MaterialTheme.colorScheme.primaryContainer,
                                                            shape = MaterialTheme.shapes.medium,
                                                        ),
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(ComponentSpacing.iconMedium),
                                                        tint = MaterialTheme.colorScheme.primary,
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(ComponentSpacing.listIconSpacing))

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
                                                                colors = TextFieldDefaults.colors(
                                                                    focusedContainerColor = Color(0xFFE0F2FF),
                                                                    unfocusedContainerColor = Color(0xFFE0F2FF),
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
                                                        // Modern styled text
                                                        Text(
                                                            text = project.name,
                                                            style = MaterialTheme.typography.titleMedium,
                                                            color = MaterialTheme.colorScheme.onSurface,
                                                        )
                                                        Text(
                                                            text = stringResource(Res.string.last_updated_project_date) + updatedAt,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                                        DropdownMenuItem(
                                                            text = { Text(stringResource(Res.string.change_project_name)) },
                                                            onClick = {
                                                                // 編集モード開始
                                                                editingId = project.id
                                                                editingTitle = project.name
                                                                expandedMenuFor = null
                                                            },
                                                            modifier = Modifier,
                                                            leadingIcon = null,
                                                            trailingIcon = null,
                                                            enabled = true,
                                                            colors = MenuDefaults.itemColors(),
                                                            contentPadding = MenuDefaults.DropdownMenuItemContentPadding,
                                                            interactionSource = null
                                                        )
                                                        DropdownMenuItem(
                                                            text = { Text(stringResource(Res.string.delete_project)) },
                                                            onClick = {
                                                                // プロジェクトを削除
                                                                viewModel.deleteProject(
                                                                    projectId = project.id,
                                                                    currentProjects = projects,
                                                                )
                                                                expandedMenuFor = null
                                                            },
                                                            modifier = Modifier,
                                                            leadingIcon = null,
                                                            trailingIcon = null,
                                                            enabled = true,
                                                            colors = MenuDefaults.itemColors(),
                                                            contentPadding = MenuDefaults.DropdownMenuItemContentPadding,
                                                            interactionSource = null
                                                        )
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
                                    modifier = Modifier.padding(top = ComponentSpacing.sectionSpacing)
                                ) {
                                    navigator.push(
                                        SelectNoteOrQuizScreen(),
                                    )
                                }
                                Spacer(modifier = Modifier.height(Spacing.md))
                                // Modern ad container
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = MaterialTheme.shapes.medium,
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .padding(Spacing.xs),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        BannerAd(
                                            adUnitId = getAdmobBannerId(),
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
