package app.yskuem.aimondaimaker.core.util

import platform.Foundation.NSBundle
import platform.UIKit.UIApplication

actual class ContextFactory {
    // RootViewController can be used to identify your current screen
    actual fun getActivity(): Any = UIApplication.sharedApplication.keyWindow?.rootViewController ?: ""
}
