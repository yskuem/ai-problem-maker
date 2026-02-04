package app.yskuem.aimondaimaker.feature.subscription

import com.revenuecat.purchases.kmp.models.Package

sealed interface SubscriptionScreenEvent {
    data object Initialize : SubscriptionScreenEvent
    data object RestorePurchase : SubscriptionScreenEvent
    data class PurchaseSubscription(
        val purchasePackage: Package,
    ) : SubscriptionScreenEvent
}