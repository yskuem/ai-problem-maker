package app.yskuem.aimondaimaker.core.util.di

import app.yskuem.aimondaimaker.core.util.AndroidStoreReview
import app.yskuem.aimondaimaker.core.util.ActivityProvider
import app.yskuem.aimondaimaker.core.util.StoreReview
import org.koin.core.module.Module
import org.koin.dsl.module

actual val storeReviewPlatformModule: Module =
    module {
        factory<StoreReview> {
            AndroidStoreReview(
                activity = requireNotNull(ActivityProvider.getActivity()),
            )
        }
    }
