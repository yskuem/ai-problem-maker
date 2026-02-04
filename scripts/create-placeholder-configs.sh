#!/usr/bin/env bash
set -euo pipefail

write_if_missing() {
  local path="$1"
  shift
  if [ -f "$path" ]; then
    return 0
  fi
  mkdir -p "$(dirname "$path")"
  printf '%s\n' "$1" > "$path"
}

# Dev/Prod host
write_if_missing composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/api/config/DevHost.kt \
'package app.yskuem.aimondaimaker.data.api.config

const val DEV_HOST_STRING = "https://dev.example.com"'

write_if_missing composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/api/config/ProdHost.kt \
'package app.yskuem.aimondaimaker.data.api.config

const val PROD_HOST_STRING = "https://prod.example.com"'

write_if_missing composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/api/config/ApiConfig.kt \
'package app.yskuem.aimondaimaker.data.api.config

object ApiConfig {
    const val DEV_HOST = DEV_HOST_STRING
    const val PROD_HOST = PROD_HOST_STRING
}'

# Supabase prod settings
write_if_missing composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/data/supabase/config/SupabaseProdSettings.kt \
'package app.yskuem.aimondaimaker.data.supabase.config

object SupabaseProdSettings {
    const val URL = "https://example.supabase.co"
    const val ANON = "public-anon-key"
}'

# Web quiz domain constants
write_if_missing composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/core/config/WebQuizAppDomain.kt \
'package app.yskuem.aimondaimaker.core.config

const val WEB_QUIZ_APP_DOMAIN = "https://quiz.example.com"
const val WEB_QUIZ_APP_DOMAIN_DEV = "https://dev.quiz.example.com"'

# Admob Android prod configs
write_if_missing composeApp/src/androidMain/kotlin/app/yskuem/aimondaimaker/feature/ad/config/AdmobAndroidProdConfigs.kt \
'package app.yskuem.aimondaimaker.feature.ad.config

object AdmobAndroidProdConfigs {
    const val INTERSTITIAL_ID: String = "ca-app-pub-prod-interstitial"
    const val BANNER_ID: String = "ca-app-pub-prod-banner"
}'

# Admob iOS prod configs
write_if_missing composeApp/src/iosMain/kotlin/app/yskuem/aimondaimaker/feature/ad/config/AdmobIosProdConfigs.kt \
'package app.yskuem.aimondaimaker.feature.ad.config

object AdmobIosProdConfigs {
    const val INTERSTITIAL_ID: String = "ca-app-pub-prod-interstitial-ios"
    const val BANNER_ID: String = "ca-app-pub-prod-banner-ios"
}'

# RevenueCat API key
write_if_missing composeApp/src/androidMain/kotlin/app/yskuem/aimondaimaker/core/config/RevenueCatApiKey.android.kt \
'package app.yskuem.aimondaimaker.core.config

actual val REVENUE_CAT_API_KEY: String = "revenuecat-public-key"'

write_if_missing composeApp/src/iosMain/kotlin/app/yskuem/aimondaimaker/core/config/RevenueCatApiKey.ios.kt \
'package app.yskuem.aimondaimaker.core.config

actual val REVENUE_CAT_API_KEY: String = "revenuecat-public-key"'


# Android prod strings
if [ ! -f composeApp/src/prod/res/values/strings.xml ]; then
  mkdir -p composeApp/src/prod/res/values
  cat > composeApp/src/prod/res/values/strings.xml <<'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">AI Note Scan</string>
    <string name="admob_id"></string>
</resources>
EOF
fi



# Optional Release xcconfig
if [ ! -f iosApp/Configuration/Release.xcconfig ]; then
  mkdir -p iosApp/Configuration
  echo '#include? "Config.xcconfig"' > iosApp/Configuration/Release.xcconfig
fi

# PDF generate base URL
write_if_missing composeApp/src/commonMain/kotlin/app/yskuem/aimondaimaker/feature/quiz/config/PdfGenerateUrl.kt \
'package app.yskuem.aimondaimaker.feature.quiz.config

const val GENERATE_PDF_BASE_UEL = "https://generate-pdf.example.com"'
