package com.tamilcalendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamilcalendar.core.Lang
import com.tamilcalendar.core.Match
import com.tamilcalendar.core.Names
import com.tamilcalendar.core.Porutham

@Composable
fun PoruthamScreen(lang: Lang) {
    val ta = lang == Lang.TAMIL
    var groomRasi by rememberSaveable { mutableStateOf<Int?>(null) }
    var groomStar by rememberSaveable { mutableStateOf<Int?>(null) }
    var brideRasi by rememberSaveable { mutableStateOf<Int?>(null) }
    var brideStar by rememberSaveable { mutableStateOf<Int?>(null) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Text(
            if (ta) "மணமகன், மணமகள் ராசியைத் தேர்ந்தெடுக்கவும். முழு 10 பொருத்தங்களுக்கு நட்சத்திரத்தையும் தேர்வு செய்யவும்."
            else "Choose the groom's and bride's rasi. Pick the stars too for all 10 poruthams.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp)).padding(10.dp),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        PersonPicker(
            if (ta) "மணமகன் (ஆண்)" else "Groom", lang, groomRasi, groomStar,
            onRasi = { groomRasi = it; groomStar = null }, onStar = { groomStar = it },
        )
        PersonPicker(
            if (ta) "மணமகள் (பெண்)" else "Bride", lang, brideRasi, brideStar,
            onRasi = { brideRasi = it; brideStar = null }, onStar = { brideStar = it },
        )

        val gr = groomRasi
        val br = brideRasi
        if (gr != null && br != null) {
            val results = Porutham.match(gr, groomStar, br, brideStar)
            val score = Porutham.totalScore(results)
            val essentialFail = results.any { it.essential && it.match == Match.NONE }
            val ratio = score / results.size
            val (verdict, color) = when {
                essentialFail -> (if (ta) "ரஜ்ஜு / வேதை பொருத்தம் இல்லை – பொதுவாகப் பரிந்துரைக்கப்படுவதில்லை" else "Rajju / Vedhai does not match – usually not recommended") to HolidayRed
                ratio >= 0.7 -> (if (ta) "நல்ல பொருத்தம்" else "Good match") to MuhurthamGreen
                ratio >= 0.5 -> (if (ta) "சராசரி பொருத்தம்" else "Average match") to Saffron
                else -> (if (ta) "குறைந்த பொருத்தம்" else "Weak match") to HolidayRed
            }
            val scoreText = if (score % 1.0 == 0.0) "${score.toInt()}" else "$score"
            SectionCard(if (ta) "முடிவு" else "Result") {
                Text(
                    (if (ta) "பொருத்தம்: " else "Score: ") + "$scoreText / ${results.size}",
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp),
                )
                Text(verdict, color = color, fontWeight = FontWeight.SemiBold)
                if (groomStar == null || brideStar == null) {
                    Text(
                        if (ta) "நட்சத்திரம் தேர்வு செய்யப்படாததால் ராசி அடிப்படையிலான 3 பொருத்தங்கள் மட்டும் காட்டப்படுகின்றன."
                        else "Stars not chosen – showing only the 3 rasi-based poruthams.",
                        style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
            SectionCard(if (ta) "பொருத்த விவரம்" else "Porutham details") {
                results.forEach { r ->
                    Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.Top) {
                        Text(
                            when (r.match) { Match.UTHAMAM -> "✔"; Match.MATHIMAM -> "◐"; Match.NONE -> "✘" },
                            color = matchColor(r.match), fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp),
                        )
                        Column(Modifier.weight(1f)) {
                            Text(r.name.get(lang) + if (r.essential) " *" else "", fontWeight = FontWeight.SemiBold)
                            Text(r.detail.get(lang), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(r.match.label.get(lang), color = matchColor(r.match), style = MaterialTheme.typography.labelMedium)
                    }
                    HorizontalDivider(Modifier.padding(top = 8.dp))
                }
                Text(
                    if (ta) "* ரஜ்ஜு, வேதை முக்கியப் பொருத்தங்கள். செவ்வாய் தோஷம், ராகு-கேது தோஷம், தசா சந்திப்பு போன்றவற்றுக்கு முழு ஜாதகத்தை ஜோதிடரிடம் காட்டி உறுதி செய்யவும்."
                    else "* Rajju and Vedhai are essential. Check full horoscopes with an astrologer for Sevvai/Rahu-Kethu dosham and dasa sandhi.",
                    style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun matchColor(m: Match): Color = when (m) {
    Match.UTHAMAM -> MuhurthamGreen
    Match.MATHIMAM -> Saffron
    Match.NONE -> HolidayRed
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PersonPicker(
    title: String, lang: Lang, rasi: Int?, star: Int?,
    onRasi: (Int) -> Unit, onStar: (Int?) -> Unit,
) {
    val ta = lang == Lang.TAMIL
    SectionCard(title) {
        Text(if (ta) "ராசி" else "Rasi", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 6.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Names.rasis.forEachIndexed { i, r ->
                FilterChip(selected = rasi == i, onClick = { onRasi(i) }, label = { Text(r.get(lang)) })
            }
        }
        if (rasi != null) {
            Text(
                if (ta) "நட்சத்திரம் (பாதம்)" else "Star (padham)",
                style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 6.dp),
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Porutham.starsInRasi(rasi).forEach { s ->
                    val padhams = Porutham.padhams(s, rasi)
                    val suffix = if (padhams.size < 4) " (${padhams.joinToString(",")})" else ""
                    FilterChip(
                        selected = star == s,
                        onClick = { onStar(if (star == s) null else s) },
                        label = { Text(Names.nakshatras[s].get(lang) + suffix) },
                    )
                }
            }
        }
    }
}
