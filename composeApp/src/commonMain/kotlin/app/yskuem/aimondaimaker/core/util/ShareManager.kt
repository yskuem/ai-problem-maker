package app.yskuem.aimondaimaker.core.util

interface ShareManager {
    fun copyToClipboard(text: String)
    fun shareText(text: String, title: String? = null)
    fun generateQuizUrl(groupId: String): String
}