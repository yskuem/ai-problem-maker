package app.yskuem.aimondaimaker.feature.auth.ui.di

import app.yskuem.aimondaimaker.feature.auth.ui.AuthScreenViewModel
import org.koin.dsl.module

val authScreenModule = module {
    factory {
        AuthScreenViewModel(
            authRepository = get(),
        )
    }
}