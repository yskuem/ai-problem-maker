package app.yskuem.aimondaimaker.domain.data.repository

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AdRepository {

    val interstitialRequests: SharedFlow<Unit>
}
