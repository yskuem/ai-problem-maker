package app.yskuem.aimondaimaker.feature.subscription

import app.yskuem.aimondaimaker.core.config.REVENUE_CAT_API_KEY
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure

object RevenueCatInitializer {

    fun configureIfNeeded() {
        Purchases.logLevel = LogLevel.DEBUG

        Purchases.configure(apiKey = REVENUE_CAT_API_KEY) {
            // NOTE : ユーザIDをここでも設定できる
            //appUserId = null
        }
    }
}
