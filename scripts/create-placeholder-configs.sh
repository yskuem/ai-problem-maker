#!/usr/bin/env bash
set -euo pipefail

# Dev host
mkdir -p composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/api/config
echo 'package app.yskuem.aimondaimaker.data.api.config

const val DEV_HOST_STRING = "https://dev.example.com"' > composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/api/config/DevHost.kt
echo 'package app.yskuem.aimondaimaker.data.api.config

const val PROD_HOST_STRING = "https://prod.example.com"' > composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/api/config/ProdHost.kt

# Supabase prod settings
mkdir -p composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/supabase/config
echo 'package app.yskuem.aimondaimaker.data.supabase.config

object SupabaseProdSettings {
    const val URL = "https://example.supabase.co"
    const val ANON = "public-anon-key"
}' > composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/supabase/config/SupabaseProdSettings.kt

# Web quiz domain constants
mkdir -p composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/core/config
echo 'package app.yskuem.aimondaimaker.core.config

const val WEB_QUIZ_APP_DOMAIN = "https://quiz.example.com"
const val WEB_QUIZ_APP_DOMAIN_DEV = "https://dev.quiz.example.com"' > composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/core/config/WebQuizAppDomain.kt

# Admob Android prod configs
mkdir -p composeApp/src/androidMain/kotlin/app/yskuem/aimondaimaker/feature/ad/config
echo 'package app.yskuem.aimondaimaker.feature.ad.config

object AdmobAndroidProdConfigs {
    const val INTERSTITIAL_ID: String = "ca-app-pub-prod-interstitial"
    const val BANNER_ID: String = "ca-app-pub-prod-banner"
}' > composeApp/src/androidMain/kotlin/app/yskuem/aimondaimaker/feature/ad/config/AdmobAndroidProdConfigs.kt

# Admob iOS prod configs
mkdir -p composeApp/src/iosMain/kotlin/app/yskuem/aimondaimaker/feature/ad/config
echo 'package app.yskuem.aimondaimaker.feature.ad.config

object AdmobIosProdConfigs {
    const val INTERSTITIAL_ID: String = "ca-app-pub-prod-interstitial-ios"
    const val BANNER_ID: String = "ca-app-pub-prod-banner-ios"
}' > composeApp/src/iosMain/kotlin/app/yskuem/aimondaimaker/feature/ad/config/AdmobIosProdConfigs.kt


# Android prod strings
mkdir -p composeApp/src/prod/res/values
echo '<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">AI Note Scan</string>
    <string name="admob_id"></string>
</resources>
' > composeApp/src/prod/res/values/strings.xml



# Optional Release xcconfig
mkdir -p iosApp/Configuration
echo '#include? "Config.xcconfig"' > iosApp/Configuration/Release.xcconfig

