package app.yskuem.aimondaimaker.core.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime

/**
 * LocalDateTime を「M月d日（曜）」形式の日本語文字列に変換する拡張関数
 */
fun LocalDateTime.toJapaneseMonthDay(): String {
    // 日付部分を取得
    val date = this.date
    // 月と日を取得
    val month = date.monthNumber
    val day = date.dayOfMonth
    // 曜日を日本語略称に変換
    val weekdayJa = date.dayOfWeek.toJapaneseShort()
    // フォーマットして返却
    return "${month}月${day}日（$weekdayJa）"
}

// DayOfWeek から日本語略称へのマッピング
private fun DayOfWeek.toJapaneseShort(): String =
    when (this) {
        DayOfWeek.MONDAY -> "月"
        DayOfWeek.TUESDAY -> "火"
        DayOfWeek.WEDNESDAY -> "水"
        DayOfWeek.THURSDAY -> "木"
        DayOfWeek.FRIDAY -> "金"
        DayOfWeek.SATURDAY -> "土"
        DayOfWeek.SUNDAY -> "日"
        else -> ""
    }
