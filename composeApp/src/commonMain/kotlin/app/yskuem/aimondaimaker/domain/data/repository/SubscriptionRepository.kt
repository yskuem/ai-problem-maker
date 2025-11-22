package app.yskuem.aimondaimaker.domain.data.repository

import com.revenuecat.purchases.kmp.ktx.SuccessfulLogin
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Offering

interface SubscriptionRepository {
    suspend fun fetchCurrentOfferingOrNull(): Offering?

    suspend fun isSubscribed(entitlementId: String): Boolean

    suspend fun identifyUser(appUserId: String): SuccessfulLogin

    suspend fun logout(): CustomerInfo
}