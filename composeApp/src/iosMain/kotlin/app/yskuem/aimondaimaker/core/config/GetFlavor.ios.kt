package app.yskuem.aimondaimaker.core.config

import platform.Foundation.NSBundle

actual fun getFlavor(): Flavor {
    val dict = NSBundle.mainBundle.infoDictionary
    val flavor =  dict?.get("Flavor") as String?
    return getFlavorFromString(flavor)
}