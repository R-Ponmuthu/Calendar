package com.tamilcalendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.tamilcalendar.todayIst

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyScreen(date: SimpleDate, lang: Lang, onDateChange: (SimpleDate) -> Unit, onOpenPalan: () -> Unit) {
    val p = CalendarRepository.panchangam(date)
    val events = CalendarRepository.eventsOn(date)
    val ta = lang == Lang.TAMIL
    var showPicker by remember { mutableStateOf(false) }
    val first = CalendarRepository.firstDay
    val last = CalendarRepository.lastDay
    fun go(d: SimpleDate) { if (d >= first && d <= last) onDateChange(d) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // ---------- Header (date sheet) ----------
        Column(
            Modifier.fillMaxWidth().padding(12.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { go(date.plusDays(-1)) }, enabled = date > first) { Text("‹", fontSize = 30.sp) }
                Column(
                    Modifier.weight(1f).clickable { showPicker = true },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(formatDateLong(date, lang), style = MaterialTheme.typography.titleMedium)
                    Text("${date.day}", fontSize = 64.sp, fontWeight = FontWeight.Bold,
                        color = if (date.dayOfWeek == 0 || CalendarRepository.isHoliday(date)) HolidayRed else MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(p.weekday.get(lang), style = MaterialTheme.typography.titleMedium)
                }
                TextButton(onClick = { go(date.plusDays(1)) }, enabled = date < last) { Text("›", fontSize = 30.sp) }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                (if (ta) "தேதி: " else "Tamil date: ") + tamilDateText(p, lang) + " " + p.weekday.get(lang),
                style = MaterialTheme.typography.titleSmall, textAlign = TextAlign.Center,
            )
            Text(p.paksha.get(lang) + " • " + p.naal.get(lang), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
            events.forEach {
                Text(it.title.get(lang), color = eventColor(it.type), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        }

        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val today = todayIst()
            AssistChip(onClick = { go(today.plusDays(-1)) }, label = { Text(if (ta) "நேற்று" else "Yesterday") })
            AssistChip(onClick = { go(today) }, label = { Text(if (ta) "இன்று" else "Today") })
            AssistChip(onClick = { go(today.plusDays(1)) }, label = { Text(if (ta) "நாளை" else "Tomorrow") })
            AssistChip(onClick = { showPicker = true }, label = { Text("📅") })
        }

        SectionCard(if (ta) "ராசி பலன்" else "Rasi Palan") {
            val transit = remember(date) { RasiPalan.moonTransit(p) }
            (0 until 12).chunked(2).forEach { pair ->
                Row(Modifier.fillMaxWidth().padding(top = 6.dp)) {
                    pair.forEach { r ->
                        val word = CalendarRepository.palanWord(date, r, transit)
                        Row(Modifier.weight(1f).clickable { onOpenPalan() }) {
                            Text(Names.rasis[r].get(lang) + " – ", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                word.get(lang), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold,
                                color = if (RasiPalan.house(transit.rasiAtSunrise, r) == 8) HolidayRed else MuhurthamGreen,
                            )
                        }
                    }
                }
            }
            TextButton(onClick = onOpenPalan) {
                Text(if (ta) "விரிவான பலன் பார்க்க →" else "See detailed palan →")
            }
        }
        SectionCard(if (ta) "நல்ல நேரம்" else "Nalla Neram (Good time)") {
            InfoRow(if (ta) "காலை" else "Morning", p.nallaNeram[0].text(), MuhurthamGreen)
            InfoRow(if (ta) "மாலை" else "Evening", p.nallaNeram[1].text(), MuhurthamGreen)
        }
        SectionCard(if (ta) "கௌரி நல்ல நேரம்" else "Gowri Nalla Neram") {
            InfoRow(if (ta) "காலை" else "Morning", p.gowriNallaNeram[0].text(), MuhurthamGreen)
            InfoRow(if (ta) "மாலை" else "Evening", p.gowriNallaNeram[1].text(), MuhurthamGreen)
        }
        SectionCard(if (ta) "தவிர்க்க வேண்டிய நேரம்" else "Inauspicious time") {
            InfoRow(if (ta) "இராகு காலம்" else "Rahu Kalam", p.rahuKalam.text(), HolidayRed)
            InfoRow(if (ta) "எமகண்டம்" else "Yemagandam", p.yemagandam.text(), HolidayRed)
            InfoRow(if (ta) "குளிகை" else "Kuligai", p.kuligai.text(), HolidayRed)
        }
        SectionCard(if (ta) "பஞ்சாங்கம்" else "Panchangam") {
            InfoRow(if (ta) "திதி" else "Tithi", describeElements(p.tithis, date, p.nextSunriseJd, lang))
            InfoRow(if (ta) "நட்சத்திரம்" else "Nakshatram", describeElements(p.nakshatras, date, p.nextSunriseJd, lang))
            InfoRow(if (ta) "யோகம்" else "Yogam", describeElements(p.yogas, date, p.nextSunriseJd, lang))
            InfoRow(if (ta) "கரணம்" else "Karanam", describeElements(p.karanas, date, p.nextSunriseJd, lang))
            InfoRow(if (ta) "பட்சம்" else "Paksham", p.paksha.get(lang))
            InfoRow(if (ta) "நாள்" else "Naal", p.naal.get(lang))
        }
        SectionCard(if (ta) "சூலம் / பரிகாரம்" else "Soolam / Parigaram") {
            InfoRow(if (ta) "சூலம்" else "Soolam", p.soolam.get(lang))
            InfoRow(if (ta) "பரிகாரம்" else "Parigaram", p.parigaram.get(lang))
        }
        SectionCard(if (ta) "சந்திராஷ்டமம்" else "Chandrashtamam") {
            InfoRow(if (ta) "நட்சத்திரம்" else "Stars", p.chandrashtamamStars.joinToString(", ") { it.get(lang) })
            InfoRow(if (ta) "ராசி" else "Rasi", p.chandrashtamamRasi.get(lang))
        }
        SectionCard(if (ta) "சூரியன் / லக்னம்" else "Sun / Lagnam") {
            InfoRow(if (ta) "சூரிய உதயம்" else "Sunrise", formatMinutes12(p.sunriseMin))
            InfoRow(if (ta) "சூரிய அஸ்தமனம்" else "Sunset", formatMinutes12(p.sunsetMin))
            InfoRow(
                if (ta) "உதய லக்னம்" else "Udhaya Lagnam",
                if (ta) "${p.lagnam.rasi.ta} லக்னம் இருப்பு ${p.lagnam.naazhigai} நாழிகை ${p.lagnam.vinadi} விநாடி"
                else "${p.lagnam.rasi.en} – ${p.lagnam.naazhigai} naazhigai ${p.lagnam.vinadi} vinadi remaining",
            )
        }
        Text(
            if (ta) "நேரங்கள் சென்னை (IST) – திருக்கணித முறைப்படி கணக்கிடப்பட்டவை."
            else "Times for Chennai (IST), computed using the Thirukanitha method.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    if (showPicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = date.epochDay * 86_400_000L,
            yearRange = CalendarRepository.years,
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { go(SimpleDate.fromEpochDay(it / 86_400_000L)) }
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showPicker = false }) { Text(if (ta) "ரத்து" else "Cancel") } },
        ) { DatePicker(state = state) }
    }
}
