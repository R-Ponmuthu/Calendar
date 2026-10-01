package com.tamilcalendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamilcalendar.core.Lang
import com.tamilcalendar.core.Names
import com.tamilcalendar.core.SimpleDate
import com.tamilcalendar.data.CalendarRepository
import com.tamilcalendar.data.EventType
import com.tamilcalendar.todayIst

private val shortWeekdaysTa = listOf("ஞா", "தி", "செ", "பு", "வி", "வெ", "ச")
private val shortWeekdaysEn = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

@Composable
fun MonthlyScreen(year: Int, month: Int, lang: Lang, onMonthChange: (year: Int, month: Int) -> Unit, onDayClick: (SimpleDate) -> Unit) {
    val years = CalendarRepository.years
    val canPrev = year > years.first || month > 1
    val canNext = year < years.last || month < 12
    fun prev() = if (month == 1) onMonthChange(year - 1, 12) else onMonthChange(year, month - 1)
    fun next() = if (month == 12) onMonthChange(year + 1, 1) else onMonthChange(year, month + 1)
    val firstOfMonth = SimpleDate(year, month, 1)
    val days = SimpleDate.daysInMonth(year, month)
    val lead = firstOfMonth.dayOfWeek
    val today = todayIst()
    val ta = lang == Lang.TAMIL
    val monthDates = (1..days).map { SimpleDate(year, month, it) }
    val monthEvents = monthDates.flatMap { CalendarRepository.eventsOn(it) }
        .filter { it.type != EventType.VIRATHAM }

    // Tamil months spanning this English month
    val tamilMonths = monthDates.map { CalendarRepository.panchangam(it).tamilDate }
        .distinctBy { it.monthIndex }.joinToString(" – ") { "${it.month.get(lang)} ${it.year.get(lang)}" }

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { prev() }, enabled = canPrev) { Text("‹", fontSize = 28.sp) }
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${Names.englishMonths[month - 1].get(lang)} $year", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(tamilMonths, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                }
                TextButton(onClick = { next() }, enabled = canNext) { Text("›", fontSize = 28.sp) }
            }
        }
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 6.dp)) {
                (if (ta) shortWeekdaysTa else shortWeekdaysEn).forEachIndexed { i, w ->
                    Text(w, Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold,
                        color = if (i == 0) HolidayRed else MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        val cells = lead + days
        val rows = (cells + 6) / 7
        items(rows) { r ->
            Row(Modifier.fillMaxWidth().padding(horizontal = 6.dp)) {
                for (c in 0 until 7) {
                    val dayNum = r * 7 + c - lead + 1
                    Box(Modifier.weight(1f).aspectRatio(0.72f).padding(1.dp)) {
                        if (dayNum in 1..days) {
                            DayCell(SimpleDate(year, month, dayNum), today, lang, onDayClick)
                        }
                    }
                }
            }
        }
        item {
            Text(
                if (ta) "● முகூர்த்தம்   ○ பௌர்ணமி   ◐ அமாவாசை   ✕ கரி நாள்   சிவப்பு = விடுமுறை"
                else "● Muhurtham   ○ Pournami   ◐ Amavasai   ✕ Karinal   Red = holiday",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(12.dp),
            )
        }
        item {
            Text(if (ta) "இம்மாத முக்கிய நாட்கள்" else "Important days this month",
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), color = MaterialTheme.colorScheme.primary)
        }
        items(monthEvents) { ev ->
            Row(
                Modifier.fillMaxWidth().clickable { onDayClick(ev.date) }.padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("${ev.date.day.toString().padStart(2, '0')} ${Names.weekdays[ev.date.dayOfWeek].get(lang).take(3)}",
                    modifier = Modifier.padding(end = 4.dp), fontWeight = FontWeight.Bold)
                Text(ev.title.get(lang), color = eventColor(ev.type), modifier = Modifier.weight(1f))
                Text(ev.type.label.get(lang), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun DayCell(d: SimpleDate, today: SimpleDate, lang: Lang, onClick: (SimpleDate) -> Unit) {
    val p = CalendarRepository.panchangam(d)
    val ev = CalendarRepository.eventsOn(d)
    val holiday = d.dayOfWeek == 0 || ev.any { it.type == EventType.GOVT_HOLIDAY }
    val isToday = d == today
    val festival = ev.firstOrNull { it.type == EventType.FESTIVAL || it.type == EventType.GOVT_HOLIDAY }
    val marks = buildString {
        if (ev.any { it.type == EventType.MUHURTHAM }) append("●")
        if (ev.any { it.type == EventType.POURNAMI }) append("○")
        if (ev.any { it.type == EventType.AMAVASAI }) append("◐")
        if (ev.any { it.type == EventType.KARINAL }) append("✕")
    }
    Column(
        Modifier.fillMaxSize()
            .background(
                if (isToday) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(6.dp),
            )
            .then(if (isToday) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp)) else Modifier)
            .clickable { onClick(d) }
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            if (p.tamilDate.day == 1) p.tamilDate.month.get(lang).take(5) else "${p.tamilDate.day}",
            fontSize = 9.sp, maxLines = 1, color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text("${d.day}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (holiday) HolidayRed else MaterialTheme.colorScheme.onSurface)
        Text(marks, fontSize = 9.sp, color = MuhurthamGreen, maxLines = 1)
        if (festival != null) {
            Text(festival.title.get(lang), fontSize = 7.sp, maxLines = 2, lineHeight = 8.sp,
                overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center, color = eventColor(festival.type))
        }
    }
}
