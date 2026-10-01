package com.tamilcalendar.data

import com.tamilcalendar.core.Label
import com.tamilcalendar.core.SimpleDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

enum class EventType(val label: Label) {
    FESTIVAL(Label("பண்டிகை", "Festival")),
    CHRISTIAN(Label("கிறிஸ்தவ பண்டிகை", "Christian Festival")),
    MUSLIM(Label("முஸ்லிம் பண்டிகை", "Muslim Festival")),
    GOVT_HOLIDAY(Label("அரசு விடுமுறை", "TN Govt Holiday")),
    MUHURTHAM(Label("சுப முகூர்த்தம்", "Subha Muhurtham")),
    KARINAL(Label("கரி நாள்", "Karinal")),
    POURNAMI(Label("பௌர்ணமி", "Pournami")),
    AMAVASAI(Label("அமாவாசை", "Amavasai")),
    PRADOSHAM(Label("பிரதோஷம்", "Pradosham")),
    VIRATHAM(Label("விரதம்", "Viratham")),
}

data class CalendarEvent(val date: SimpleDate, val type: EventType, val title: Label)

// ---------------------------------------------------------------------------------
// JSON file formats (see hosting/README.md). Unknown keys are ignored so that newer
// data files never crash older app versions.
// ---------------------------------------------------------------------------------

@Serializable
data class EventDto(val date: String, val type: String, val ta: String = "", val en: String = "")

@Serializable
data class MonthStartDto(val date: String, val month: Int)

/** One calendar year: hosting/v1/calendar/<year>.json */
@Serializable
data class YearFileDto(
    val schemaVersion: Int = 1,
    val year: Int,
    val revision: Int = 1,
    val source: String? = null,
    val tamilMonthStarts: List<MonthStartDto> = emptyList(),
    val events: List<EventDto> = emptyList(),
)

@Serializable
data class PalanEntryDto(
    val date: String,
    val rasi: Int,
    val ta: String,
    val en: String,
    /** Optional one-word summary, e.g. "லாபம்" / "Profit". */
    val wordTa: String = "",
    val wordEn: String = "",
)

/** Hand-written palan for one date + rasi. */
data class PalanText(val word: Label?, val text: Label)

/** Optional hand-written rasi palan: hosting/v1/rasipalan/<year>.json */
@Serializable
data class PalanFileDto(val year: Int, val revision: Int = 1, val entries: List<PalanEntryDto> = emptyList())

@Serializable
data class ManifestEntry(val revision: Int, val path: String)

/** hosting/v1/manifest.json – lists every year file and its revision. */
@Serializable
data class ManifestDto(
    val schemaVersion: Int = 1,
    val calendar: Map<String, ManifestEntry> = emptyMap(),
    val rasiPalan: Map<String, ManifestEntry> = emptyMap(),
)

internal val DataJson = Json { ignoreUnknownKeys = true; isLenient = true }

/** Immutable, indexed snapshot of all loaded data files. */
class Dataset(
    val calendars: Map<Int, YearFileDto>,
    val palans: Map<Int, PalanFileDto>,
) {
    val dataYears: Set<Int> get() = calendars.keys

    val events: List<CalendarEvent> by lazy {
        calendars.values.flatMap { file ->
            file.events.mapNotNull { e ->
                val type = runCatching { EventType.valueOf(e.type) }.getOrNull() ?: return@mapNotNull null
                val date = SimpleDate.parseIso(e.date) ?: return@mapNotNull null
                CalendarEvent(
                    date, type,
                    Label(e.ta.ifBlank { type.label.ta }, e.en.ifBlank { type.label.en }),
                )
            }
        }.sortedBy { it.date.epochDay }
    }

    val byDay: Map<Long, List<CalendarEvent>> by lazy { events.groupBy { it.date.epochDay } }

    /** (epochDay of Tamil month day 1, month index) pinned by the data files. */
    val monthStarts: List<Pair<Long, Int>> by lazy {
        calendars.values.flatMap { f ->
            f.tamilMonthStarts.mapNotNull { m -> SimpleDate.parseIso(m.date)?.let { it.epochDay to m.month } }
        }.distinctBy { it.first }.sortedBy { it.first }
    }

    private val palanIndex: Map<Pair<Long, Int>, PalanText> by lazy {
        palans.values.flatMap { it.entries }.mapNotNull { e ->
            val word = if (e.wordTa.isNotBlank() || e.wordEn.isNotBlank())
                Label(e.wordTa.ifBlank { e.wordEn }, e.wordEn.ifBlank { e.wordTa }) else null
            SimpleDate.parseIso(e.date)?.let { (it.epochDay to e.rasi) to PalanText(word, Label(e.ta, e.en)) }
        }.toMap()
    }

    fun palanFor(date: SimpleDate, rasi: Int): PalanText? = palanIndex[date.epochDay to rasi]

    companion object {
        val EMPTY = Dataset(emptyMap(), emptyMap())
    }
}
