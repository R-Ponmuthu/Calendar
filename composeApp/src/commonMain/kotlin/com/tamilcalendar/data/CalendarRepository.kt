package com.tamilcalendar.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tamilcalendar.core.DayPanchangam
import com.tamilcalendar.core.Label
import com.tamilcalendar.core.Panchangam
import com.tamilcalendar.core.SimpleDate
import com.tamilcalendar.realTodayIst

/**
 * Single source of truth for the UI. Event data comes from the yearly JSON files
 * ([DataLoader]); panchangam, viratham and chandrashtamam are computed for any year.
 */
object CalendarRepository {
    /** Compose state: screens recompose automatically when new data is applied. */
    var dataset: Dataset by mutableStateOf(Dataset.EMPTY)
        private set

    /** Must be called on the main thread. */
    fun apply(ds: Dataset) {
        Panchangam.setMonthStarts(ds.monthStarts)
        dataset = ds
    }

    /** Years the app lets the user browse: every year with data, plus the current year. */
    val years: IntRange
        get() {
            val all = dataset.dataYears + realTodayIst().year
            return all.min()..all.max()
        }

    val firstDay: SimpleDate get() = SimpleDate(years.first, 1, 1)
    val lastDay: SimpleDate get() = SimpleDate(years.last, 12, 31)

    fun hasData(year: Int) = year in dataset.dataYears

    fun panchangam(date: SimpleDate): DayPanchangam = Panchangam.forDate(date)

    fun days(year: Int): List<SimpleDate> {
        val first = SimpleDate(year, 1, 1)
        val count = (SimpleDate(year, 12, 31).epochDay - first.epochDay).toInt()
        return (0..count).map { first.plusDays(it) }
    }

    private val virathamCache = HashMap<Int, List<CalendarEvent>>()
    private val virathamByDay = HashMap<Long, List<CalendarEvent>>()

    /** Computed monthly viratham days (Thirukanitha): Ekadasi, Sashti, Sankatahara Chathurthi, etc. */
    fun viratham(year: Int): List<CalendarEvent> = virathamCache.getOrPut(year) {
        val out = ArrayList<CalendarEvent>()
        val before = panchangam(SimpleDate(year, 1, 1).plusDays(-1))
        var prevRise = before.sunriseTithi
        var prevStar = before.dayStarIndex
        var prevSet = before.sunsetTithi
        for (d in days(year)) {
            val p = panchangam(d)
            fun add(ta: String, en: String) = out.add(CalendarEvent(d, EventType.VIRATHAM, Label(ta, en)))
            if ((p.sunriseTithi == 10 || p.sunriseTithi == 25) && prevRise != p.sunriseTithi) add("ஏகாதசி", "Ekadasi")
            if (p.sunriseTithi == 5 && prevRise != 5) add("சஷ்டி விரதம்", "Sashti Viratham")
            if (p.sunsetTithi == 18 && prevSet != 18) add("சங்கடஹர சதுர்த்தி", "Sankatahara Chathurthi")
            if (p.sunsetTithi == 28 && prevSet != 28) add("மாத சிவராத்திரி", "Masa Sivarathri")
            if (p.sunriseTithi == 3 && prevRise != 3) add("சதுர்த்தி", "Chathurthi")
            if (p.dayStarIndex == 2 && prevStar != 2) add("கிருத்திகை விரதம்", "Karthigai Viratham")
            if (p.dayStarIndex == 21 && prevStar != 21) add("திருவோணம் விரதம்", "Thiruvonam Viratham")
            prevRise = p.sunriseTithi; prevStar = p.dayStarIndex; prevSet = p.sunsetTithi
        }
        out.groupBy { it.date.epochDay }.forEach { (k, v) -> virathamByDay[k] = v }
        out
    }

    /** Data-file events of the given types for a year. */
    fun events(year: Int, vararg types: EventType): List<CalendarEvent> =
        dataset.events.filter { it.date.year == year && (types.isEmpty() || it.type in types) }

    fun eventsOn(date: SimpleDate): List<CalendarEvent> {
        viratham(date.year) // ensures the year's viratham index is built
        return dataset.byDay[date.epochDay].orEmpty() + virathamByDay[date.epochDay].orEmpty()
    }

    fun isHoliday(date: SimpleDate) = dataset.byDay[date.epochDay].orEmpty().any { it.type == EventType.GOVT_HOLIDAY }
    fun isMuhurtham(date: SimpleDate) = dataset.byDay[date.epochDay].orEmpty().any { it.type == EventType.MUHURTHAM }

    /** Days of [year] on which the Moon transits the 8th sign from [rasiIndex] (0 = Mesham). */
    fun chandrashtamamDays(rasiIndex: Int, year: Int): List<SimpleDate> {
        val target = (rasiIndex + 7) % 12
        return days(year).filter { panchangam(it).moonRasiIndex == target }
    }

    /** Hand-written palan from the data files, if any. */
    fun palanOverride(date: SimpleDate, rasi: Int): PalanText? = dataset.palanFor(date, rasi)

    /** One-word palan for [rasi] on [date] (hand-written word if published, else computed). */
    fun palanWord(date: SimpleDate, rasi: Int, transit: com.tamilcalendar.core.RasiPalan.MoonTransit): Label =
        palanOverride(date, rasi)?.word
            ?: com.tamilcalendar.core.RasiPalan.oneWord(com.tamilcalendar.core.RasiPalan.forRasi(panchangam(date), rasi, transit))
}
