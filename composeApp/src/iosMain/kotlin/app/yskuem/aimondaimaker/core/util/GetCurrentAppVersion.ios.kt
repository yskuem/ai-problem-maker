package app.yskuem.aimondaimaker.core.util

import platform.Foundation.NSBundle

actual fun getCurrentAppVersionString(): String {
    val infoDict = NSBundle.mainBundle.infoDictionary
    val version = infoDict?.get("CFBundleShortVersionString") as? String
    return version ?: throw Throwable("バージョン取得失敗")
}