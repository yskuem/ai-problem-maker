package app.yskuem.aimondaimaker.core.util

interface StoreReview {
    suspend fun requestReview(): Result<Unit>
}