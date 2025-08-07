package app.yskuem.aimondaimaker.core.config

fun getWebQuizAppDomain(): String {
    return when(getFlavor()) {
        Flavor.DEV -> WEB_QUIZ_APP_DOMAIN_DEV
        Flavor.STAGING -> WEB_QUIZ_APP_DOMAIN_DEV
        Flavor.PROD -> WEB_QUIZ_APP_DOMAIN
    }
}