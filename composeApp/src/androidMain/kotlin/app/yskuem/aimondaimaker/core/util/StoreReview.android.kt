package app.yskuem.aimondaimaker.core.util

import android.content.Context
import androidx.activity.ComponentActivity
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidStoreReview : StoreReview {
    override suspend fun requestReview(platformActivity: PlatformActivity?): Result<Unit> =
        try {
            if (platformActivity == null) {
                throw IllegalArgumentException("PlatformActivity is null")
            }
            val manager = ReviewManagerFactory.create(platformActivity)

            // Request review info
            val reviewInfo =
                suspendCancellableCoroutine { continuation ->
                    val request = manager.requestReviewFlow()
                    request.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(task.result)
                        } else {
                            continuation.resume(null)
                        }
                    }
                }

            if (reviewInfo != null) {
                // Launch the review flow
                suspendCancellableCoroutine { continuation ->
                    val flow = manager.launchReviewFlow(platformActivity, reviewInfo)
                    flow.addOnCompleteListener {
                        // The review flow has finished, regardless of whether the user reviewed or not
                        continuation.resume(Unit)
                    }
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to request review info"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
