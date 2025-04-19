package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.feature.main.di.mainScreenModule
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(coreModule, mainScreenModule)
}