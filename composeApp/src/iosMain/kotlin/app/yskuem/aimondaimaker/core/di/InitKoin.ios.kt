package app.yskuem.aimondaimaker.core.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin


actual fun initKoinPlatform(): KoinApplication = startKoin {
    modules(diModules)
}

