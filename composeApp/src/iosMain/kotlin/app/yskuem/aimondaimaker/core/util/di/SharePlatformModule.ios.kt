package app.yskuem.aimondaimaker.core.util.di

import app.yskuem.aimondaimaker.core.util.Clipboard
import app.yskuem.aimondaimaker.core.util.IosClipboard
import app.yskuem.aimondaimaker.core.util.IosShareManager
import app.yskuem.aimondaimaker.core.util.ShareManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val sharePlatformModule: Module =
    module {
        single<Clipboard> {
            IosClipboard()
        }
        single<ShareManager> {
            IosShareManager(
                clipboard = get(),
                sharedQuizRepository = get(),
                authRepository = get(),
            )
        }
    }
