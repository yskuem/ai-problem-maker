package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.core.util.di.openUrlPlatformModule
import app.yskuem.aimondaimaker.core.util.di.pdfSaverPlatformModule
import app.yskuem.aimondaimaker.core.util.di.sharePlatformModule
import app.yskuem.aimondaimaker.core.util.di.storeReviewPlatformModule
import app.yskuem.aimondaimaker.data.local_db.localDbModule
import app.yskuem.aimondaimaker.data.repository.di.repositoryModule
import app.yskuem.aimondaimaker.data.supabase.di.supabaseModule
import app.yskuem.aimondaimaker.feature.di.viewModelModule
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.di.selectAlbumOrCameraModule
import app.yskuem.aimondaimaker.usecase.di.useCaseModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(diModules)
    }
}

val diModules =
    listOf(
        coreModule,
        repositoryModule,
        selectAlbumOrCameraModule,
        supabaseModule,
        viewModelModule,
        localDbModule,
        useCaseModule,
        openUrlPlatformModule,
        sharePlatformModule,
        storeReviewPlatformModule,
        pdfSaverPlatformModule,
    )
