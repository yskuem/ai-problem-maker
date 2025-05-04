package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Quiz

interface QuizRepository {
    suspend fun fetchFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): List<Quiz>

    suspend fun saveQuiz(
        quiz: Quiz,
        projectId: String,
    )
}