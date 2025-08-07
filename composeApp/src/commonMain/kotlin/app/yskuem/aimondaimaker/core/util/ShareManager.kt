package app.yskuem.aimondaimaker.core.util

import app.yskuem.aimondaimaker.domain.entity.Quiz

interface ShareManager {
    fun copyToClipboard(text: String)

    fun shareText(
        text: String,
        title: String? = null,
    )

    fun generateQuizUrl(groupId: String): String

    suspend fun saveQuizToSupabase(
        groupId: String,
        quizData: List<Quiz>,
        userId: String,
    )
}
