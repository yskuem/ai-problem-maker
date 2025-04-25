package app.yskuem.aimondaimaker.feature.problem.di

import app.yskuem.aimondaimaker.feature.problem.viewmodel.ShowProblemScreenViewModel
import org.koin.dsl.module

val problemScreenModule = module {
    factory {
        ShowProblemScreenViewModel(
            problemRepository = get()
        )
    }
}