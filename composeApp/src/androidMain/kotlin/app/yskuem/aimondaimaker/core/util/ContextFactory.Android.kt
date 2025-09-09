package app.yskuem.aimondaimaker.core.util

import app.yskuem.aimondaimaker.core.util.ActivityProvider

actual class ContextFactory {
    actual fun getContext(): Any = requireNotNull(ActivityProvider.getContext())

    actual fun getApplication(): Any = requireNotNull(ActivityProvider.getApplication())

    actual fun getActivity(): Any = requireNotNull(ActivityProvider.getActivity())
}
