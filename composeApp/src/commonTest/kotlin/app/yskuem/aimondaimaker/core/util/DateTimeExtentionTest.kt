package app.yskuem.aimondaimaker.core.util

import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeExtentionTest {

    @Test
    fun `should format Monday correctly`() {
        // 2024年1月1日は月曜日
        val dateTime = LocalDateTime(2024, 1, 1, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("1月1日（月）", result)
    }

    @Test
    fun `should format Tuesday correctly`() {
        // 2024年1月2日は火曜日
        val dateTime = LocalDateTime(2024, 1, 2, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("1月2日（火）", result)
    }

    @Test
    fun `should format Wednesday correctly`() {
        // 2024年1月3日は水曜日
        val dateTime = LocalDateTime(2024, 1, 3, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("1月3日（水）", result)
    }

    @Test
    fun `should format Thursday correctly`() {
        // 2024年1月4日は木曜日
        val dateTime = LocalDateTime(2024, 1, 4, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("1月4日（木）", result)
    }

    @Test
    fun `should format Friday correctly`() {
        // 2024年1月5日は金曜日
        val dateTime = LocalDateTime(2024, 1, 5, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("1月5日（金）", result)
    }

    @Test
    fun `should format Saturday correctly`() {
        // 2024年1月6日は土曜日
        val dateTime = LocalDateTime(2024, 1, 6, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("1月6日（土）", result)
    }

    @Test
    fun `should format Sunday correctly`() {
        // 2024年1月7日は日曜日
        val dateTime = LocalDateTime(2024, 1, 7, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("1月7日（日）", result)
    }

    @Test
    fun `should format double digit month and day correctly`() {
        // 2024年12月31日
        val dateTime = LocalDateTime(2024, 12, 31, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("12月31日（火）", result)
    }

    @Test
    fun `should format February 29th in leap year correctly`() {
        // 2024年2月29日（うるう年）
        val dateTime = LocalDateTime(2024, 2, 29, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("2月29日（木）", result)
    }

    @Test
    fun `should format March 1st correctly`() {
        // 2024年3月1日
        val dateTime = LocalDateTime(2024, 3, 1, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("3月1日（金）", result)
    }

    @Test
    fun `should format New Year's Day correctly`() {
        // 2025年1月1日
        val dateTime = LocalDateTime(2025, 1, 1, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("1月1日（水）", result)
    }

    @Test
    fun `should format mid-year date correctly`() {
        // 2024年6月15日
        val dateTime = LocalDateTime(2024, 6, 15, 12, 0, 0)
        val result = dateTime.toJapaneseMonthDay()
        assertEquals("6月15日（土）", result)
    }

    @Test
    fun `should format date independent of time`() {
        // 同じ日付でも時間が異なる場合の確認
        val dateTime1 = LocalDateTime(2024, 5, 10, 0, 0, 0)
        val dateTime2 = LocalDateTime(2024, 5, 10, 23, 59, 59)
        
        val result1 = dateTime1.toJapaneseMonthDay()
        val result2 = dateTime2.toJapaneseMonthDay()
        
        assertEquals("5月10日（金）", result1)
        assertEquals("5月10日（金）", result2)
        assertEquals(result1, result2)
    }
}