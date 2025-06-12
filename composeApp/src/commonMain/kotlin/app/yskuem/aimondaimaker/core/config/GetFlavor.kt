package app.yskuem.aimondaimaker.core.config

expect fun getFlavor(): Flavor

fun getFlavorFromString(flavor: String?): Flavor =
    when (flavor) {
        "dev" -> Flavor.DEV
        "staging" -> Flavor.STAGING
        "prod" -> Flavor.PROD
        else -> throw Throwable("Flavor not found in Info.plist")
    }

enum class Flavor {
    DEV,
    STAGING,
    PROD,
}
