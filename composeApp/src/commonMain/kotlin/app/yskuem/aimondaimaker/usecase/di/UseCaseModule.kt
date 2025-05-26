package app.yskuem.aimondaimaker.usecase.di

import app.yskuem.aimondaimaker.domain.usecase.AdUseCase
import app.yskuem.aimondaimaker.usecase.AdUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<AdUseCase> {
        AdUseCaseImpl(
            adRepository = get(),
        )
    }
}