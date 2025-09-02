package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Quiz

interface SharedQuizRepository {
    suspend fun saveSharedQuizzes(
        groupId: String,
        quizData: List<Quiz>,
        userId: String,
    ): List<Quiz>

    suspend fun getSharedQuizzes(groupId: String): List<Quiz>
}
