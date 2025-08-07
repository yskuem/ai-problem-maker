package app.yskuem.aimondaimaker

import platform.UIKit.UIDevice
import platform.UIKit.UIUserInterfaceIdiomPad

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val isIPad: Boolean = UIDevice.currentDevice.userInterfaceIdiom == UIUserInterfaceIdiomPad
}

actual fun getPlatform(): Platform = IOSPlatform()
