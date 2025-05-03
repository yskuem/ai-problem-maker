package app.yskuem.aimondaimaker.feature.di

import app.yskuem.aimondaimaker.feature.auth.ui.AuthScreenViewModel
import app.yskuem.aimondaimaker.feature.problem.viewmodel.ShowProblemScreenViewModel
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.SelectAlbumOrCameraViewModel
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreenViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory {
        AuthScreenViewModel(
            authRepository = get(),
        )
    }
    factory {
        ShowProblemScreenViewModel(
            problemRepository = get()
        )
    }
    factory {
        SelectAlbumOrCameraViewModel(
            imagePicker = get(),
        )
    }
    factory {
        SelectProjectScreenViewModel(
            supabaseClientHelper = get(),
        )
    }
}