package app.yskuem.aimondaimaker.core.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class AndroidOpenUrl(
    private val context: Context,
) : OpenUrl {
    override fun handle(url: String) {
        val intent =
            Intent(Intent.ACTION_VIEW, url.toUri())
                .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        context.startActivity(intent)
    }
}
