package app.yskuem.aimondaimaker.core.util.di

import app.yskuem.aimondaimaker.core.util.IosOpenUrl
import app.yskuem.aimondaimaker.core.util.OpenUrl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val openUrlPlatformModule: Module = module {
    single<OpenUrl> {
        IosOpenUrl()
    }
}