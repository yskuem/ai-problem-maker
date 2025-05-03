package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.data.supabase.di.supabaseModule
import app.yskuem.aimondaimaker.data.repository.di.repositoryModule
import app.yskuem.aimondaimaker.feature.di.viewModelModule
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.di.selectAlbumOrCameraModule
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(
        coreModule,
        repositoryModule,
        selectAlbumOrCameraModule,
        supabaseModule,
        viewModelModule,
    )
}