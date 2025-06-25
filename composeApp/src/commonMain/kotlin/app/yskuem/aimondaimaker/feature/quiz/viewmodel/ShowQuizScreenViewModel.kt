package app.yskuem.aimondaimaker.feature.quiz.viewmodel

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.usecase.AdUseCase
import app.yskuem.aimondaimaker.feature.quiz.uiState.QuizUiState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShowQuizScreenViewModel(
    private val authRepository: AuthRepository,
    private val quizRepository: QuizRepository,
    private val projectRepository: ProjectRepository,
    private val adUseCase: AdUseCase,
) : ScreenModel {
    private val _quizList = MutableStateFlow<DataUiState<List<Quiz>>>(DataUiState.Loading)
    private val _currentQuizIndex = MutableStateFlow(0)

    val uiState: StateFlow<QuizUiState> =
        combine(
            _quizList,
            _currentQuizIndex,
        ) { quizList, currentQuizListIndex ->
            QuizUiState(
                quizList = quizList,
                currentQuizListIndex = currentQuizListIndex,
            )
        }.stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QuizUiState(),
        )

    fun onLoadPage(
        imageByte: ByteArray,
        fileName: String,
        extension: String,
        projectId: String? = null,
    ) {
        screenModelScope.launch {
            _quizList.value = DataUiState.Loading

            // 画像からQuizを取得
            val quizList =
                onFetchQuizList(
                    imageByte = imageByte,
                    fileName = fileName,
                    extension = extension,
                )
            onSaveData(
                quizList = quizList,
                projectId = projectId,
            )
        }
    }

    private suspend fun onFetchQuizList(
        imageByte: ByteArray,
        fileName: String,
        extension: String,
    ): List<Quiz> =
        try {
            val quizList = quizRepository.generateFromImage(imageByte, fileName, extension)
            _quizList.value = DataUiState.Success(quizList)
            quizList
        } catch (e: Exception) {
            _quizList.value = DataUiState.Error(e)
            emptyList()
        }

    private suspend fun onSaveData(
        quizList: List<Quiz>,
        projectId: String? = null,
    ) {
        val res =
            runCatching {
                val userId = authRepository.getUserId()

                val finalProjectId =
                    projectId ?: projectRepository
                        .addProject(
                            projectName = quizList[0].title,
                        ).id

                // QuizInfoの保存
                quizRepository.saveQuizInfo(
                    projectId = finalProjectId,
                    userId = userId,
                    groupId = quizList[0].groupId,
                    quizTitle = quizList[0].title,
                )

                // Quizの保存
                quizList.map {
                    quizRepository.saveQuiz(
                        quiz = it,
                        projectId = finalProjectId,
                        userId = userId,
                    )
                }
            }
        res
            .onSuccess {
                println("success save")
            }.onFailure { e ->
                println("$e")
            }
    }

    fun showInterstitialAd() {
        screenModelScope.launch {
            adUseCase.onInterstitialAdLoaded()
        }
    }
}
