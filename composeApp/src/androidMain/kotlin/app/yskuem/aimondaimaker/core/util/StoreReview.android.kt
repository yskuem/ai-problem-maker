package app.yskuem.aimondaimaker.core.util

import androidx.activity.ComponentActivity
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidStoreReview(
    private val activity: ComponentActivity,
) : StoreReview {
    override suspend fun requestReview(): Result<Unit> {
        return try {
            val manager = ReviewManagerFactory.create(activity)
            
            // Request review info
            val reviewInfo = suspendCancellableCoroutine { continuation ->
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
                    val flow = manager.launchReviewFlow(activity, reviewInfo)
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
}