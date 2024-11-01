package com.alexvolkov.dazntestapp.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

object Utils {
    fun parseDate(dateString: String): Date {
        val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(dateString, formatter)
        return Date.from(zonedDateTime.toInstant())
    }

    fun formatDate(date: Date): String {
        val now = LocalDate.now()
        val targetDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val targetDateTime = date.toInstant().atZone(ZoneId.systemDefault())
        val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(now, targetDate)

        return when (daysBetween) {
            -1L -> "Yesterday, ${DateTimeFormatter.ofPattern("HH:mm").format(targetDateTime)}"
            0L -> "Today, ${DateTimeFormatter.ofPattern("HH:mm").format(targetDateTime)}"
            1L -> "Tomorrow, ${DateTimeFormatter.ofPattern("HH:mm").format(targetDateTime)}"
            2L -> "In two days"
            3L -> "In three days"
            else -> DateTimeFormatter.ofPattern("dd.MM.yyyy").format(targetDate)
        }
    }
}