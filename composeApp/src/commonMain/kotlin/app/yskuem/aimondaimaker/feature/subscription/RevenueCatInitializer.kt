package app.yskuem.aimondaimaker.feature.subscription

import app.yskuem.aimondaimaker.core.config.Flavor
import app.yskuem.aimondaimaker.core.config.REVENUE_CAT_API_KEY
import app.yskuem.aimondaimaker.core.config.getFlavor
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure

object RevenueCatInitializer {

    fun configureIfNeeded() {
        if(getFlavor() != Flavor.PROD) {
            // アプリケーションIDが異なるためクラッシュするのでとりあえずPROD以外では無効化
            return
        }
        Purchases.logLevel = LogLevel.DEBUG

        Purchases.configure(apiKey = REVENUE_CAT_API_KEY) {
            // NOTE : ユーザIDをここでも設定できる
            //appUserId = null
        }
    }
}
