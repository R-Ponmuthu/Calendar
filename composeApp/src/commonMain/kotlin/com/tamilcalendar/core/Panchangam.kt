package com.tamilcalendar.core

import kotlin.math.floor

/** One panchangam element (tithi / nakshatra / yogam / karanam) active during the day. */
data class Element(
    val index: Int,
    val label: Label,
    /** End moment as JD (UT). */
    val endJd: Double,
) {
    val endDate: SimpleDate get() = SimpleDate.fromJdIst(endJd).first
    val endMinutes: Int get() = SimpleDate.fromJdIst(endJd).second
}

data class TimeRange(val startMin: Int, val endMin: Int) {
    fun text(): String = "${formatMinutes12(startMin)} - ${formatMinutes12(endMin)}"
}

data class TamilDate(val day: Int, val monthIndex: Int, val yearIndex: Int) {
    val month: Label get() = Names.tamilMonths[monthIndex]
    val year: Label get() = Names.tamilYears[yearIndex]
}

data class Lagnam(val rasiIndex: Int, val remainingMinutes: Int) {
    val rasi: Label get() = Names.rasis[rasiIndex]
    /** 1 naazhigai = 24 minutes, 1 vinadi = 24 seconds */
    val naazhigai: Int get() = remainingMinutes / 24
    val vinadi: Int get() = ((remainingMinutes % 24) * 60) / 24
}

data class DayPanchangam(
    val date: SimpleDate,
    val weekday: Label,
    val tamilDate: TamilDate,
    val sunriseMin: Int,
    val sunsetMin: Int,
    val tithis: List<Element>,
    val nakshatras: List<Element>,
    val yogas: List<Element>,
    val karanas: List<Element>,
    val paksha: Label,
    /** Nakshatra prevailing for the major part of the day (used for Naal & Chandrashtamam). */
    val dayStarIndex: Int,
    val moonRasiIndex: Int,
    val chandrashtamamStars: List<Label>,
    val chandrashtamamRasi: Label,
    val naal: Label,
    val lagnam: Lagnam,
    val rahuKalam: TimeRange,
    val yemagandam: TimeRange,
    val kuligai: TimeRange,
    val nallaNeram: List<TimeRange>,
    val gowriNallaNeram: List<TimeRange>,
    val soolam: Label,
    val parigaram: Label,
    /** Tithi index at sunrise / at sunset. */
    val sunriseTithi: Int,
    val sunsetTithi: Int,
    val sunriseJd: Double,
    val nextSunriseJd: Double,
)

object Panchangam {
    private const val TITHI_RATE = 12.19
    private const val MOON_RATE = 13.18
    private const val YOGA_RATE = 14.15

    private fun tithiAngle(jd: Double) = Astro.norm360(Astro.moonLongitude(jd) - Astro.sunLongitude(jd))
    private fun yogaAngle(jd: Double) = Astro.norm360(Astro.siderealMoon(jd) + Astro.siderealSun(jd))

    private val cache = HashMap<Long, DayPanchangam>()

    fun forDate(date: SimpleDate, loc: Location = CHENNAI): DayPanchangam =
        cache.getOrPut(date.epochDay) { compute(date, loc) }

    private fun elements(
        startJd: Double, endJd: Double, segment: Double, count: Int, rate: Double,
        angle: (Double) -> Double, label: (Int) -> Label,
    ): List<Element> {
        val out = ArrayList<Element>()
        var idx = floor(angle(startJd) / segment).toInt() % count
        var cursor = startJd
        while (out.size < 4) {
            val target = ((idx + 1) % count) * segment
            val end = Astro.findAngle(cursor, target, rate, angle)
            out += Element(idx, label(idx), end)
            if (end >= endJd) break
            idx = (idx + 1) % count
            cursor = end + 1e-4
        }
        return out
    }

    /**
     * Tamil month first-days as published by tamildailycalendar.com (Vakya convention) for
     * Dec 2025 – Jan 2027. Pairs of (epochDay of day 1, month index). Outside this range the
     * month is computed astronomically.
     */
    private val defaultMonthStarts: List<Pair<Long, Int>> = listOf(
        SimpleDate(2025, 12, 16) to 8, SimpleDate(2026, 1, 15) to 9, SimpleDate(2026, 2, 13) to 10,
        SimpleDate(2026, 3, 15) to 11, SimpleDate(2026, 4, 14) to 0, SimpleDate(2026, 5, 15) to 1,
        SimpleDate(2026, 6, 15) to 2, SimpleDate(2026, 7, 17) to 3, SimpleDate(2026, 8, 18) to 4,
        SimpleDate(2026, 9, 18) to 5, SimpleDate(2026, 10, 18) to 6, SimpleDate(2026, 11, 17) to 7,
        SimpleDate(2026, 12, 16) to 8, SimpleDate(2027, 1, 15) to 9, SimpleDate(2027, 2, 13) to 10,
    ).map { it.first.epochDay to it.second }

    private var monthStartsOverride: List<Pair<Long, Int>> = defaultMonthStarts

    /**
     * Pins Tamil month first-days from the data files (tamilMonthStarts). Entries from the
     * data files replace built-in ones that fall within 5 days of them.
     */
    fun setMonthStarts(starts: List<Pair<Long, Int>>) {
        val kept = defaultMonthStarts.filter { d -> starts.none { kotlin.math.abs(it.first - d.first) <= 5 } }
        val merged = (kept + starts).distinctBy { it.first }.sortedBy { it.first }
        if (merged != monthStartsOverride) {
            monthStartsOverride = merged
            cache.clear()
        }
    }

    private fun yearIndexFor(date: SimpleDate, sign: Int): Int {
        // Thai/Masi/Panguni (and Margazhi days in January) belong to the year that began last April.
        val gregYearOfStart = if (sign >= 9 || date.month <= 2) date.year - 1 else date.year
        return (((gregYearOfStart - 1987) % 60) + 60) % 60
    }

    fun tamilDate(date: SimpleDate, loc: Location = CHENNAI): TamilDate {
        val ed = date.epochDay
        val ov = monthStartsOverride
        val i = ov.indexOfLast { it.first <= ed }
        // Use a pinned start only when the following month's start is also pinned (no gaps).
        if (i >= 0 && i + 1 < ov.size && ov[i + 1].first - ov[i].first in 27L..33L) {
            val start = ov[i]
            return TamilDate((ed - start.first).toInt() + 1, start.second, yearIndexFor(date, start.second))
        }
        val ref = monthReferenceJd(date, loc)
        val sign = floor(Astro.siderealSun(ref) / 30.0).toInt()
        // Find the sankranti (sun entering this sign) at or before ref
        val sankranti = Astro.findAngle(ref - 33.0, sign * 30.0, 0.9856) { Astro.siderealSun(it) }
        var start = SimpleDate.fromJdIst(sankranti).first
        if (monthReferenceJd(start, loc) < sankranti) start = start.plusDays(1)
        val day = (date.epochDay - start.epochDay).toInt() + 1
        return TamilDate(day, sign, yearIndexFor(date, sign))
    }

    /**
     * Fallback solar-month rule: the month begins on the first day whose sunrise
     * falls after the Sun's entry into the new sidereal sign.
     */
    private fun monthReferenceJd(date: SimpleDate, loc: Location): Double =
        Astro.sunEvent(date, loc, rising = true)

    private val melStars = setOf(3, 5, 7, 11, 20, 21, 22, 23, 25)
    private val keezhStars = setOf(1, 2, 8, 9, 10, 15, 18, 19, 24)

    private fun tr(h1: Int, m1: Int, h2: Int, m2: Int) = TimeRange(h1 * 60 + m1, h2 * 60 + m2)

    // Indexed by weekday 0=Sunday
    private val rahu = listOf(tr(16, 30, 18, 0), tr(7, 30, 9, 0), tr(15, 0, 16, 30), tr(12, 0, 13, 30), tr(13, 30, 15, 0), tr(10, 30, 12, 0), tr(9, 0, 10, 30))
    private val yema = listOf(tr(12, 0, 13, 30), tr(10, 30, 12, 0), tr(9, 0, 10, 30), tr(7, 30, 9, 0), tr(6, 0, 7, 30), tr(15, 0, 16, 30), tr(13, 30, 15, 0))
    private val kuli = listOf(tr(15, 0, 16, 30), tr(13, 30, 15, 0), tr(12, 0, 13, 30), tr(10, 30, 12, 0), tr(9, 0, 10, 30), tr(7, 30, 9, 0), tr(6, 0, 7, 30))
    private val nalla = listOf(
        listOf(tr(7, 45, 8, 45), tr(15, 15, 16, 15)),
        listOf(tr(6, 15, 7, 15), tr(16, 45, 17, 45)),
        listOf(tr(7, 45, 8, 45), tr(16, 45, 17, 45)),
        listOf(tr(9, 15, 10, 15), tr(16, 45, 17, 45)),
        listOf(tr(10, 45, 11, 45), tr(12, 15, 13, 15)),
        listOf(tr(9, 15, 10, 15), tr(16, 45, 17, 45)),
        listOf(tr(7, 45, 8, 45), tr(16, 45, 17, 45)),
    )
    private val gowri = listOf(
        listOf(tr(10, 45, 11, 45), tr(13, 30, 14, 30)),
        listOf(tr(9, 15, 10, 15), tr(19, 30, 20, 30)),
        listOf(tr(10, 45, 11, 45), tr(19, 30, 20, 30)),
        listOf(tr(10, 45, 11, 45), tr(18, 30, 19, 30)),
        listOf(tr(12, 15, 13, 15), tr(18, 30, 19, 30)),
        listOf(tr(12, 15, 13, 15), tr(18, 30, 19, 30)),
        listOf(tr(10, 45, 11, 45), tr(21, 30, 22, 30)),
    )
    private val soolamDir = listOf("W", "E", "N", "N", "S", "W", "E")
    private val parigarams = listOf(
        Label("வெல்லம்", "Jaggery"), Label("தயிர்", "Curd"), Label("பால்", "Milk"), Label("பால்", "Milk"),
        Label("தைலம்", "Oil"), Label("வெல்லம்", "Jaggery"), Label("தயிர்", "Curd"),
    )

    private fun compute(date: SimpleDate, loc: Location): DayPanchangam {
        val sunrise = Astro.sunEvent(date, loc, true)
        val sunset = Astro.sunEvent(date, loc, false)
        val nextSunrise = Astro.sunEvent(date.plusDays(1), loc, true)

        val tithis = elements(sunrise, nextSunrise, 12.0, 30, TITHI_RATE, ::tithiAngle) { Names.tithis[it] }
        val stars = elements(sunrise, nextSunrise, 360.0 / 27, 27, MOON_RATE, { Astro.siderealMoon(it) }) { Names.nakshatras[it] }
        val yogas = elements(sunrise, nextSunrise, 360.0 / 27, 27, YOGA_RATE, ::yogaAngle) { Names.yogas[it] }
        val karanas = elements(sunrise, nextSunrise, 6.0, 60, TITHI_RATE, ::tithiAngle) { Names.karana(it) }

        // Star prevailing for the longest stretch between sunrise and next sunrise
        var best = stars.first().index
        var bestLen = -1.0
        var segStart = sunrise
        for (s in stars) {
            val len = minOf(s.endJd, nextSunrise) - segStart
            if (len > bestLen) { bestLen = len; best = s.index }
            segStart = s.endJd
        }
        val dayStar = best
        val sunriseTithi = floor(tithiAngle(sunrise) / 12.0).toInt() % 30
        val sunsetTithi = floor(tithiAngle(sunset) / 12.0).toInt() % 30

        val moonRasi = floor(Astro.siderealMoon(sunrise + 0.25) / 30.0).toInt() % 12
        // Chandrashtamam: people whose star is 16 positions before today's star (17th star counting back)
        val cStar = ((dayStar - 16) % 27 + 27) % 27
        val cRasiIdx = ((moonRasi - 7) % 12 + 12) % 12
        // All stars falling (fully or partly) in the chandrashtamam rasi
        val cStars = (0 until 27).filter { s ->
            val startDeg = s * 360.0 / 27; val endDeg = startDeg + 360.0 / 27
            val rStart = cRasiIdx * 30.0; val rEnd = rStart + 30.0
            startDeg < rEnd - 1e-9 && endDeg > rStart + 1e-9
        }.sortedBy { if (it == cStar) -1 else it }.map { Names.nakshatras[it] }

        // Lagnam at sunrise and remaining time in it
        val ascRasi = floor(Astro.ascendant(sunrise, loc) / 30.0).toInt()
        var lo = sunrise; var hi = sunrise + 0.25
        repeat(30) {
            val mid = (lo + hi) / 2
            if (floor(Astro.ascendant(mid, loc) / 30.0).toInt() == ascRasi) lo = mid else hi = mid
        }
        val lagnam = Lagnam(ascRasi, ((lo - sunrise) * 1440).toInt())

        val wd = date.dayOfWeek
        val naal = when (dayStar) { in melStars -> Names.melNokku; in keezhStars -> Names.keezhNokku; else -> Names.samaNokku }
        return DayPanchangam(
            date = date,
            weekday = Names.weekdays[wd],
            tamilDate = tamilDate(date, loc),
            sunriseMin = SimpleDate.fromJdIst(sunrise).second,
            sunsetMin = SimpleDate.fromJdIst(sunset).second,
            tithis = tithis, nakshatras = stars, yogas = yogas, karanas = karanas,
            paksha = if (sunriseTithi < 15) Names.valarpirai else Names.theipirai,
            dayStarIndex = dayStar,
            moonRasiIndex = moonRasi,
            chandrashtamamStars = cStars,
            chandrashtamamRasi = Names.rasis[cRasiIdx],
            naal = naal,
            lagnam = lagnam,
            rahuKalam = rahu[wd], yemagandam = yema[wd], kuligai = kuli[wd],
            nallaNeram = nalla[wd], gowriNallaNeram = gowri[wd],
            soolam = Names.directions.getValue(soolamDir[wd]),
            parigaram = parigarams[wd],
            sunriseTithi = sunriseTithi, sunsetTithi = sunsetTithi,
            sunriseJd = sunrise, nextSunriseJd = nextSunrise,
        )
    }

}
