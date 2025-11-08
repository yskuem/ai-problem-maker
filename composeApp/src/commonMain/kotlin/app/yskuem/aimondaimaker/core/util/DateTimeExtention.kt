package app.yskuem.aimondaimaker.core.util

import kotlinx.datetime.LocalDateTime

/**
 * Formats a [LocalDateTime] as a month/day with weekday string using the current device locale.
 */
expect fun LocalDateTime.toLocalizedMonthDay(): String
