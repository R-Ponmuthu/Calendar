package com.tamilcalendar.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamilcalendar.core.DayPanchangam
import com.tamilcalendar.core.Element
import com.tamilcalendar.core.Lang
import com.tamilcalendar.core.Names
import com.tamilcalendar.core.SimpleDate
import com.tamilcalendar.core.formatMinutes12
import com.tamilcalendar.data.CalendarRepository
import com.tamilcalendar.data.EventType

@Composable
fun SectionCard(title: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(Modifier.fillMaxWidth().padding(top = 6.dp)) {
        Text(label, modifier = Modifier.width(130.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = valueColor)
    }
}

/** "பஞ்சமி 02:55 PM வரை, பின்பு சஷ்டி" */
fun describeElements(list: List<Element>, day: SimpleDate, nextSunriseJd: Double, lang: Lang): String {
    val sb = StringBuilder()
    list.forEachIndexed { i, e ->
        if (i > 0) sb.append(if (lang == Lang.TAMIL) ", பின்பு " else ", then ")
        sb.append(e.label.get(lang))
        if (e.endJd < nextSunriseJd) {
            val t = formatMinutes12(e.endMinutes)
            val nextDay = e.endDate != day
            sb.append(
                if (lang == Lang.TAMIL) " ${if (nextDay) "நாளை " else ""}$t வரை"
                else " until $t${if (nextDay) " (next day)" else ""}",
            )
        }
    }
    return sb.toString()
}

fun formatDateLong(d: SimpleDate, lang: Lang): String =
    "${d.day} ${Names.englishMonths[d.month - 1].get(lang)} ${d.year}"

fun tamilDateText(p: DayPanchangam, lang: Lang): String =
    "${p.tamilDate.day} - ${p.tamilDate.month.get(lang)} - ${p.tamilDate.year.get(lang)}"

@Composable
fun eventColor(type: EventType): Color = when (type) {
    EventType.GOVT_HOLIDAY -> HolidayRed
    EventType.MUHURTHAM -> MuhurthamGreen
    EventType.KARINAL -> KarinalGrey
    EventType.FESTIVAL, EventType.CHRISTIAN, EventType.MUSLIM -> Saffron
    else -> MaterialTheme.colorScheme.primary
}

/** Year chips; hidden when only one year is available. */
@Composable
fun YearSelector(year: Int, onYearChange: (Int) -> Unit) {
    val years = CalendarRepository.years
    if (years.first == years.last) return
    Row(
        Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        years.forEach { y ->
            FilterChip(selected = y == year, onClick = { onYearChange(y) }, label = { Text("$y") })
        }
    }
}

/** Shown when a list is empty because that year's data file has not been published yet. */
@Composable
fun NoDataNotice(year: Int, lang: Lang) {
    Text(
        if (lang == Lang.TAMIL) "$year ஆண்டுக்கான பட்டியல் இன்னும் சேர்க்கப்படவில்லை. இணைய இணைப்புடன் ஆப்பைத் திறந்தால் புதிய தரவு தானாகப் பதிவிறங்கும்."
        else "The $year list has not been published yet. Open the app while online and new data downloads automatically.",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

fun stars(rating: Int): String = "★".repeat(rating) + "☆".repeat(5 - rating)
