package com.tamilcalendar.core

import kotlin.math.floor

/**
 * Daily rasi palan from the Moon's transit (Chandra gochara): the result depends on the
 * house (1..12) the Moon occupies counted from the native's rasi. When the Moon changes
 * sign before the next sunrise, the palan is given for both parts of the day.
 */
object RasiPalan {

    data class Segment(
        val house: Int,
        val rating: Int,
        /** One-word summary, e.g. லாபம் / Profit. */
        val word: Label,
        val text: Label,
        /** Minutes after midnight (IST) when this segment starts; null = from sunrise. */
        val fromMinutes: Int?,
        val fromNextDay: Boolean = false,
    ) {
        val isChandrashtamam: Boolean get() = house == 8
    }

    data class MoonTransit(val rasiAtSunrise: Int, val nextRasi: Int?, val changeJd: Double?)

    fun moonTransit(p: DayPanchangam): MoonTransit {
        val r = floor(Astro.siderealMoon(p.sunriseJd) / 30.0).toInt() % 12
        val next = (r + 1) % 12
        val change = Astro.findAngle(p.sunriseJd, next * 30.0, 13.18) { Astro.siderealMoon(it) }
        return if (change < p.nextSunriseJd) MoonTransit(r, next, change) else MoonTransit(r, null, null)
    }

    fun house(moonRasi: Int, rasi: Int) = ((moonRasi - rasi + 12) % 12) + 1

    fun forRasi(p: DayPanchangam, rasi: Int, t: MoonTransit = moonTransit(p)): List<Segment> {
        val h1 = house(t.rasiAtSunrise, rasi)
        val first = Segment(h1, ratings[h1 - 1], keywords[h1 - 1], texts[h1 - 1], null)
        if (t.nextRasi == null || t.changeJd == null) return listOf(first)
        val (d, min) = SimpleDate.fromJdIst(t.changeJd)
        val h2 = house(t.nextRasi, rasi)
        return listOf(first, Segment(h2, ratings[h2 - 1], keywords[h2 - 1], texts[h2 - 1], min, d != p.date))
    }

    /** One-word palan for the day; "A → B" when the Moon changes sign during the day. */
    fun oneWord(segments: List<Segment>): Label =
        if (segments.size == 1) segments[0].word
        else Label(segments.joinToString(" → ") { it.word.ta }, segments.joinToString(" → ") { it.word.en })

    /** One-word result for each house 1..12. */
    val keywords = listOf(
        Label("உற்சாகம்", "Enthusiasm"), Label("சிக்கனம்", "Thrift"), Label("தைரியம்", "Courage"),
        Label("கவலை", "Worry"), Label("தடை", "Delay"), Label("வெற்றி", "Victory"),
        Label("மகிழ்ச்சி", "Happiness"), Label("எச்சரிக்கை", "Caution"), Label("சோர்வு", "Fatigue"),
        Label("முன்னேற்றம்", "Progress"), Label("லாபம்", "Profit"), Label("விரயம்", "Spending"),
    )

    val houseNames = listOf(
        Label("ஜென்ம சந்திரன்", "Janma (1st)"), Label("2-ல் சந்திரன்", "2nd house"), Label("3-ல் சந்திரன்", "3rd house"),
        Label("4-ல் சந்திரன்", "4th house"), Label("5-ல் சந்திரன்", "5th house"), Label("6-ல் சந்திரன்", "6th house"),
        Label("7-ல் சந்திரன்", "7th house"), Label("சந்திராஷ்டமம்", "Chandrashtamam (8th)"), Label("9-ல் சந்திரன்", "9th house"),
        Label("10-ல் சந்திரன்", "10th house"), Label("11-ல் சந்திரன்", "11th house"), Label("12-ல் சந்திரன்", "12th house"),
    )

    /** 1..5 stars per house. */
    private val ratings = listOf(3, 2, 5, 2, 2, 4, 4, 1, 2, 5, 5, 2)

    private val texts = listOf(
        Label(
            "சந்திரன் உங்கள் ராசியில் சஞ்சரிக்கிறார். மனதில் உற்சாகமும் புதிய எண்ணங்களும் தோன்றும். உணவு, உடை வசதிகள் கிடைக்கும்; ஆனாலும் அவசர முடிவுகளைத் தவிர்த்து நிதானமாகச் செயல்படுங்கள்.",
            "The Moon is in your own sign. You feel enthusiastic and full of new ideas, and comforts come easily. Avoid hasty decisions and act calmly.",
        ),
        Label(
            "பண விஷயங்களில் கவனம் தேவை. எதிர்பாராத செலவுகள் வரலாம்; பேச்சில் நிதானம் அவசியம். குடும்பத்தினருடன் வீண் வாக்குவாதங்களைத் தவிர்க்கவும்.",
            "Be careful with money – unexpected expenses may come up. Weigh your words and avoid needless arguments at home.",
        ),
        Label(
            "தைரியமும் தன்னம்பிக்கையும் கூடும் நாள். முயற்சிகளில் வெற்றி கிடைக்கும்; உடன்பிறந்தோர் மற்றும் நண்பர்களின் ஆதரவு உண்டு. சிறு பயணங்கள் நன்மை தரும்.",
            "Courage and confidence rise. Efforts succeed, and siblings and friends are supportive. Short trips are rewarding.",
        ),
        Label(
            "மனதில் ஏதோ ஒரு கவலை அல்லது பதற்றம் இருக்கும். வீடு, வாகனம் தொடர்பான விஷயங்களில் கவனம் தேவை. தாயாரின் நலனில் அக்கறை காட்டுங்கள்.",
            "Some worry or restlessness is likely. Take care with matters of home and vehicles, and look after your mother's well-being.",
        ),
        Label(
            "திட்டமிட்ட காரியங்களில் சிறு தடங்கல்கள் ஏற்படலாம். குழந்தைகள் விஷயத்தில் கவனம் தேவை. ஊக வணிகம், பங்கு முதலீடுகளைத் தள்ளிப் போடுங்கள்.",
            "Planned work may meet small obstacles. Pay attention to children's matters and postpone speculative investments.",
        ),
        Label(
            "எதிர்ப்புகளை வெல்லும் நாள். போட்டிகளில் வெற்றியும், நிலுவையில் இருந்த காரியங்களில் முன்னேற்றமும் உண்டு. உடல் நலம் சீராகும்; கடன் பிரச்சினைகள் குறையும்.",
            "A day to overcome opposition. Success in competition and progress on pending work; health improves and debt worries ease.",
        ),
        Label(
            "மகிழ்ச்சியான நாள். வாழ்க்கைத் துணையுடன் இணக்கம் கூடும்; கூட்டுத் தொழில், ஒப்பந்தங்கள் சாதகமாக அமையும். மதிப்பும் மரியாதையும் உயரும்.",
            "A pleasant day. Harmony with your spouse grows, partnerships and agreements go well, and your standing rises.",
        ),
        Label(
            "சந்திராஷ்டம நாள். புதிய முயற்சிகள், முக்கிய முடிவுகள், நீண்ட பயணங்களைத் தவிர்க்கவும். பேச்சிலும் வாகனம் ஓட்டுவதிலும் எச்சரிக்கை தேவை. இறை வழிபாடு மன அமைதி தரும்.",
            "Chandrashtamam. Avoid new ventures, major decisions and long journeys. Be careful in speech and while driving; prayer brings peace of mind.",
        ),
        Label(
            "சோர்வும் சிறு தாமதங்களும் ஏற்படலாம். பெரியோர்களின் ஆலோசனைப்படி நடப்பது நல்லது. ஆன்மிக நாட்டம் கூடும்; தேவையற்ற அலைச்சலைத் தவிர்க்கவும்.",
            "Fatigue and minor delays are possible. Follow the advice of elders; interest in spiritual matters grows. Avoid unnecessary running around.",
        ),
        Label(
            "தொழில், உத்தியோகத்தில் முன்னேற்றம் காணும் நாள். மேலதிகாரிகளின் பாராட்டு கிடைக்கும்; எடுத்த காரியம் வெற்றியாகும். புதிய பொறுப்புகள் தேடி வரும்.",
            "Progress in career and business. Superiors appreciate you, tasks succeed and new responsibilities come your way.",
        ),
        Label(
            "லாபகரமான நாள். பண வரவு அதிகரிக்கும்; நண்பர்கள் மூலம் நல்ல செய்தி வரும். நீண்ட நாள் விருப்பங்கள் நிறைவேறும்.",
            "A profitable day. Income improves, good news arrives through friends and long-held wishes are fulfilled.",
        ),
        Label(
            "செலவுகள் அதிகரிக்கும் நாள். தூக்கமின்மை, அலைச்சல் ஏற்படலாம். சுப செலவுகள், ஆன்மிகப் பயணங்கள் மன நிறைவு தரும்; கடன் கொடுப்பதைத் தவிர்க்கவும்.",
            "Expenses increase and rest may be disturbed. Spending on good causes or a pilgrimage brings satisfaction; avoid lending money.",
        ),
    )
}
