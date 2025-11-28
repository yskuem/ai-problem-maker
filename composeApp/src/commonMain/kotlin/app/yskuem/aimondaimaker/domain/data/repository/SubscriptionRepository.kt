package app.yskuem.aimondaimaker.domain.data.repository

import com.revenuecat.purchases.kmp.ktx.SuccessfulLogin
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Offering
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.StoreTransaction
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    suspend fun fetchCurrentOfferingOrNull(): Offering?

    fun isSubscribed(entitlementId: String = ENTITLEMENT_ID_PREMIUM): Flow<Boolean>

    suspend fun identifyUser(appUserId: String): SuccessfulLogin

    suspend fun logout(): CustomerInfo

    suspend fun subscribe(packageToPurchase: Package): StoreTransaction

    suspend fun restorePurchaseAndRecheckIsSubscribed(
        entitlementId: String = ENTITLEMENT_ID_PREMIUM
    ): Boolean
}


private const val ENTITLEMENT_ID_PREMIUM = "premium"