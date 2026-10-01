package com.tamilcalendar

import com.tamilcalendar.core.SimpleDate
import com.tamilcalendar.data.CalendarRepository

/** Milliseconds since the Unix epoch (UTC). */
expect fun currentEpochMillis(): Long

/** Today's date in IST. */
fun realTodayIst(): SimpleDate {
    val istMillis = currentEpochMillis() + 330L * 60_000L
    return SimpleDate.fromEpochDay(istMillis.floorDiv(86_400_000L))
}

/** Today's date in IST, clamped to the range of years the app shows. */
fun todayIst(): SimpleDate {
    val today = realTodayIst()
    return when {
        today < CalendarRepository.firstDay -> CalendarRepository.firstDay
        today > CalendarRepository.lastDay -> CalendarRepository.lastDay
        else -> today
    }
}
