package com.tamilcalendar

import com.tamilcalendar.core.Panchangam
import com.tamilcalendar.core.SimpleDate
import com.tamilcalendar.data.CalendarRepository
import com.tamilcalendar.core.Match
import com.tamilcalendar.core.Porutham
import com.tamilcalendar.core.RasiPalan
import com.tamilcalendar.data.DataJson
import com.tamilcalendar.data.Dataset
import com.tamilcalendar.data.EventType
import com.tamilcalendar.data.YearFileDto
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PanchangamTest {
    /** Reference values from tamildailycalendar.com for 01-Oct-2026. */
    @Test
    fun october1st2026MatchesReference() {
        val p = Panchangam.forDate(SimpleDate(2026, 10, 1))
        assertEquals(14, p.tamilDate.day)
        assertEquals("புரட்டாசி", p.tamilDate.month.ta)
        assertEquals("பராபவ", p.tamilDate.year.ta)
        assertEquals("வியாழன்", p.weekday.ta)
        assertEquals("சுவாதி", p.chandrashtamamStars.first().ta)
        assertEquals("கன்னி", p.lagnam.rasi.ta)
        assertEquals("மேல் நோக்கு நாள்", p.naal.ta)
        assertEquals("தெற்கு", p.soolam.ta)
        assertEquals("01:30 PM - 03:00 PM", p.rahuKalam.text())
    }

    @Test
    fun newMoonOct2026Time() {
        // Astronomical new moon: 10-Oct-2026 15:50 UTC = 21:20 IST
        val p = Panchangam.forDate(SimpleDate(2026, 10, 10))
        val amavasai = p.tithis.first { it.index == 29 }
        assertTrue(abs(amavasai.endMinutes - (21 * 60 + 20)) <= 3, "end=${amavasai.endMinutes}")
    }

    @Test
    fun computedPradoshamMatchesPublishedList() {
        val computed = CalendarRepository.days(2026).filter {
            val q = CalendarRepository.panchangam(it); q.sunsetTithi == 12 || q.sunsetTithi == 27
        }.toSet()
        // Published 2026 pradosham dates (hosting/v1/calendar/2026.json)
        val published = listOf(
            1 to listOf(1, 16, 30), 2 to listOf(14), 3 to listOf(1, 16, 30), 4 to listOf(15, 29),
            5 to listOf(14, 28), 6 to listOf(12, 27), 7 to listOf(12, 26), 8 to listOf(10, 25),
            9 to listOf(8, 24), 10 to listOf(8, 23), 11 to listOf(6, 22), 12 to listOf(6, 21),
        ).flatMap { (m, ds) -> ds.map { SimpleDate(2026, m, it) } }
        val hits = published.count { it in computed }
        assertTrue(hits >= published.size - 1, "matched $hits of ${published.size}")
    }

    @Test
    fun parsesYearFile() {
        val json = """{"year":2027,"revision":2,"futureField":true,
            "tamilMonthStarts":[{"date":"2027-04-14","month":0}],
            "events":[{"date":"2027-01-15","type":"FESTIVAL","ta":"தைப் பொங்கல்","en":"Thai Pongal"},
                      {"date":"2027-02-01","type":"MUHURTHAM"},
                      {"date":"2027-02-02","type":"SOMETHING_NEW","ta":"x","en":"x"}]}"""
        val dto = DataJson.decodeFromString(YearFileDto.serializer(), json)
        val ds = Dataset(mapOf(2027 to dto), emptyMap())
        assertEquals(2, ds.events.size) // unknown type skipped
        assertEquals(EventType.MUHURTHAM, ds.events[1].type)
        assertEquals("சுப முகூர்த்தம்", ds.events[1].title.ta) // default title
        assertEquals(1, ds.monthStarts.size)
    }

    @Test
    fun poruthamTables() {
        // Bride Ashwini (Mesham), groom Bharani (Mesham): same rasi, count 2
        val r = Porutham.match(groomRasi = 0, groomStar = 1, brideRasi = 0, brideStar = 0)
        assertEquals(10, r.size)
        assertEquals(Match.UTHAMAM, r.first { it.name.en.startsWith("Dina") }.match)
        // Ashwini (Paadha) + Magam (Paadha) -> rajju dosham
        val rajju = Porutham.match(4, 9, 0, 0).first { it.name.en.startsWith("Rajju") }
        assertEquals(Match.NONE, rajju.match)
        // Ashwini - Kettai are vedhai
        val vedhai = Porutham.match(7, 17, 0, 0).first { it.name.en.startsWith("Vedhai") }
        assertEquals(Match.NONE, vedhai.match)
        // Rasi only
        assertEquals(3, Porutham.match(0, null, 6, null).size)
        assertEquals(listOf(2, 3, 4), Porutham.padhams(2, 1)) // Karthigai in Rishabam
        assertEquals(listOf(0, 1, 2), Porutham.starsInRasi(0))
    }

    @Test
    fun palanChandrashtamamMatchesDailySheet() {
        val p = Panchangam.forDate(SimpleDate(2026, 10, 1))
        val t = RasiPalan.moonTransit(p)
        val rasi = ((t.rasiAtSunrise - 7) % 12 + 12) % 12
        assertTrue(RasiPalan.forRasi(p, rasi, t).first().isChandrashtamam)
        assertEquals("எச்சரிக்கை", RasiPalan.oneWord(RasiPalan.forRasi(p, rasi, t)).ta)
    }

    @Test
    fun tamilNewYear() {
        val t = Panchangam.tamilDate(SimpleDate(2026, 4, 14))
        assertEquals(1, t.day); assertEquals(0, t.monthIndex); assertEquals("Parabhava", t.year.en)
    }
}
