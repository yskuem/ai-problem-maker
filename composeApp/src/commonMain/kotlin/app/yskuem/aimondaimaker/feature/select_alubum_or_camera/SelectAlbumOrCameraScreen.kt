package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.how_to_title
import ai_problem_maker.composeapp.generated.resources.pick_from_gallery
import ai_problem_maker.composeapp.generated.resources.take_picture
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.yskuem.aimondaimaker.feature.note.ui.CreateNoteScreen
import app.yskuem.aimondaimaker.feature.quiz.ui.CreateQuizScreen
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.mode.CreateMode
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.mode.NavCreateMode
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.mode.toCreateMode
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource

data class SelectAlbumOrCameraScreen(
    val navMode: NavCreateMode,
    val projectId: String? = null,
    val onBack: () -> Unit = {},
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val scope = rememberCoroutineScope()
        val mode = navMode.toCreateMode()

        val galleryManager =
            rememberGalleryManager {
                scope.launch {
                    val bytes =
                        withContext(Dispatchers.Default) {
                            it?.toByteArray()
                        }
                    if (bytes == null) {
                        navigator?.pop()
                        return@launch
                    }
                    when (mode) {
                        CreateMode.Note -> {
                            navigator?.push(
                                CreateNoteScreen(
                                    imageByte = bytes,
                                    extension = "jpg",
                                    projectId = projectId,
                                ),
                            )
                        }
                        CreateMode.Quiz -> {
                            navigator?.push(
                                CreateQuizScreen(
                                    imageByte = bytes,
                                    extension = "jpg",
                                    projectId = projectId,
                                ),
                            )
                        }
                    }
                }
            }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(stringResource(mode.title))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigator?.pop()
                                onBack()
                            },
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                        }
                    },
                    colors =
                        TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                )
            },
        ) { paddingValues ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = mode.icon,
                        contentDescription = "",
                        modifier = Modifier.size(120.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(mode.contentDescription),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp,
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            navigator?.push(
                                item =
                                    CameraPermissionScreen(
                                        mode = navMode,
                                        projectId = projectId,
                                    ),
                            )
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Camera,
                            contentDescription = "カメラ",
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(Res.string.take_picture), fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                galleryManager.launch()
                            }
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(stringResource(Res.string.pick_from_gallery), fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(
                                text = stringResource(Res.string.how_to_title),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(mode.usage),
                                lineHeight = 24.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
