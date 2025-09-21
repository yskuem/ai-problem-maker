package app.yskuem.aimondaimaker.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.yskuem.aimondaimaker.data.local_db.UserDataStore
import org.koin.compose.koinInject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val TWO_MONTHS_IN_MILLIS = 60L * 24L * 60L * 60L * 1000L // 60 days in milliseconds

@OptIn(ExperimentalTime::class)
@Composable
fun rememberStoreReviewLauncher(): suspend () -> Result<Unit> {
    val storeReview: StoreReview = koinInject()
    val userDataStore: UserDataStore = koinInject()
    val platformActivity = currentPlatformActivity()

    return remember {
        {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            val lastRequestTime = userDataStore.getLastReviewRequestTimestamp()

            if (lastRequestTime == 0L || currentTime - lastRequestTime >= TWO_MONTHS_IN_MILLIS) {
                val result = storeReview.requestReview(platformActivity)
                if (result.isSuccess) {
                    userDataStore.setLastReviewRequestTimestamp(currentTime)
                }
                result
            } else {
                Result.success(Unit) // Skip review request, not enough time has passed
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun LaunchStoreReview(
    trigger: Boolean,
    onComplete: (Result<Unit>) -> Unit = {},
) {
    val storeReview: StoreReview = koinInject()
    val userDataStore: UserDataStore = koinInject()
    var hasLaunched by remember { mutableStateOf(false) }
    val platformActivity = currentPlatformActivity()

    LaunchedEffect(trigger) {
        if (trigger && !hasLaunched) {
            hasLaunched = true

            val currentTime = Clock.System.now().toEpochMilliseconds()
            val lastRequestTime = userDataStore.getLastReviewRequestTimestamp()

            val result =
                if (lastRequestTime == 0L || currentTime - lastRequestTime >= TWO_MONTHS_IN_MILLIS) {
                    val reviewResult = storeReview.requestReview(platformActivity)
                    if (reviewResult.isSuccess) {
                        userDataStore.setLastReviewRequestTimestamp(currentTime)
                    }
                    reviewResult
                } else {
                    Result.success(Unit) // Skip review request, not enough time has passed
                }

            onComplete(result)
        }
    }
}
