package com.tamilcalendar.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tamilcalendar.core.Label
import com.tamilcalendar.core.Lang
import com.tamilcalendar.core.Names
import com.tamilcalendar.core.SimpleDate
import com.tamilcalendar.data.CalendarEvent
import com.tamilcalendar.data.CalendarRepository
import com.tamilcalendar.data.EventType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventListScreen(
    events: List<CalendarEvent>,
    year: Int,
    lang: Lang,
    onDayClick: (SimpleDate) -> Unit,
    onYearChange: (Int) -> Unit,
    showPanchangDetail: Boolean = false,
    showTithiTimes: Boolean = false,
    showTitle: Boolean = false,
    note: Label? = null,
    computed: Boolean = false,
) {
    val grouped = events.sortedBy { it.date.epochDay }.groupBy { it.date.month }
    LazyColumn(Modifier.fillMaxSize()) {
        item { YearSelector(year, onYearChange) }
        if (events.isEmpty() && !computed && !CalendarRepository.hasData(year)) {
            item { NoDataNotice(year, lang) }
        }
        if (note != null) {
            item {
                Text(note.get(lang), style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(12.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer).padding(10.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
        item {
            Text(
                (if (lang == Lang.TAMIL) "மொத்தம் " else "Total ") + events.size + (if (lang == Lang.TAMIL) " நாட்கள் – $year" else " days in $year"),
                style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            )
        }
        grouped.forEach { (month, list) ->
            stickyHeader {
                Text(
                    "${Names.englishMonths[month - 1].get(lang)} $year",
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary).padding(horizontal = 12.dp, vertical = 6.dp),
                    color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold,
                )
            }
            items(list) { ev -> EventRow(ev, lang, onDayClick, showPanchangDetail, showTithiTimes, showTitle) }
        }
    }
}

@Composable
private fun EventRow(
    ev: CalendarEvent, lang: Lang, onDayClick: (SimpleDate) -> Unit,
    showPanchangDetail: Boolean, showTithiTimes: Boolean, showTitle: Boolean,
) {
    val p = CalendarRepository.panchangam(ev.date)
    val ta = lang == Lang.TAMIL
    Row(
        Modifier.fillMaxWidth().clickable { onDayClick(ev.date) }.padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.width(64.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${ev.date.day}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold,
                color = if (ev.date.dayOfWeek == 0) HolidayRed else MaterialTheme.colorScheme.onSurface)
            Text(p.weekday.get(lang), style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center, maxLines = 1)
        }
        Column(Modifier.weight(1f).padding(start = 8.dp)) {
            if (showTitle || ev.type in setOf(EventType.FESTIVAL, EventType.CHRISTIAN, EventType.MUSLIM)) {
                Text(ev.title.get(lang), fontWeight = FontWeight.SemiBold, color = eventColor(ev.type))
            }
            Text(tamilDateText(p, lang), style = MaterialTheme.typography.bodyMedium)
            if (showPanchangDetail) {
                Text(
                    "${p.paksha.get(lang)} • ${Names.nakshatras[p.dayStarIndex].get(lang)} • ${Names.tithis[p.sunriseTithi].get(lang)}",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    (if (ta) "நல்ல நேரம்: " else "Nalla neram: ") + p.nallaNeram.joinToString(", ") { it.text() },
                    style = MaterialTheme.typography.bodySmall, color = MuhurthamGreen,
                )
            }
            if (showTithiTimes) {
                Text(describeElements(p.tithis, ev.date, p.nextSunriseJd, lang),
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
    HorizontalDivider(Modifier.padding(horizontal = 12.dp))
}

@Composable
fun FestivalScreen(year: Int, lang: Lang, onDayClick: (SimpleDate) -> Unit, onYearChange: (Int) -> Unit) {
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        Label("இந்து", "Hindu") to EventType.FESTIVAL,
        Label("கிறிஸ்தவ", "Christian") to EventType.CHRISTIAN,
        Label("முஸ்லிம்", "Muslim") to EventType.MUSLIM,
    )
    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tab) {
            tabs.forEachIndexed { i, t ->
                Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t.first.get(lang)) })
            }
        }
        val type = tabs[tab].second
        EventListScreen(CalendarRepository.events(year, type), year, lang, onDayClick, onYearChange, showTitle = true)
    }
}

@Composable
fun ChandrashtamamScreen(year: Int, lang: Lang, onDayClick: (SimpleDate) -> Unit, onYearChange: (Int) -> Unit) {
    var rasi by remember { mutableIntStateOf(0) }
    val ta = lang == Lang.TAMIL
    val days = remember(rasi, year) { CalendarRepository.chandrashtamamDays(rasi, year) }
    // group consecutive days into periods
    val periods = remember(days) {
        val out = ArrayList<List<SimpleDate>>()
        var cur = ArrayList<SimpleDate>()
        days.forEach { d ->
            if (cur.isNotEmpty() && d.epochDay != cur.last().epochDay + 1) { out += cur; cur = ArrayList() }
            cur += d
        }
        if (cur.isNotEmpty()) out += cur
        out
    }
    Column(Modifier.fillMaxSize()) {
        YearSelector(year, onYearChange)
        Row(Modifier.horizontalScroll(rememberScrollState()).padding(8.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Names.rasis.forEachIndexed { i, r ->
                FilterChip(selected = rasi == i, onClick = { rasi = i }, label = { Text(r.get(lang)) })
            }
        }
        Text(
            if (ta) "${Names.rasis[rasi].ta} ராசிக்கு சந்திராஷ்டம நாட்கள் $year (சந்திரன் ${Names.rasis[(rasi + 7) % 12].ta} ராசியில்)"
            else "Chandrashtamam days for ${Names.rasis[rasi].en} in $year (Moon in ${Names.rasis[(rasi + 7) % 12].en})",
            style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 12.dp),
        )
        Column(Modifier.verticalScroll(rememberScrollState())) {
            periods.forEach { period ->
                val a = period.first(); val b = period.last()
                Row(
                    Modifier.fillMaxWidth().clickable { onDayClick(a) }.padding(horizontal = 12.dp, vertical = 10.dp),
                ) {
                    Text(
                        "${formatDateLong(a, lang)} (${Names.weekdays[a.dayOfWeek].get(lang)})" +
                            if (b != a) "  →  ${formatDateLong(b, lang)} (${Names.weekdays[b.dayOfWeek].get(lang)})" else "",
                    )
                }
                HorizontalDivider(Modifier.padding(horizontal = 12.dp))
            }
        }
    }
}

@Composable
fun AboutScreen(lang: Lang) {
    val ta = lang == Lang.TAMIL
    val years = CalendarRepository.dataset.dataYears.sorted().joinToString(", ")
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(if (ta) "தமிழ் காலண்டர்" else "Tamil Calendar", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            if (ta) "தினசரி பஞ்சாங்கம், ராசி பலன், மாத காலண்டர், முகூர்த்த / திருமண நாட்கள், திருமணப் பொருத்தம், பண்டிகைகள், பௌர்ணமி, அமாவாசை, பிரதோஷம், கரி நாள், தமிழக அரசு விடுமுறைகள் மற்றும் சந்திராஷ்டமம்."
            else "Daily panchangam, rasi palan, monthly calendar, muhurtham / wedding dates, marriage porutham, festivals, Pournami, Amavasai, Pradosham, Karinal, Tamil Nadu government holidays and Chandrashtamam.",
        )
        Text(
            if (ta) "திதி, நட்சத்திரம், யோகம், கரணம், லக்னம், சூரிய உதயம் ஆகியவை சென்னைக்கு திருக்கணித (வானியல்) முறைப்படி கணக்கிடப்படுகின்றன. வாக்கிய பஞ்சாங்கத்துடன் நேரங்கள் சற்று மாறுபடலாம்."
            else "Tithi, nakshatram, yogam, karanam, lagnam and sunrise are computed astronomically (Thirukanitha / drik) for Chennai, IST. Vakya panchangams may differ by a few hours.",
        )
        Text(
            if (ta) "இராகு காலம், எமகண்டம், குளிகை, நல்ல நேரம், கௌரி நல்ல நேரம், சூலம் ஆகியவை பாரம்பரிய வார அட்டவணைப்படி. ராசி பலன் சந்திர கோசார அடிப்படையிலான பொதுப் பலன்."
            else "Rahu Kalam, Yemagandam, Kuligai, Nalla Neram, Gowri Nalla Neram and Soolam follow the traditional weekday tables. Rasi palan is a general reading based on the Moon's transit.",
        )
        Text(
            if (ta) "பண்டிகை, விடுமுறை, முகூர்த்தம், கரி நாள், பௌர்ணமி, அமாவாசை, பிரதோஷ நாட்கள் ஆண்டுதோறும் இணையம் வழியாகப் புதுப்பிக்கப்படும். தற்போதுள்ள ஆண்டுகள்: $years"
            else "Festival, holiday, muhurtham, karinal, pournami, amavasai and pradosham lists are updated online every year. Years available: $years",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
