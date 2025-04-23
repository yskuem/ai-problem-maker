package app.yskuem.aimondaimaker.feature.select_project.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator

class SelectProjectScreen : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        // どのプロジェクトのメニューが開いているか
        var expandedMenuFor by remember { mutableStateOf<Int?>(null) }
        val navigator = LocalNavigator.current

        // プロジェクト一覧
        var projects by remember {
            mutableStateOf(
                listOf(
                    Project(1, "物理学ノート", "2025年4月20日", ProjectType.Note),
                    Project(2, "数学参考書", "2025年4月18日", ProjectType.Book),
                    Project(3, "英語ノート", "2025年4月15日", ProjectType.Note),
                    Project(4, "化学参考書", "2025年4月10日", ProjectType.Book),
                    Project(5, "化学参考書", "2025年4月10日", ProjectType.Book),
                    Project(6, "化学参考書", "2025年4月10日", ProjectType.Book),
                    Project(7, "化学参考書", "2025年4月10日", ProjectType.Book),
                    Project(8, "化学参考書", "2025年4月10日", ProjectType.Book),
                    Project(9, "化学参考書", "2025年4月10日", ProjectType.Book),
                )
            )
        }
        var searchTerm by remember { mutableStateOf("") }
        var editingId by remember { mutableStateOf<Int?>(null) }
        var editingTitle by remember { mutableStateOf("") }

        // フォーカス用リクエスタ（1つだけでOK）
        val focusRequester = remember { FocusRequester() }

        // フォーカス取得のトリガー
        LaunchedEffect(editingId) {
            if (editingId != null) {
                focusRequester.requestFocus()
            }
        }

        val filtered = projects.filter { it.title.contains(searchTerm, ignoreCase = true) }
        val gridState = rememberLazyGridState()

        Scaffold { padding ->
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
                                            imageVector = when (project.type) {
                                                ProjectType.Note -> Icons.Default.Description
                                                ProjectType.Book -> Icons.Default.MenuBook
                                            },
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
                                                        projects = projects.map {
                                                            if (it.id == editingId) {
                                                                it.copy(
                                                                    title = editingTitle.trim(),
                                                                    lastEdited = "2025年4月22日"
                                                                )
                                                            } else it
                                                        }
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
                                                text = project.title,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colors.onSurface
                                            )
                                            Text(
                                                text = "最終編集: ${project.lastEdited}",
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
                                                editingTitle = project.title
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
                            navigator?.push(SelectNoteOrProblemScreen())
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

data class Project(
    val id: Int,
    val title: String,
    val lastEdited: String,
    val type: ProjectType
)

enum class ProjectType { Note, Book }