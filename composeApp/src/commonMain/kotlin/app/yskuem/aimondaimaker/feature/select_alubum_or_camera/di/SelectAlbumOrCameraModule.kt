package app.yskuem.aimondaimaker.feature.select_alubum_or_camera.di

import app.yskuem.aimondaimaker.feature.problem.viewmodel.ShowProblemScreenViewModel
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.SelectAlbumOrCameraViewModel
import org.koin.dsl.module

val selectAlbumOrCameraModule = module {
    factory {
        SelectAlbumOrCameraViewModel(
            imagePicker = get(),
        )
    }
}