package com.tamilcalendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamilcalendar.core.Lang
import com.tamilcalendar.core.Names
import com.tamilcalendar.core.RasiPalan
import com.tamilcalendar.core.SimpleDate
import com.tamilcalendar.core.formatMinutes12
import com.tamilcalendar.data.CalendarRepository

@Composable
fun RasiPalanScreen(date: SimpleDate, lang: Lang, onDateChange: (SimpleDate) -> Unit) {
    val ta = lang == Lang.TAMIL
    val p = CalendarRepository.panchangam(date)
    val transit = remember(date) { RasiPalan.moonTransit(p) }
    var selected by remember { mutableStateOf<Int?>(null) }
    val first = CalendarRepository.firstDay
    val last = CalendarRepository.lastDay

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Date navigation
        Row(
            Modifier.fillMaxWidth().padding(12.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = { onDateChange(date.plusDays(-1)) }, enabled = date > first) { Text("‹", fontSize = 28.sp) }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(formatDateLong(date, lang), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("${p.weekday.get(lang)} • ${tamilDateText(p, lang)}", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
            }
            TextButton(onClick = { onDateChange(date.plusDays(1)) }, enabled = date < last) { Text("›", fontSize = 28.sp) }
        }

        // Moon position
        val moonNow = Names.rasis[transit.rasiAtSunrise]
        val moonText = if (transit.nextRasi != null && transit.changeJd != null) {
            val (d, min) = SimpleDate.fromJdIst(transit.changeJd)
            val t = formatMinutes12(min)
            val next = Names.rasis[transit.nextRasi]
            if (ta) "சந்திரன் ${moonNow.ta} ராசியில் ${if (d != date) "நாளை " else ""}$t வரை, பின்பு ${next.ta} ராசியில்."
            else "Moon in ${moonNow.en} until $t${if (d != date) " (next day)" else ""}, then in ${next.en}."
        } else {
            if (ta) "சந்திரன் இன்று முழுவதும் ${moonNow.ta} ராசியில்." else "Moon in ${moonNow.en} all day."
        }
        Text(moonText, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp))

        // Rasi filter
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            FilterChip(selected = selected == null, onClick = { selected = null }, label = { Text(if (ta) "அனைத்தும்" else "All") })
            Names.rasis.forEachIndexed { i, r ->
                FilterChip(selected = selected == i, onClick = { selected = i }, label = { Text(r.get(lang)) })
            }
        }

        val rasis = selected?.let { listOf(it) } ?: (0 until 12).toList()
        rasis.forEach { rasi -> PalanCard(date, rasi, transit, lang) }

        Text(
            if (ta) "சந்திர கோசார அடிப்படையிலான பொதுப் பலன். தனிப்பட்ட ஜாதகப்படி பலன்கள் மாறுபடலாம்."
            else "General reading based on the Moon's transit (Chandra gochara). Individual horoscopes may differ.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun PalanCard(date: SimpleDate, rasi: Int, transit: RasiPalan.MoonTransit, lang: Lang) {
    val ta = lang == Lang.TAMIL
    val p = CalendarRepository.panchangam(date)
    val segments = RasiPalan.forRasi(p, rasi, transit)
    val override = CalendarRepository.palanOverride(date, rasi)
    val rasiName = Names.rasis[rasi].get(lang)

    val word = CalendarRepository.palanWord(date, rasi, transit).get(lang)

    SectionCard("$rasiName – $word   ${stars(segments.first().rating)}") {
        if (override != null) {
            Text(override.text.get(lang), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 6.dp))
            return@SectionCard
        }
        segments.forEach { seg ->
            val heading = buildString {
                if (seg.fromMinutes != null) {
                    val t = formatMinutes12(seg.fromMinutes)
                    append(if (ta) "${if (seg.fromNextDay) "நாளை " else ""}$t முதல் • " else "From $t${if (seg.fromNextDay) " (next day)" else ""} • ")
                }
                if (segments.size > 1) append("${seg.word.get(lang)} • ")
                append(RasiPalan.houseNames[seg.house - 1].get(lang))
                if (seg.fromMinutes != null) append("  ${stars(seg.rating)}")
            }
            Text(
                heading,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (seg.isChandrashtamam) HolidayRed else MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(seg.text.get(lang), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
