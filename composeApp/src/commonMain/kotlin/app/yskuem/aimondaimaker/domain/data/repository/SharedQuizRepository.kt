package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.SharedQuiz

interface SharedQuizRepository {
    suspend fun saveSharedQuiz(
        groupId: String,
        quizData: List<Quiz>,
        userId: String,
    ): SharedQuiz

    suspend fun getSharedQuiz(groupId: String): SharedQuiz?
}