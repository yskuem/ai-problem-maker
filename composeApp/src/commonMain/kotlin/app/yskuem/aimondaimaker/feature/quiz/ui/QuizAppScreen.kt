package app.yskuem.aimondaimaker.feature.quiz.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.yskuem.aimondaimaker.domain.entity.Quiz
import cafe.adriel.voyager.navigator.LocalNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizApp(
    quizList: List<Quiz>,
    onBack: () -> Unit,
) {
    var currentQuestion by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var quizCompleted by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF0066CC)
    val backgroundColor = Color(0xFFF5F5F7)

    MaterialTheme(
        colorScheme =
            lightColorScheme(
                primary = primaryColor,
                background = backgroundColor,
                surface = Color.White,
            ),
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "${currentQuestion + 1} / ${quizList.size} 問目")
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "戻る",
                            )
                        }
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Color.Black,
                            navigationIconContentColor = Color.Black,
                            actionIconContentColor = Color.Black,
                        ),
                )
            },
            containerColor = backgroundColor,
        ) { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (quizCompleted) {
                    QuizCompletedScreen(
                        score = score,
                        totalQuestions = quizList.size,
                        onRestart = {
                            currentQuestion = 0
                            selectedOption = null
                            showResult = false
                            score = 0
                            quizCompleted = false
                        },
                    )
                } else {
                    val currentQuiz = quizList[currentQuestion]
                    val correctAnswerIndex = currentQuiz.choices.indexOf(currentQuiz.answer)

                    QuizContentScreen(
                        quiz = currentQuiz,
                        correctAnswerIndex = correctAnswerIndex,
                        currentQuestionIndex = currentQuestion,
                        totalQuestions = quizList.size,
                        selectedOption = selectedOption,
                        showResult = showResult,
                        score = score,
                        onOptionSelected = { optionIndex ->
                            if (selectedOption == null) {
                                selectedOption = optionIndex
                                showResult = true
                                if (optionIndex == correctAnswerIndex) {
                                    score += 1
                                }
                            }
                        },
                        onNextQuestion = {
                            selectedOption = null
                            showResult = false
                            if (currentQuestion < quizList.size - 1) {
                                currentQuestion += 1
                            } else {
                                quizCompleted = true
                            }
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuizContentScreen(
    quiz: Quiz,
    correctAnswerIndex: Int,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    selectedOption: Int?,
    showResult: Boolean,
    score: Int,
    onOptionSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Card(
        modifier =
            Modifier
                .widthIn(max = 500.dp)
                .padding(16.dp)
                .verticalScroll(scrollState),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "問題 ${currentQuestionIndex + 1}/$totalQuestions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
                Text(
                    text = "スコア: $score",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Chip(
                onClick = { },
                colors =
                    ChipDefaults.chipColors(
                        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    ),
                modifier = Modifier.padding(vertical = 4.dp),
            ) {
                Text(
                    text = quiz.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            val progress = (currentQuestionIndex + 1).toFloat() / totalQuestions
            val animatedProgress by animateFloatAsState(targetValue = progress)

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f)),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primary),
                )
            }

            Text(
                text = quiz.question,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(vertical = 8.dp),
            )

            quiz.choices.forEachIndexed { index, option ->
                OptionItem(
                    text = option,
                    isSelected = selectedOption == index,
                    isCorrectAnswer = index == correctAnswerIndex,
                    isShowingResult = showResult,
                    onClick = { onOptionSelected(index) },
                )
            }

            AnimatedVisibility(visible = showResult) {
                Column {
                    Card(
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(vertical = 8.dp),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "解説",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = quiz.explanation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = showResult) {
                Button(
                    onClick = onNextQuestion,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = if (currentQuestionIndex < totalQuestions - 1) "次の問題へ" else "結果を見る",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next")
                    }
                }
            }
        }
    }
}

@Composable
fun OptionItem(
    text: String,
    isSelected: Boolean,
    isCorrectAnswer: Boolean,
    isShowingResult: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor =
        when {
            isShowingResult && isCorrectAnswer -> Color(0xFFE7F3E8)
            isShowingResult && isSelected && !isCorrectAnswer -> Color(0xFFFCE8E8)
            isSelected -> Color(0xFFE1EFFF)
            else -> Color.White
        }

    val borderColor =
        when {
            isShowingResult && isCorrectAnswer -> Color(0xFF4CAF50)
            isShowingResult && isSelected && !isCorrectAnswer -> Color(0xFFE57373)
            isSelected -> MaterialTheme.colorScheme.primary
            else -> Color.LightGray
        }

    val alpha = if (isShowingResult && !isSelected && !isCorrectAnswer) 0.5f else 1f

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
                .clickable(enabled = !isShowingResult, onClick = onClick)
                .padding(16.dp)
                .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isShowingResult) {
            if (isCorrectAnswer) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Correct",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.padding(end = 12.dp),
                )
            } else if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Incorrect",
                    tint = Color(0xFFE57373),
                    modifier = Modifier.padding(end = 12.dp),
                )
            } else {
                Spacer(modifier = Modifier.width(36.dp))
            }
        } else {
            Spacer(modifier = Modifier.width(36.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun QuizCompletedScreen(
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit,
) {
    val percentage = (score.toFloat() / totalQuestions * 100).toInt()
    val navigator = LocalNavigator.current

    Card(
        modifier =
            Modifier
                .widthIn(max = 500.dp)
                .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "クイズ終了！",
                style =
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
            )

            Text(
                text = "${totalQuestions}問中${score}問正解",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )

            Text(
                text = "$percentage%",
                style =
                    MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp),
            )

            Button(
                onClick = onRestart,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
            ) {
                Text(
                    text = "もう一度挑戦する",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp),
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Button(
                onClick = {
                    navigator?.pop()
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
            ) {
                Text(
                    text = "前の画面に戻る",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}
