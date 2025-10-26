package app.yskuem.aimondaimaker.feature.quiz.viewmodel

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.ui.PdfDocument
import app.yskuem.aimondaimaker.core.util.FirebaseCrashlytics
import app.yskuem.aimondaimaker.data.api.response.PdfResponse
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.PdfRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.feature.quiz.uiState.QuizUiState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowQuizScreenViewModel(
    private val authRepository: AuthRepository,
    private val quizRepository: QuizRepository,
    private val projectRepository: ProjectRepository,
    private val crashlytics: FirebaseCrashlytics,
    private val pdfRepository: PdfRepository,
) : ScreenModel {
    private val _quizList = MutableStateFlow<DataUiState<List<Quiz>>>(DataUiState.Loading)
    private val _currentQuizIndex = MutableStateFlow(0)
    private val _pdfData = MutableStateFlow<DataUiState<PdfResponse>>(DataUiState.Initial)
    private val _savePdfState = MutableStateFlow<DataUiState<Unit>>(DataUiState.Initial)
    val savePdfState: StateFlow<DataUiState<Unit>> = _savePdfState.asStateFlow()

    val uiState: StateFlow<QuizUiState> =
        combine(
            _quizList,
            _currentQuizIndex,
            _pdfData,
            _savePdfState
        ) { quizList, currentQuizListIndex, pdfData, saveState ->
            QuizUiState(
                quizList = quizList,
                currentQuizListIndex = currentQuizListIndex,
                pdfData = pdfData,
                pdfSaveState = saveState,
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

    fun onCreatePdf(
        quizList: List<Quiz>,
        isColorMode: Boolean = true,
    ) {
        screenModelScope.launch {
            _pdfData.update {
                DataUiState.Loading
            }
            val res = pdfRepository.createQuizPdf(
                quizList = quizList,
                isColorModel = isColorMode,
            )
            _pdfData.update {
                DataUiState.Success(res)
            }
        }
    }

    fun onClosePdfViewer() {
        _pdfData.update {
            DataUiState.Initial
        }
        _savePdfState.value = DataUiState.Initial
    }

    fun onSavePdf(
        pdfData: ByteArray,
        pdfName: String,
    ) {
        screenModelScope.launch {
            _savePdfState.value = DataUiState.Loading
            try {
                // suggestedName は拡張子なしを渡し、extension で "pdf" を指定
                val nameNoExt =
                    if (pdfName.endsWith(".pdf", ignoreCase = true))
                        pdfName.substring(0, pdfName.length - 4)
                    else
                        pdfName

                val dest: PlatformFile? = FileKit.openFileSaver(
                    suggestedName = nameNoExt,
                    extension = "pdf"
                )

                if (dest == null) {
                    _savePdfState.value = DataUiState.Initial // キャンセル
                    return@launch
                }

                // 実書き込みは重い可能性があるので別ディスパッチャへ
                withContext(Dispatchers.Default) {
                    dest.write(pdfData)
                }

                _savePdfState.value = DataUiState.Success(Unit)
            } catch (t: Throwable) {
                _savePdfState.value = DataUiState.Error(t)
            }
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
            crashlytics.log(e.toString())
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
}
