package app.yskuem.aimondaimaker.core.util

import android.text.format.DateFormat
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

actual fun LocalDateTime.toLocalizedMonthDay(): String {
    val locale = Locale.getDefault()
    val pattern = DateFormat.getBestDateTimePattern(locale, "MdEEE")
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return date.toJavaLocalDate().format(formatter)
}
