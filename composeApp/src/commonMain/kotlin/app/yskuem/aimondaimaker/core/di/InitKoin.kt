package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.data.repository.di.repositoryModule
import app.yskuem.aimondaimaker.feature.problem.di.problemScreenModule
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(coreModule, problemScreenModule, repositoryModule)
}