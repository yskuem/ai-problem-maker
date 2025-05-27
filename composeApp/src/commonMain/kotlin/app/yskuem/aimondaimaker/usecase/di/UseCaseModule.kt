package app.yskuem.aimondaimaker.usecase.di

import app.yskuem.aimondaimaker.domain.usecase.AdUseCase
import app.yskuem.aimondaimaker.domain.usecase.CheckUpdateUseCase
import app.yskuem.aimondaimaker.usecase.AdUseCaseImpl
import app.yskuem.aimondaimaker.usecase.CheckUpdateUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<AdUseCase> {
        AdUseCaseImpl(
            adRepository = get(),
        )
    }
    single<CheckUpdateUseCase> {
        CheckUpdateUseCaseImpl(
            versionRepository = get(),
        )
    }
}