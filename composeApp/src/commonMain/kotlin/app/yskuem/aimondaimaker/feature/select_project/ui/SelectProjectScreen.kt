package app.yskuem.aimondaimaker.feature.select_project.ui

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.cancel
import ai_problem_maker.composeapp.generated.resources.change_project_name
import ai_problem_maker.composeapp.generated.resources.confirm_delete_project_message
import ai_problem_maker.composeapp.generated.resources.confirm_delete_project_title
import ai_problem_maker.composeapp.generated.resources.delete_project
import ai_problem_maker.composeapp.generated.resources.last_updated_project_date
import ai_problem_maker.composeapp.generated.resources.new_project
import ai_problem_maker.composeapp.generated.resources.no_project_message
import ai_problem_maker.composeapp.generated.resources.search_project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.composable.BannerAd
import app.yskuem.aimondaimaker.core.ui.CreateNewButton
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.ui.EmptyProjectsUI
import app.yskuem.aimondaimaker.core.ui.ErrorScreen
import app.yskuem.aimondaimaker.core.ui.ErrorScreenType
import app.yskuem.aimondaimaker.core.ui.LoadingScreen
import app.yskuem.aimondaimaker.core.ui.components.PremiumCard
import app.yskuem.aimondaimaker.core.ui.components.PremiumSearchBar
import app.yskuem.aimondaimaker.core.ui.theme.*
import app.yskuem.aimondaimaker.core.util.toLocalizedMonthDay
import app.yskuem.aimondaimaker.domain.entity.Project
import app.yskuem.aimondaimaker.feature.ad.config.getAdmobBannerId
import app.yskuem.aimondaimaker.feature.settings.SettingsScreen
import app.yskuem.aimondaimaker.feature.show_project_info.ShowProjectInfoScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SelectProjectScreen : Screen {
    @OptIn(DependsOnGoogleMobileAds::class, ExperimentalTime::class)
    @Composable
    override fun Content() {
        // どのプロジェクトのメニューが開いているか
        var expandedMenuFor by remember { mutableStateOf<String?>(null) }
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<SelectProjectScreenViewModel>()

        var searchTerm by remember { mutableStateOf("") }
        var editingId by remember { mutableStateOf<String?>(null) }
        var editingTitle by remember { mutableStateOf("") }
        var projectToDelete by remember { mutableStateOf<Project?>(null) }

        // フォーカス用リクエスタ（1つだけでOK）
        val focusRequester = remember { FocusRequester() }

        // フォーカスマネージャーを取得
        val focusManager = LocalFocusManager.current

        val uiState by viewModel.projects.collectAsState()

        val isSubscribed by viewModel.isSubscribed.collectAsState()

        val isLoadingMore by viewModel.isLoadingMore.collectAsState()
        val hasMoreData by viewModel.hasMoreData.collectAsState()

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
            containerColor = BackgroundPrimary,
        ) { padding ->
            when (val projectState = uiState) {
                is DataUiState.Initial -> {}

                is DataUiState.Loading -> {
                    LoadingScreen()
                }

                is DataUiState.Error -> {
                    ErrorScreen(
                        type = ErrorScreenType.RELOAD,
                        errorMessage =
                            projectState.throwable.message
                                ?: "unknown error",
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
                                .background(
                                    brush =
                                        Brush.verticalGradient(
                                            listOf(
                                                BackgroundPrimary,
                                                BackgroundGradient,
                                                BackgroundSecondary,
                                            ),
                                        ),
                                ).systemBarsPadding()
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        focusManager.clearFocus()
                                    }
                                },
                    ) {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                                    .padding(ComponentSpacing.screenPadding),
                        ) {

                            // Search bar + Settings icon row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = ComponentSpacing.fieldSpacing),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                            ) {
                                PremiumSearchBar(
                                    value = searchTerm,
                                    onValueChange = { searchTerm = it },
                                    placeholder = stringResource(Res.string.search_project),
                                    modifier = Modifier.weight(1f),
                                    onSearch = {
                                        focusManager.clearFocus()
                                    },
                                )

                                // Glass-morphism settings icon
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .shadow(
                                            elevation = Elevation.sm,
                                            shape = RoundedCornerShape(CornerRadius.xl),
                                            ambientColor = ShadowLight,
                                            spotColor = ShadowMedium,
                                        )
                                        .clip(RoundedCornerShape(CornerRadius.xl))
                                        .background(
                                            brush = Brush.verticalGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.surface,
                                                    MaterialTheme.colorScheme.surfaceContainer,
                                                ),
                                            ),
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = BorderAccent,
                                            shape = RoundedCornerShape(CornerRadius.xl),
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    IconButton(
                                        onClick = { navigator.push(SettingsScreen()) },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "Settings",
                                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                            modifier = Modifier.size(22.dp),
                                        )
                                    }
                                }
                            }

                            if (projects.isEmpty()) {
                                EmptyProjectsUI(
                                    modifier = Modifier.weight(1f),
                                    message = stringResource(Res.string.no_project_message),
                                    iconVector = Icons.AutoMirrored.Filled.MenuBook,
                                )
                            } else {
                                // Premium project grid with sophisticated cards
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(1),
                                    state = gridState,
                                    contentPadding = PaddingValues(Spacing.sm),
                                    verticalArrangement = Arrangement.spacedBy(Spacing.lg),
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
                                                .toLocalizedMonthDay()
                                        PremiumCard(
                                            onClick = {
                                                navigator.push(
                                                    ShowProjectInfoScreen(
                                                        projectId = project.id,
                                                    ),
                                                )
                                            },
                                            elevation = Elevation.md,
                                            gradientColors =
                                                listOf(
                                                    MaterialTheme.colorScheme.surface,
                                                    MaterialTheme.colorScheme.surfaceContainerLow,
                                                ),
                                            borderGradient =
                                                listOf(
                                                    BorderAccent,
                                                    BorderAccent.copy(alpha = 0.3f),
                                                ),
                                            modifier =
                                                Modifier
                                                    .padding(horizontal = Spacing.xs)
                                                    .fillMaxWidth(),
                                        ) {
                                            Row(
                                                modifier =
                                                    Modifier
                                                        .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                // Premium icon with gradient background
                                                Box(
                                                    modifier =
                                                        Modifier
                                                            .size(ComponentSpacing.iconXXLarge)
                                                            .shadow(
                                                                elevation = Elevation.sm,
                                                                shape = RoundedCornerShape(CornerRadius.lg),
                                                                ambientColor = ShadowBrand,
                                                                spotColor = ShadowLight,
                                                            ).background(
                                                                brush =
                                                                    Brush.linearGradient(
                                                                        listOf(
                                                                            BrandPrimary.copy(alpha = 0.9f),
                                                                            BrandSecondary.copy(alpha = 0.8f),
                                                                        ),
                                                                    ),
                                                                shape = RoundedCornerShape(CornerRadius.lg),
                                                            ).border(
                                                                width = 1.dp,
                                                                color = Color.White.copy(alpha = 0.2f),
                                                                shape = RoundedCornerShape(CornerRadius.lg),
                                                            ),
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(ComponentSpacing.iconLarge),
                                                        tint = Color.White,
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
                                                                colors =
                                                                    TextFieldDefaults.colors(
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
                                                        // Premium styled text with enhanced typography
                                                        Text(
                                                            text = project.name,
                                                            style = MaterialTheme.typography.titleLarge,
                                                            color = TextPrimary,
                                                        )
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Text(
                                                            text = stringResource(Res.string.last_updated_project_date) + updatedAt,
                                                            style =
                                                                MaterialTheme.typography.bodySmall.copy(
                                                                    color = TextTertiary,
                                                                ),
                                                        )
                                                    }
                                                }

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
                                                            interactionSource = null,
                                                        )
                                                        DropdownMenuItem(
                                                            text = { Text(stringResource(Res.string.delete_project)) },
                                                            onClick = {
                                                                projectToDelete = project
                                                                expandedMenuFor = null
                                                            },
                                                            modifier = Modifier,
                                                            leadingIcon = null,
                                                            trailingIcon = null,
                                                            enabled = true,
                                                            colors = MenuDefaults.itemColors(),
                                                            contentPadding = MenuDefaults.DropdownMenuItemContentPadding,
                                                            interactionSource = null,
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // Loading more indicator
                                    if (isLoadingMore) {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                contentAlignment = Alignment.Center,
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(24.dp),
                                                    strokeWidth = 2.dp,
                                                )
                                            }
                                        }
                                    }
                                }

                                // Detect scroll to end for pagination
                                LaunchedEffect(gridState) {
                                    snapshotFlow {
                                        val layoutInfo = gridState.layoutInfo
                                        val totalItems = layoutInfo.totalItemsCount
                                        val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                        lastVisibleItem >= totalItems - 3
                                    }.distinctUntilChanged()
                                        .collect { isNearEnd ->
                                            if (isNearEnd) {
                                                viewModel.fetchMoreProjects()
                                            }
                                        }
                                }
                            }

                            Column {
                                CreateNewButton(
                                    buttonText = stringResource(Res.string.new_project),
                                    modifier = Modifier.padding(top = ComponentSpacing.sectionSpacing),
                                ) {
                                    navigator.push(
                                        SelectNoteOrQuizScreen(),
                                    )
                                }
                                Spacer(modifier = Modifier.height(Spacing.md))

                                if (!isSubscribed) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .shadow(
                                                    elevation = Elevation.xs,
                                                    shape = RoundedCornerShape(CornerRadius.lg),
                                                    ambientColor = GlassShadow,
                                                    spotColor = GlassShadow,
                                                ).clip(RoundedCornerShape(CornerRadius.lg))
                                                .background(GlassSurface)
                                                .border(
                                                    width = 1.dp,
                                                    color = GlassBorder,
                                                    shape = RoundedCornerShape(CornerRadius.lg),
                                                ),
                                    ) {
                                        Box(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(60.dp)
                                                    .padding(Spacing.sm),
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
                        projectToDelete?.let { pendingProject ->
                            AlertDialog(
                                onDismissRequest = { projectToDelete = null },
                                title = {
                                    Text(text = stringResource(Res.string.confirm_delete_project_title))
                                },
                                text = {
                                    Text(
                                        text =
                                            stringResource(
                                                Res.string.confirm_delete_project_message,
                                                pendingProject.name,
                                            ),
                                    )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            viewModel.deleteProject(
                                                projectId = pendingProject.id,
                                                currentProjects = projects,
                                            )
                                            projectToDelete = null
                                        },
                                    ) {
                                        Text(text = stringResource(Res.string.delete_project))
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { projectToDelete = null }) {
                                        Text(text = stringResource(Res.string.cancel))
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
