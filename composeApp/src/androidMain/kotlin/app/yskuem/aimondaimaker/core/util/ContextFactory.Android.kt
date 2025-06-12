package app.yskuem.aimondaimaker.core.util

import app.yskuem.aimondaimaker.MainActivity

actual class ContextFactory {
    actual fun getContext(): Any = MainActivity.instance.baseContext

    actual fun getApplication(): Any = MainActivity.instance.application

    actual fun getActivity(): Any = MainActivity.instance
}
