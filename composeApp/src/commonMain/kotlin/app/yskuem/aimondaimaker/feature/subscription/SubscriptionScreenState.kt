package app.yskuem.aimondaimaker.feature.subscription

import app.yskuem.aimondaimaker.core.ui.DataUiState
import com.revenuecat.purchases.kmp.models.Offering

data class SubscriptionScreenState(
    val offering: DataUiState<Offering> = DataUiState.Initial,
)
