package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.QuizInfo

interface QuizRepository {
    suspend fun fetchFromImage(
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

    suspend fun fetchAnsweredQuizList(
        projectId: String,
    ): List<Quiz>

    suspend fun saveQuiz(
        quiz: Quiz,
        projectId: String,
        userId: String,
    )

    suspend fun fetchQuizInfoList(userId: String): List<QuizInfo>
}