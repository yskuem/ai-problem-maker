package app.yskuem.aimondaimaker.core.util.di

import app.yskuem.aimondaimaker.core.util.IosStoreReview
import app.yskuem.aimondaimaker.core.util.StoreReview
import org.koin.core.module.Module
import org.koin.dsl.module

actual val storeReviewPlatformModule: Module =
    module {
        single<StoreReview> {
            IosStoreReview()
        }
    }