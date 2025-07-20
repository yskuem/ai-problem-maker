package app.yskuem.aimondaimaker.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.koin.compose.koinInject

@Composable
fun rememberStoreReviewLauncher(): suspend () -> Result<Unit> {
    val storeReview: StoreReview = koinInject()
    return remember { { storeReview.requestReview() } }
}

@Composable
fun LaunchStoreReview(
    trigger: Boolean,
    onComplete: (Result<Unit>) -> Unit = {}
) {
    val storeReview: StoreReview = koinInject()
    var hasLaunched by remember { mutableStateOf(false) }
    
    LaunchedEffect(trigger) {
        if (trigger && !hasLaunched) {
            hasLaunched = true
            val result = storeReview.requestReview()
            onComplete(result)
        }
    }
}