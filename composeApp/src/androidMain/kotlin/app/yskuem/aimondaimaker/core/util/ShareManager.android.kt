package app.yskuem.aimondaimaker.core.util

import android.content.Context
import android.content.Intent

class AndroidShareManager(
    private val context: Context,
    private val clipboard: Clipboard,
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
        return "https://aimondaimaker.app/quiz/$groupId"
    }
}