package app.yskuem.aimondaimaker.core.util

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.Foundation.NSArray
import platform.Foundation.NSString
import platform.Foundation.arrayWithObjects

class IosShareManager(
    private val clipboard: IosClipboard,
) : ShareManager {
    override fun copyToClipboard(text: String) {
        clipboard.copyText(text)
    }

    override fun shareText(text: String, title: String?) {
        val items = NSArray.arrayWithObjects(text as NSString, null)
        val activityController = UIActivityViewController(
            activityItems = items,
            applicationActivities = null
        )
        
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            activityController,
            animated = true,
            completion = null
        )
    }

    override fun generateQuizUrl(groupId: String): String {
        // TODO: Replace with actual app URL scheme when available
        return "https://aimondaimaker.app/quiz/$groupId"
    }
}