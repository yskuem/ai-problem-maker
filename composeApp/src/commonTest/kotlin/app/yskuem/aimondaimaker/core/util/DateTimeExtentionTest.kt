package app.yskuem.aimondaimaker.core.util

import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeExtentionTest {
    @Test
    fun testToJapaneseMonthDay() {
        val dt = LocalDateTime(2024, 3, 15, 0, 0)
        assertEquals("3月15日（金）", dt.toJapaneseMonthDay())
    }
}
