package app.yskuem.aimondaimaker.feature.problem.di

import app.yskuem.aimondaimaker.feature.problem.viewmodel.ProblemScreenViewModel
import org.koin.dsl.module

val problemScreenModule = module {
    factory {
        ProblemScreenViewModel(
            imagePicker = get(),
            problemRepository = get()
        )
    }
}