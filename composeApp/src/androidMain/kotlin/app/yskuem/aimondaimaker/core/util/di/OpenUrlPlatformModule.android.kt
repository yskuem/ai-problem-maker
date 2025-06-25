package app.yskuem.aimondaimaker.core.util.di

import app.yskuem.aimondaimaker.core.util.AndroidOpenUrl
import app.yskuem.aimondaimaker.core.util.OpenUrl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val openUrlPlatformModule: Module =
    module {
        single<OpenUrl> {
            AndroidOpenUrl(
                context = androidContext(),
            )
        }
    }
