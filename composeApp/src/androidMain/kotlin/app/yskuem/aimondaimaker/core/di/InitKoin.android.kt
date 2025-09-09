package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.core.util.ActivityProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

actual fun initKoinPlatform(): KoinApplication =
    startKoin {
        androidContext(requireNotNull(ActivityProvider.getContext()))
        modules(
            diModules,
        )
    }
