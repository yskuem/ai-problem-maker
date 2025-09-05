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

# Google services for prod
mkdir -p composeApp/src/prod
echo '{
  "project_info": {
    "project_number": "000000000000",
    "project_id": "placeholder-prod",
    "storage_bucket": "placeholder-prod.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:000000000000:android:0000000000000000",
        "android_client_info": {
          "package_name": "app.yskuem.aimondaimaker"
        }
      },
      "api_key": [
        {
          "current_key": "AIzaSyPlaceholder"
        }
      ]
    }
  ],
  "configuration_version": "1"
}' > composeApp/src/prod/google-services.json

# Android prod strings
mkdir -p composeApp/src/prod/res/values
echo '<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">AI Note Scan</string>
    <string name="admob_id"></string>
</resources>
' > composeApp/src/prod/res/values/strings.xml

# key.properties
echo 'storePassword=dummy
keyPassword=dummy
keyAlias=dummy
storeFile=dummy.jks' > key.properties

# iOS GoogleService files
mkdir -p iosApp/iosApp
echo '<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>GOOGLE_APP_ID</key>
    <string>1:000000000000:ios:0000000000000000</string>
    <key>GCM_SENDER_ID</key>
    <string>000000000000</string>
    <key>API_KEY</key>
    <string>AIzaSyIOSPlaceholder</string>
    <key>PROJECT_ID</key>
    <string>placeholder-prod</string>
</dict>
</plist>
' > iosApp/iosApp/GoogleService-Info.plist

mkdir -p iosApp/GoogleService/prod
echo '<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>GOOGLE_APP_ID</key>
    <string>1:000000000000:ios:0000000000000000</string>
    <key>GCM_SENDER_ID</key>
    <string>000000000000</string>
    <key>API_KEY</key>
    <string>AIzaSyIOSPlaceholder</string>
    <key>PROJECT_ID</key>
    <string>placeholder-prod</string>
</dict>
</plist>
' > iosApp/GoogleService/prod/GoogleService-Info.plist

# Optional Release xcconfig
mkdir -p iosApp/Configuration
echo '#include? "Config.xcconfig"' > iosApp/Configuration/Release.xcconfig

