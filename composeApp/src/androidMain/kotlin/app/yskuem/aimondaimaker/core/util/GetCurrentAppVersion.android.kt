package app.yskuem.aimondaimaker.core.util

import app.yskuem.aimondaimaker.core.util.ActivityProvider

actual fun getCurrentAppVersionString(): String {
    val context = requireNotNull(ActivityProvider.getContext())
    val info = context.packageManager.getPackageInfo(context.packageName, 0)
    val version = info.versionName
    return version ?: throw Throwable("バージョン取得失敗")
}
