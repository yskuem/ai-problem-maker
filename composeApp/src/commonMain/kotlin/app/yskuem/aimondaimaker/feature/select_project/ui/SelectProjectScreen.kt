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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SelectProjectScreen: Screen {
    @Composable
    override fun Content() {
        var projects by remember {
            mutableStateOf(
                listOf(
                    Project(1, "物理学ノート", "2025年4月20日", ProjectType.Note),
                    Project(2, "数学参考書", "2025年4月18日", ProjectType.Book),
                    Project(3, "英語ノート", "2025年4月15日", ProjectType.Note),
                    Project(4, "化学参考書", "2025年4月10日", ProjectType.Book)
                )
            )
        }
        var searchTerm by remember { mutableStateOf("") }
        var editingId by remember { mutableStateOf<Int?>(null) }
        var editingTitle by remember { mutableStateOf("") }

        val filtered = projects.filter { it.title.contains(searchTerm, ignoreCase = true) }
        val gridState: LazyGridState = rememberLazyGridState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("デジタルノート", fontSize = 20.sp) },
                    actions = {
                        IconButton(onClick = { /* 設定画面へ遷移 */ }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF9FAFB))
                        .padding(padding)
                        .padding(16.dp)
                ) {
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

                    // プロジェクトグリッド（1列表示）
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        state = gridState,
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(filtered) { project ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                elevation = 4.dp,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable { /* カードクリック */ }
                            ) {
                                Row(modifier = Modifier.padding(12.dp)) {
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
                                            when (project.type) {
                                                ProjectType.Note -> Icons.Default.Description
                                                ProjectType.Book -> Icons.Default.MenuBook
                                            },
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = Color(0xFF3B82F6)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        if (editingId == project.id) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                TextField(
                                                    value = editingTitle,
                                                    onValueChange = { editingTitle = it },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(end = 8.dp),
                                                    singleLine = true,
                                                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFE0F2FF))
                                                )
                                                IconButton(onClick = {
                                                    if (editingTitle.isNotBlank()) {
                                                        projects = projects.map {
                                                            if (it.id == editingId) it.copy(
                                                                title = editingTitle.trim(),
                                                                lastEdited = "2025年4月22日"
                                                            ) else it
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
                                            Text(
                                                text = project.title,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colors.onSurface,
                                                modifier = Modifier
                                                    .clickable {
                                                        editingId = project.id
                                                        editingTitle = project.title
                                                    }
                                            )
                                            Text(
                                                text = "最終編集: ${project.lastEdited}",
                                                fontSize = 12.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 新規プロジェクト作成ボタン
                    Button(
                        onClick = { /* 新規プロジェクト作成 */ },
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
        )
    }
}

data class Project(
    val id: Int,
    val title: String,
    val lastEdited: String,
    val type: ProjectType
)

enum class ProjectType { Note, Book }
