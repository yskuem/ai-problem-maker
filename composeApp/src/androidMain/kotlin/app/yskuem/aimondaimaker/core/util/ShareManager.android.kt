package app.yskuem.aimondaimaker.core.util

import android.content.Context
import android.content.Intent
import app.yskuem.aimondaimaker.core.config.WEB_QUIZ_APP_DOMAIN
import app.yskuem.aimondaimaker.domain.data.repository.SharedQuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz

class AndroidShareManager(
    private val context: Context,
    private val clipboard: Clipboard,
    private val sharedQuizRepository: SharedQuizRepository,
) : ShareManager {
    override fun copyToClipboard(text: String) {
        clipboard.copyText(text)
    }

    override fun shareText(text: String, title: String?) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            if (title != null) {
                putExtra(Intent.EXTRA_TITLE, title)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(shareIntent, title ?: "クイズを共有")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    override fun generateQuizUrl(groupId: String): String {
        // TODO: Replace with actual app URL scheme when available
        return "${WEB_QUIZ_APP_DOMAIN}?group_id=$groupId"
    }

    override suspend fun saveQuizToSupabase(groupId: String, quizData: List<Quiz>, userId: String) {
        try {
            val uploadQuizzes = sharedQuizRepository.getSharedQuizzes(groupId = groupId)
            if( uploadQuizzes.isEmpty()) {
                sharedQuizRepository.saveSharedQuizzes(groupId, quizData, userId)
            }
        } catch (e: Exception) {
            // Handle error silently for now - could be logged or shown to user
            println("Failed to save quiz to Supabase: ${e.message}")
        }
    }
}