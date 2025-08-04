package app.yskuem.aimondaimaker.core.util

import app.yskuem.aimondaimaker.domain.data.repository.SharedQuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.Foundation.NSArray
import platform.Foundation.NSString
import platform.Foundation.arrayWithObjects

class IosShareManager(
    private val clipboard: Clipboard,
    private val sharedQuizRepository: SharedQuizRepository,
) : ShareManager {
    override fun copyToClipboard(text: String) {
        clipboard.copyText(text)
    }

    override fun shareText(text: String, title: String?) {
        val items = NSArray.arrayWithObjects(text as NSString, null)
        val activityController = UIActivityViewController(
            activityItems = items,
            applicationActivities = null
        )
        
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            activityController,
            animated = true,
            completion = null
        )
    }

    override fun generateQuizUrl(groupId: String): String {
        // TODO: Replace with actual app URL scheme when available
        return "https://aimondaimaker.app/quiz/$groupId"
    }

    override suspend fun saveQuizToSupabase(groupId: String, quizData: List<Quiz>, userId: String) {
        try {
            sharedQuizRepository.saveSharedQuiz(groupId, quizData, userId)
        } catch (e: Exception) {
            // Handle error silently for now - could be logged or shown to user
            println("Failed to save quiz to Supabase: ${e.message}")
        }
    }
}