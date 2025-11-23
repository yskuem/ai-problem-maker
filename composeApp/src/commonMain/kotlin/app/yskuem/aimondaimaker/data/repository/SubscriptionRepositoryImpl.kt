package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.domain.data.repository.SubscriptionRepository
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.SuccessfulLogin
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import com.revenuecat.purchases.kmp.ktx.awaitLogIn
import com.revenuecat.purchases.kmp.ktx.awaitLogOut
import com.revenuecat.purchases.kmp.ktx.awaitOfferings
import com.revenuecat.purchases.kmp.ktx.awaitPurchase
import com.revenuecat.purchases.kmp.ktx.awaitRestore
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Offering
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PurchasesException
import com.revenuecat.purchases.kmp.models.StoreTransaction

class SubscriptionRepositoryImpl: SubscriptionRepository {
    override suspend fun fetchCurrentOfferingOrNull(): Offering? {
        return try {
            val offerings = Purchases.sharedInstance.awaitOfferings()
            offerings.current
        } catch (e: PurchasesException) {
            // TODO: Kermit / Napier などでログ
            println("Failed to fetch offerings: ${e.message}")
            throw e
        }
    }

    override suspend fun isSubscribed(entitlementId: String): Boolean {
        return try {
            val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
            customerInfo
                .entitlements[entitlementId]
                ?.isActive == true
        } catch (e: PurchasesException) {
            println("Failed to fetch customer info: ${e.message}")
            throw e
        }
    }

    override suspend fun identifyUser(appUserId: String): SuccessfulLogin {
        return try {
            Purchases.sharedInstance.awaitLogIn(appUserId)
        } catch (e: PurchasesException) {
            throw e
        }
    }

    override suspend fun logout(): CustomerInfo {
        return try {
            Purchases.sharedInstance.awaitLogOut()
        } catch (e: PurchasesException) {
            throw e
        }
    }

    override suspend fun subscribe(packageToPurchase: Package): StoreTransaction {
        return try {
            val result = Purchases.sharedInstance.awaitPurchase(packageToPurchase)
            result.storeTransaction
        } catch (e: PurchasesException) {
            throw e
        }
    }

    override suspend fun restorePurchaseAndRecheckIsSubscribed(entitlementId: String): Boolean {
        return try {
            val info = Purchases.sharedInstance.awaitRestore()
            info.entitlements[entitlementId]?.isActive == true
        } catch (e: PurchasesException) {
            throw e
        }
    }
}