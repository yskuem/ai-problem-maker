package app.yskuem.aimondaimaker.core.config

import app.yskuem.aimondaimaker.BuildConfig

actual fun getFlavor(): Flavor {
    val flavor = BuildConfig.FLAVOR
    return getFlavorFromString(flavor)
}