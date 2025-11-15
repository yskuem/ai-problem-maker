package app.yskuem.aimondaimaker.domain.data.repository

import com.revenuecat.purchases.kmp.models.Offering

interface SubscriptionRepository {
    suspend fun fetchCurrentOfferingOrNull(): Offering?

    suspend fun isSubscribed(entitlementId: String): Boolean
}