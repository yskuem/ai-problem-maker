package app.yskuem.aimondaimaker.core.util

import androidx.activity.ComponentActivity
import app.yskuem.aimondaimaker.MainActivity

actual class ContextFactory {
    actual fun getActivity(): Any = MainActivity.instance
}
