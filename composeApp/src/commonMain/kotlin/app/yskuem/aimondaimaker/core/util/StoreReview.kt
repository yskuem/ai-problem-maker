package app.yskuem.aimondaimaker.core.util

interface StoreReview {
    suspend fun requestReview(platformActivity: PlatformActivity?): Result<Unit>
}
