package app.yskuem.aimondaimaker.core.util

import platform.UIKit.UIPasteboard

class IosClipboard : Clipboard {
    override fun copyText(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
}
