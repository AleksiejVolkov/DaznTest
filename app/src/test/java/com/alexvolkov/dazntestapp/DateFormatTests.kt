package com.alexvolkov.dazntestapp

import com.alexvolkov.dazntestapp.util.Utils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class DateFormatTests {

    @Test
    fun `test formatDate for yesterday`() {
        val now = LocalDate.now()
        val yesterday = Date.from(now.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val formattedDate = Utils.formatDate(yesterday)
        assertEquals("Yesterday, 00:00", formattedDate)
    }

    @Test
    fun `test formatDate for today`() {
        val now = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        val formattedDate = Utils.formatDate(now)
        assertEquals("Today, 00:00", formattedDate)
    }

    @Test
    fun `test formatDate for tomorrow`() {
        val now = LocalDate.now()
        val tomorrow = Date.from(now.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val formattedDate = Utils.formatDate(tomorrow)
        assertEquals("Tomorrow, 00:00", formattedDate)
    }

    @Test
    fun `test formatDate for in two days`() {
        val now = LocalDate.now()
        val inTwoDays = Date.from(now.plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val formattedDate = Utils.formatDate(inTwoDays)
        assertEquals("In two days", formattedDate)
    }

    @Test
    fun `test formatDate for in three days`() {
        val now = LocalDate.now()
        val inThreeDays = Date.from(now.plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val formattedDate = Utils.formatDate(inThreeDays)
        assertEquals("In three days", formattedDate)
    }

    @Test
    fun `test formatDate for other dates`() {
        val now = LocalDate.now()
        val otherDate = Date.from(now.plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant())
        val formattedDate = Utils.formatDate(otherDate)
        val expectedDate = now.plusDays(10).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        assertEquals(expectedDate, formattedDate)
    }
}