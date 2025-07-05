package app.yskuem.aimondaimaker.core.util

import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeExtensionTest {

    @Test
    fun `should format LocalDateTime to Japanese month day format`() {
        // Given
        val dateTime = LocalDateTime(2024, 1, 15, 10, 30, 0) // Monday

        // When
        val result = dateTime.toJapaneseMonthDay()

        // Then
        assertEquals("1月15日（月）", result)
    }

    @Test
    fun `should format all weekdays correctly`() {
        // Given & When & Then
        val mondayDateTime = LocalDateTime(2024, 1, 15, 10, 30, 0) // Monday
        assertEquals("1月15日（月）", mondayDateTime.toJapaneseMonthDay())

        val tuesdayDateTime = LocalDateTime(2024, 1, 16, 10, 30, 0) // Tuesday
        assertEquals("1月16日（火）", tuesdayDateTime.toJapaneseMonthDay())

        val wednesdayDateTime = LocalDateTime(2024, 1, 17, 10, 30, 0) // Wednesday
        assertEquals("1月17日（水）", wednesdayDateTime.toJapaneseMonthDay())

        val thursdayDateTime = LocalDateTime(2024, 1, 18, 10, 30, 0) // Thursday
        assertEquals("1月18日（木）", thursdayDateTime.toJapaneseMonthDay())

        val fridayDateTime = LocalDateTime(2024, 1, 19, 10, 30, 0) // Friday
        assertEquals("1月19日（金）", fridayDateTime.toJapaneseMonthDay())

        val saturdayDateTime = LocalDateTime(2024, 1, 20, 10, 30, 0) // Saturday
        assertEquals("1月20日（土）", saturdayDateTime.toJapaneseMonthDay())

        val sundayDateTime = LocalDateTime(2024, 1, 21, 10, 30, 0) // Sunday
        assertEquals("1月21日（日）", sundayDateTime.toJapaneseMonthDay())
    }

    @Test
    fun `should handle different months correctly`() {
        // Given & When & Then
        val januaryDateTime = LocalDateTime(2024, 1, 1, 10, 30, 0)
        assertEquals("1月1日（月）", januaryDateTime.toJapaneseMonthDay())

        val februaryDateTime = LocalDateTime(2024, 2, 1, 10, 30, 0)
        assertEquals("2月1日（木）", februaryDateTime.toJapaneseMonthDay())

        val marchDateTime = LocalDateTime(2024, 3, 1, 10, 30, 0)
        assertEquals("3月1日（金）", marchDateTime.toJapaneseMonthDay())

        val decemberDateTime = LocalDateTime(2024, 12, 31, 10, 30, 0)
        assertEquals("12月31日（火）", decemberDateTime.toJapaneseMonthDay())
    }

    @Test
    fun `should handle single digit days correctly`() {
        // Given
        val dateTime = LocalDateTime(2024, 1, 5, 10, 30, 0) // Friday

        // When
        val result = dateTime.toJapaneseMonthDay()

        // Then
        assertEquals("1月5日（金）", result)
    }

    @Test
    fun `should handle double digit days correctly`() {
        // Given
        val dateTime = LocalDateTime(2024, 1, 25, 10, 30, 0) // Thursday

        // When
        val result = dateTime.toJapaneseMonthDay()

        // Then
        assertEquals("1月25日（木）", result)
    }

    @Test
    fun `should handle leap year February correctly`() {
        // Given
        val dateTime = LocalDateTime(2024, 2, 29, 10, 30, 0) // Leap year February 29th - Thursday

        // When
        val result = dateTime.toJapaneseMonthDay()

        // Then
        assertEquals("2月29日（木）", result)
    }

    @Test
    fun `should ignore time portion and only format date`() {
        // Given
        val morningDateTime = LocalDateTime(2024, 1, 15, 9, 0, 0) // Monday morning
        val eveningDateTime = LocalDateTime(2024, 1, 15, 21, 30, 45) // Monday evening

        // When
        val morningResult = morningDateTime.toJapaneseMonthDay()
        val eveningResult = eveningDateTime.toJapaneseMonthDay()

        // Then
        assertEquals("1月15日（月）", morningResult)
        assertEquals("1月15日（月）", eveningResult)
        assertEquals(morningResult, eveningResult)
    }

    @Test
    fun `should handle New Year's Day correctly`() {
        // Given
        val newYearDateTime = LocalDateTime(2024, 1, 1, 0, 0, 0) // New Year's Day - Monday

        // When
        val result = newYearDateTime.toJapaneseMonthDay()

        // Then
        assertEquals("1月1日（月）", result)
    }

    @Test
    fun `should handle different years correctly`() {
        // Given
        val date2023 = LocalDateTime(2023, 12, 31, 10, 30, 0) // Sunday
        val date2024 = LocalDateTime(2024, 1, 1, 10, 30, 0) // Monday

        // When
        val result2023 = date2023.toJapaneseMonthDay()
        val result2024 = date2024.toJapaneseMonthDay()

        // Then
        assertEquals("12月31日（日）", result2023)
        assertEquals("1月1日（月）", result2024)
    }
}