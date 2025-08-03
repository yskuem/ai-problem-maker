package app.yskuem.aimondaimaker.core.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class AndroidClipboard(
    private val context: Context,
) : Clipboard {
    override fun copyText(text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("quiz_link", text)
        clipboardManager.setPrimaryClip(clip)
    }
}