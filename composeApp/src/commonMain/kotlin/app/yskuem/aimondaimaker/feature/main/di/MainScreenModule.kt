package app.yskuem.aimondaimaker.feature.main.di

import app.yskuem.aimondaimaker.feature.main.viewmodel.MainScreenViewModel
import org.koin.dsl.module

val mainScreenModule = module {
    factory { MainScreenViewModel(get()) }
}