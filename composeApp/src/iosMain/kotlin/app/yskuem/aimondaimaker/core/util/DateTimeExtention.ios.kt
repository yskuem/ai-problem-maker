package app.yskuem.aimondaimaker.core.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone

actual fun LocalDateTime.toLocalizedMonthDay(): String {
    val formatter = NSDateFormatter().apply {
        locale = NSLocale.autoupdatingCurrentLocale
        setLocalizedDateFormatFromTemplate("MdEEE")
        timeZone = NSTimeZone.localTimeZone
    }

    val date = toInstant(TimeZone.currentSystemDefault()).toNSDate()
    return formatter.stringFromDate(date)
}
