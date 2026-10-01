package com.tamilcalendar.core

/**
 * Minimal, dependency-free civil date (proleptic Gregorian) used across all platforms.
 * All calendar times in this app are in Indian Standard Time (UTC+05:30).
 */
data class SimpleDate(val year: Int, val month: Int, val day: Int) : Comparable<SimpleDate> {

    /** Days since 1970-01-01. */
    val epochDay: Long get() = daysFromCivil(year, month, day)

    /** 0 = Sunday ... 6 = Saturday */
    val dayOfWeek: Int get() = (((epochDay + 4) % 7 + 7) % 7).toInt()

    fun plusDays(n: Int): SimpleDate = fromEpochDay(epochDay + n)

    /** Julian Day number at 00:00 IST of this date (expressed in UT). */
    val jdMidnightIst: Double get() = epochDay + 2440587.5 - IST_OFFSET_DAYS

    override fun compareTo(other: SimpleDate): Int = epochDay.compareTo(other.epochDay)

    fun iso(): String = "$year-${month.pad2()}-${day.pad2()}"

    companion object {
        const val IST_OFFSET_DAYS = 5.5 / 24.0

        fun fromEpochDay(z0: Long): SimpleDate {
            val z = z0 + 719468
            val era = (if (z >= 0) z else z - 146096) / 146097
            val doe = z - era * 146097
            val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
            val y = yoe + era * 400
            val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
            val mp = (5 * doy + 2) / 153
            val d = doy - (153 * mp + 2) / 5 + 1
            val m = if (mp < 10) mp + 3 else mp - 9
            return SimpleDate((if (m <= 2) y + 1 else y).toInt(), m.toInt(), d.toInt())
        }

        fun daysFromCivil(y0: Int, m: Int, d: Int): Long {
            val y = (if (m <= 2) y0 - 1 else y0).toLong()
            val era = (if (y >= 0) y else y - 399) / 400
            val yoe = y - era * 400
            val mm = m.toLong()
            val doy = (153 * (if (mm > 2) mm - 3 else mm + 9) + 2) / 5 + d - 1
            val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
            return era * 146097 + doe - 719468
        }

        /** Parses "YYYY-MM-DD"; returns null when malformed. */
        fun parseIso(s: String): SimpleDate? {
            val parts = s.trim().split('-')
            if (parts.size != 3) return null
            val y = parts[0].toIntOrNull() ?: return null
            val m = parts[1].toIntOrNull() ?: return null
            val d = parts[2].toIntOrNull() ?: return null
            if (m !in 1..12 || d !in 1..daysInMonth(y, m)) return null
            return SimpleDate(y, m, d)
        }

        fun daysInMonth(year: Int, month: Int): Int = when (month) {
            2 -> if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }

        /** Converts a Julian Day (UT) into IST date + minutes after IST midnight. */
        fun fromJdIst(jd: Double): Pair<SimpleDate, Int> {
            val local = jd + IST_OFFSET_DAYS - 2440587.5
            var dayNum = kotlin.math.floor(local).toLong()
            var minutes = kotlin.math.round((local - dayNum) * 1440.0).toInt()
            if (minutes >= 1440) { minutes -= 1440; dayNum += 1 }
            return fromEpochDay(dayNum) to minutes
        }
    }
}

fun Int.pad2(): String = if (this < 10) "0$this" else "$this"

/** Formats minutes-after-midnight as 12-hour clock text, e.g. "02:55 PM". */
fun formatMinutes12(totalMinutes: Int): String {
    val m = ((totalMinutes % 1440) + 1440) % 1440
    val h24 = m / 60
    val mm = m % 60
    val ampm = if (h24 < 12) "AM" else "PM"
    val h12 = when {
        h24 == 0 -> 12
        h24 > 12 -> h24 - 12
        else -> h24
    }
    return "${h12.pad2()}:${mm.pad2()} $ampm"
}
