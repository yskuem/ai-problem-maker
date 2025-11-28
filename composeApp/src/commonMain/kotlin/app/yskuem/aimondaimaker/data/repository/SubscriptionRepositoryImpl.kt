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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class SubscriptionRepositoryImpl : SubscriptionRepository {
    private val _customerInfo = MutableStateFlow<CustomerInfo?>(null)

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

    override fun isSubscribed(entitlementId: String): Flow<Boolean> {
        return _customerInfo
            .onStart {
                fetchCustomerInfo()
            }
            .map { info ->
                info?.entitlements?.get(entitlementId)?.isActive == true
            }
    }

    private suspend fun fetchCustomerInfo() {
        try {
            val info = Purchases.sharedInstance.awaitCustomerInfo()
            _customerInfo.update { info }
        } catch (e: PurchasesException) {
            println("Failed to fetch customer info: ${e.message}")
        }
    }

    override suspend fun identifyUser(appUserId: String): SuccessfulLogin {
        return try {
            val result = Purchases.sharedInstance.awaitLogIn(appUserId)
            _customerInfo.update { result.customerInfo }
            result
        } catch (e: PurchasesException) {
            throw e
        }
    }

    override suspend fun logout(): CustomerInfo {
        return try {
            val info = Purchases.sharedInstance.awaitLogOut()
            _customerInfo.update { info }
            info
        } catch (e: PurchasesException) {
            throw e
        }
    }

    override suspend fun subscribe(packageToPurchase: Package): StoreTransaction {
        return try {
            val result = Purchases.sharedInstance.awaitPurchase(packageToPurchase)
            _customerInfo.update { result.customerInfo }
            result.storeTransaction
        } catch (e: PurchasesException) {
            throw e
        }
    }

    override suspend fun restorePurchaseAndRecheckIsSubscribed(entitlementId: String): Boolean {
        return try {
            val info = Purchases.sharedInstance.awaitRestore()
            _customerInfo.update { info }
            info.entitlements[entitlementId]?.isActive == true
        } catch (e: PurchasesException) {
            throw e
        }
    }
}