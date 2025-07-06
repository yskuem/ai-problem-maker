package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.core.util.di.openUrlPlatformModule
import app.yskuem.aimondaimaker.data.local_db.localDbModule
import app.yskuem.aimondaimaker.data.repository.di.adRepositoryPlatformModule
import app.yskuem.aimondaimaker.data.repository.di.repositoryModule
import app.yskuem.aimondaimaker.data.supabase.di.supabaseModule
import app.yskuem.aimondaimaker.feature.di.viewModelModule
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.di.selectAlbumOrCameraModule
import app.yskuem.aimondaimaker.usecase.di.useCaseModule
import org.koin.core.KoinApplication

fun initKoin() {
    initKoinPlatform()
}

expect fun initKoinPlatform(): KoinApplication

val diModules =
    listOf(
        coreModule,
        repositoryModule,
        adRepositoryPlatformModule,
        selectAlbumOrCameraModule,
        supabaseModule,
        viewModelModule,
        localDbModule,
        useCaseModule,
        openUrlPlatformModule,
    )
