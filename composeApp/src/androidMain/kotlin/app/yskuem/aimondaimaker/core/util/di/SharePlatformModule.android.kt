package app.yskuem.aimondaimaker.core.util.di

import app.yskuem.aimondaimaker.core.util.AndroidClipboard
import app.yskuem.aimondaimaker.core.util.AndroidShareManager
import app.yskuem.aimondaimaker.core.util.Clipboard
import app.yskuem.aimondaimaker.core.util.ShareManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val sharePlatformModule: Module =
    module {
        single<Clipboard> {
            AndroidClipboard(
                context = androidContext(),
            )
        }
        single<ShareManager> {
            AndroidShareManager(
                context = androidContext(),
                clipboard = get(),
                sharedQuizRepository = get(),
                authRepository = get(),
            )
        }
    }
