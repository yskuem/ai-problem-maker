package app.yskuem.aimondaimaker.core.util

import platform.StoreKit.SKStoreReviewController

class IosStoreReview : StoreReview {
    override suspend fun requestReview(): Result<Unit> =
        try {
            SKStoreReviewController.requestReview()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
