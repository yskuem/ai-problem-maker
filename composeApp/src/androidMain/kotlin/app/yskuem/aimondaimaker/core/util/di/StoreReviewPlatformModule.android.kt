package app.yskuem.aimondaimaker.core.util.di

import androidx.activity.ComponentActivity
import app.yskuem.aimondaimaker.core.util.AndroidStoreReview
import app.yskuem.aimondaimaker.core.util.StoreReview
import org.koin.core.module.Module
import org.koin.dsl.module

actual val storeReviewPlatformModule: Module =
    module {
        single<StoreReview> {
            AndroidStoreReview(
                activity = get<ComponentActivity>(),
            )
        }
    }