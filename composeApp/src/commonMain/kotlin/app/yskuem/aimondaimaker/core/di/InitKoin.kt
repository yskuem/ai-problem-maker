package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.data.data_source.di.supabaseModule
import app.yskuem.aimondaimaker.data.repository.di.repositoryModule
import app.yskuem.aimondaimaker.feature.auth.ui.di.authScreenModule
import app.yskuem.aimondaimaker.feature.problem.di.problemScreenModule
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.di.selectAlbumOrCameraModule
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(
        coreModule,
        repositoryModule,
        problemScreenModule,
        selectAlbumOrCameraModule,
        supabaseModule,
        authScreenModule,
    )
}