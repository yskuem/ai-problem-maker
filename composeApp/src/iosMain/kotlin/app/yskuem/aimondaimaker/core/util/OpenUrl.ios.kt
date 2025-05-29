package app.yskuem.aimondaimaker.core.util

import platform.Foundation.NSURL
import platform.UIKit.UIApplication


class IosOpenUrl : OpenUrl {
    override fun handle(url: String) {
        val nsUrl = NSURL(string = url)
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}