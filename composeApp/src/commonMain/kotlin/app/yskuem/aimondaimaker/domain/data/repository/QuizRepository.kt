package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.QuizInfo

interface QuizRepository {
    suspend fun generateFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): List<Quiz>

    suspend fun saveQuizInfo(
        projectId: String,
        userId: String,
        groupId: String,
        quizTitle: String,
    )

    suspend fun fetchAnsweredQuizzes(
        groupId: String,
    ): List<Quiz>

    suspend fun saveQuiz(
        quiz: Quiz,
        projectId: String,
        userId: String,
    )

    suspend fun fetchQuizInfoList(projectId: String): List<QuizInfo>
}