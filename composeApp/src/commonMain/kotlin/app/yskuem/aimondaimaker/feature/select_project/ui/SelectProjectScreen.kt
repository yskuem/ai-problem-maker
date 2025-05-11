package app.yskuem.aimondaimaker.feature.select_project.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.feature.show_project_info.ShowProjectInfoScreen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

class SelectProjectScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        // どのプロジェクトのメニューが開いているか
        var expandedMenuFor by remember { mutableStateOf<String?>(null) }
        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<SelectProjectScreenViewModel>()

        var searchTerm by remember { mutableStateOf("") }
        var editingId by remember { mutableStateOf<String?>(null) }
        var editingTitle by remember { mutableStateOf("") }

        // フォーカス用リクエスタ（1つだけでOK）
        val focusRequester = remember { FocusRequester() }

        val projectState by viewModel.projects.collectAsState()

        // フォーカス取得のトリガー
        LaunchedEffect(editingId) {
            if (editingId != null) {
                focusRequester.requestFocus()
            }
        }
        val gridState = rememberLazyGridState()

        Scaffold { padding ->
            when(val projectState = projectState) {
                is DataUiState.Loading -> {
                    // TODO まとめる
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                is DataUiState.Error -> {
                    // TODO まとめる
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        Text(
                            text = "エラーが発生しました: ${projectState.throwable.message}",
                            modifier = Modifier.align(Alignment.Center),
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                is DataUiState.Success -> {
                    val projects = projectState.data
                    val filtered = projects.filter {
                        it.name.contains(searchTerm, ignoreCase = true)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFF9FAFB))
                                .padding(padding)
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(30.dp))

                            // 検索バー
                            OutlinedTextField(
                                value = searchTerm,
                                onValueChange = { searchTerm = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                placeholder = { Text("プロジェクトを検索...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { /* 検索実行 */ })
                            )

                            // プロジェクトグリッド
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(1),
                                state = gridState,
                                contentPadding = PaddingValues(4.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                items(filtered) { project ->
                                    Card(
                                        onClick = {
                                            navigator?.push(ShowProjectInfoScreen())
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        elevation = 4.dp,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // アイコン
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .background(
                                                        color = Color(0xFFE0F2FF),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(24.dp),
                                                    tint = Color(0xFF3B82F6)
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
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .focusRequester(focusRequester)
                                                                .padding(end = 8.dp),
                                                            singleLine = true,
                                                            colors = TextFieldDefaults.textFieldColors(
                                                                backgroundColor = Color(0xFFE0F2FF)
                                                            )
                                                        )
                                                        IconButton(onClick = {
                                                            if (editingTitle.isNotBlank()) {
                                                                // TODO 名前の変更ロジック
//                                                                projects = projects.map {
//                                                                    if (it.id == editingId) {
//                                                                        it.copy(
//                                                                            title = editingTitle.trim(),
//                                                                            lastEdited = "2025年4月22日"
//                                                                        )
//                                                                    } else it
//                                                                }
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
                                                        color = MaterialTheme.colors.onSurface
                                                    )
                                                    Text(
                                                        // TODO わかりやすい日付フォーマットにする
                                                        text = "最終編集: ${project.updatedAt}",
                                                        fontSize = 12.sp,
                                                        color = Color.Gray
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
                                                    onDismissRequest = { expandedMenuFor = null }
                                                ) {
                                                    DropdownMenuItem(onClick = {
                                                        // 編集モード開始
                                                        editingId = project.id
                                                        editingTitle = project.name
                                                        expandedMenuFor = null
                                                    }) {
                                                        Text("名前を変更")
                                                    }
                                                    // ここに他のメニュー項目を追加可能
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // 新規プロジェクト作成ボタン
                            Button(
                                onClick = {
                                    // 遷移処理
                                    navigator?.push(SelectNoteOrQuizScreen())
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("新規プロジェクト")
                            }
                        }
                    }
                }
            }
        }
    }
}
