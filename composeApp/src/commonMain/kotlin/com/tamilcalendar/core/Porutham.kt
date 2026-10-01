package com.tamilcalendar.core

enum class Match(val label: Label, val score: Double) {
    UTHAMAM(Label("உத்தமம்", "Excellent"), 1.0),
    MATHIMAM(Label("மத்திமம்", "Average"), 0.5),
    NONE(Label("பொருத்தம் இல்லை", "Not matching"), 0.0),
}

data class PoruthamResult(
    val name: Label,
    val match: Match,
    val detail: Label,
    /** Rajju and Vedhai are treated as essential in Tamil tradition. */
    val essential: Boolean = false,
)

/**
 * Thirumana porutham (10 poruthams, Tamil tradition). Counting is always from the
 * bride's star / rasi to the groom's. Star indices: 0 = Ashwini … 26 = Revathi;
 * rasi indices: 0 = Mesham … 11 = Meenam.
 */
object Porutham {

    /** Stars that have at least one padham in [rasi]. */
    fun starsInRasi(rasi: Int): List<Int> = ((9 * rasi) / 4..(9 * rasi + 8) / 4).toList()

    /** Padhams (1..4) of [star] that fall in [rasi], e.g. Karthigai in Rishabam -> [2,3,4]. */
    fun padhams(star: Int, rasi: Int): List<Int> = (0..3).filter { (star * 4 + it) / 9 == rasi }.map { it + 1 }

    // ---- tables -------------------------------------------------------------------
    private enum class Gana(val label: Label) {
        DEVA(Label("தேவ கணம்", "Deva ganam")),
        MANUSHA(Label("மனுஷ கணம்", "Manusha ganam")),
        RAKSHASA(Label("ராட்சஸ கணம்", "Rakshasa ganam")),
    }

    private val ganas: List<Gana> = "DMRMDMDDRRMMDRDRDRRMMDRRMMD".map {
        when (it) { 'D' -> Gana.DEVA; 'M' -> Gana.MANUSHA; else -> Gana.RAKSHASA }
    }

    private enum class Yoni(val label: Label) {
        HORSE(Label("குதிரை", "Horse")), ELEPHANT(Label("யானை", "Elephant")), GOAT(Label("ஆடு", "Goat")),
        SERPENT(Label("பாம்பு", "Serpent")), DOG(Label("நாய்", "Dog")), CAT(Label("பூனை", "Cat")),
        RAT(Label("எலி", "Rat")), COW(Label("பசு", "Cow")), BUFFALO(Label("எருமை", "Buffalo")),
        TIGER(Label("புலி", "Tiger")), DEER(Label("மான்", "Deer")), MONKEY(Label("குரங்கு", "Monkey")),
        MONGOOSE(Label("கீரி", "Mongoose")), LION(Label("சிங்கம்", "Lion")),
    }

    private val yonis = listOf(
        Yoni.HORSE, Yoni.ELEPHANT, Yoni.GOAT, Yoni.SERPENT, Yoni.SERPENT, Yoni.DOG, Yoni.CAT,
        Yoni.GOAT, Yoni.CAT, Yoni.RAT, Yoni.RAT, Yoni.COW, Yoni.BUFFALO, Yoni.TIGER,
        Yoni.BUFFALO, Yoni.TIGER, Yoni.DEER, Yoni.DEER, Yoni.DOG, Yoni.MONKEY, Yoni.MONGOOSE,
        Yoni.MONKEY, Yoni.LION, Yoni.HORSE, Yoni.LION, Yoni.COW, Yoni.ELEPHANT,
    )

    private val yoniEnemies = listOf(
        Yoni.HORSE to Yoni.BUFFALO, Yoni.ELEPHANT to Yoni.LION, Yoni.GOAT to Yoni.MONKEY,
        Yoni.SERPENT to Yoni.MONGOOSE, Yoni.DOG to Yoni.DEER, Yoni.CAT to Yoni.RAT, Yoni.COW to Yoni.TIGER,
    )

    private enum class Rajju(val label: Label) {
        PAADHA(Label("பாத ரஜ்ஜு", "Paadha rajju")),
        THODAI(Label("தொடை ரஜ்ஜு", "Thodai (Ooru) rajju")),
        NAABHI(Label("நாபி (உதர) ரஜ்ஜு", "Naabhi (Udhara) rajju")),
        KANDA(Label("கண்ட ரஜ்ஜு", "Kanda rajju")),
        SIRASU(Label("சிரசு ரஜ்ஜு", "Sirasu rajju")),
    }

    private fun rajju(star: Int): Rajju = when (star % 9) {
        0, 8 -> Rajju.PAADHA
        1, 7 -> Rajju.THODAI
        2, 6 -> Rajju.NAABHI
        3, 5 -> Rajju.KANDA
        else -> Rajju.SIRASU
    }

    /** Vedhai (mutually obstructing) star pairs. */
    private val vedhaPairs: Set<Pair<Int, Int>> = listOf(
        0 to 17, 1 to 16, 2 to 15, 3 to 14, 5 to 21, 6 to 20, 7 to 19, 8 to 18,
        9 to 26, 10 to 25, 11 to 24, 12 to 23, 4 to 13, 4 to 22, 13 to 22,
    ).flatMap { listOf(it, it.second to it.first) }.toSet()

    // Planets: 0 Sun, 1 Moon, 2 Mars, 3 Mercury, 4 Jupiter, 5 Venus, 6 Saturn
    private val planetNames = listOf(
        Label("சூரியன்", "Sun"), Label("சந்திரன்", "Moon"), Label("செவ்வாய்", "Mars"), Label("புதன்", "Mercury"),
        Label("குரு", "Jupiter"), Label("சுக்கிரன்", "Venus"), Label("சனி", "Saturn"),
    )
    private val rasiLord = listOf(2, 5, 3, 1, 0, 3, 5, 2, 4, 6, 6, 4)

    /** Natural friendship: 'F' friend, 'N' neutral, 'E' enemy; row = planet, column = other planet. */
    private val friendship = listOf(
        "-FFNFEE", // Sun
        "F-NFNNN", // Moon
        "FF-EFNN", // Mars: friends Sun, Moon, Jupiter; enemy Mercury
        "FEN-NFN", // Mercury: friends Sun, Venus; enemy Moon
        "FFFE-EN", // Jupiter: friends Sun, Moon, Mars; enemies Mercury, Venus
        "EENFN-F", // Venus: friends Mercury, Saturn; enemies Sun, Moon
        "EEEFNF-", // Saturn: friends Mercury, Venus; enemies Sun, Moon, Mars
    )

    /** Vasya rasis for each rasi (Tamil table). */
    private val vasya = listOf(
        setOf(4, 7), setOf(3, 6), setOf(5), setOf(7, 8), setOf(6), setOf(1, 11),
        setOf(9), setOf(3, 5), setOf(11), setOf(10, 0), setOf(11), setOf(9),
    )

    // ---- calculation --------------------------------------------------------------

    /** Count from bride's star to groom's star, inclusive (1..27). */
    fun starCount(brideStar: Int, groomStar: Int) = ((groomStar - brideStar + 27) % 27) + 1

    /** Count from bride's rasi to groom's rasi, inclusive (1..12). */
    fun rasiCount(brideRasi: Int, groomRasi: Int) = ((groomRasi - brideRasi + 12) % 12) + 1

    /**
     * Returns the 10 poruthams. When either star is null only the three rasi-based
     * poruthams (Rasi, Rasi Adhipathi, Vasyam) are returned.
     */
    fun match(groomRasi: Int, groomStar: Int?, brideRasi: Int, brideStar: Int?): List<PoruthamResult> {
        val out = ArrayList<PoruthamResult>()
        val starsKnown = groomStar != null && brideStar != null
        if (starsKnown) {
            val g = groomStar!!; val b = brideStar!!
            val n = starCount(b, g)
            out += dinam(n)
            out += ganam(g, b)
            out += mahendram(n)
            out += streeDeergham(n)
            out += yoni(g, b)
        }
        out += rasi(groomRasi, brideRasi, starsKnown && groomStar == brideStar)
        out += rasiAdhipathi(groomRasi, brideRasi)
        out += vasyam(groomRasi, brideRasi)
        if (starsKnown) {
            out += rajju(groomStar!!, brideStar!!)
            out += vedhai(groomStar, brideStar)
        }
        return out
    }

    fun totalScore(results: List<PoruthamResult>): Double = results.sumOf { it.match.score }

    private fun lbl(ta: String, en: String) = Label(ta, en)

    private fun dinam(n: Int): PoruthamResult {
        val name = lbl("தினப் பொருத்தம்", "Dina porutham")
        return when {
            n == 1 -> PoruthamResult(name, Match.MATHIMAM, lbl("இருவருக்கும் ஒரே நட்சத்திரம் – மத்திமம்", "Same star for both – average"))
            n % 9 in setOf(0, 2, 4, 6, 8) -> PoruthamResult(name, Match.UTHAMAM, lbl("பெண் நட்சத்திரத்திலிருந்து ஆண் நட்சத்திரம் $n-வது", "Groom's star is no. $n counting from the bride's"))
            else -> PoruthamResult(name, Match.NONE, lbl("பெண் நட்சத்திரத்திலிருந்து ஆண் நட்சத்திரம் $n-வது", "Groom's star is no. $n counting from the bride's"))
        }
    }

    private fun ganam(g: Int, b: Int): PoruthamResult {
        val gg = ganas[g]; val bg = ganas[b]
        val m = when {
            gg == bg -> Match.UTHAMAM
            gg == Gana.RAKSHASA || bg == Gana.RAKSHASA -> Match.NONE
            else -> Match.MATHIMAM // Deva + Manusha
        }
        return PoruthamResult(
            lbl("கணப் பொருத்தம்", "Gana porutham"), m,
            lbl("ஆண்: ${gg.label.ta}, பெண்: ${bg.label.ta}", "Groom: ${gg.label.en}, Bride: ${bg.label.en}"),
        )
    }

    private fun mahendram(n: Int) = PoruthamResult(
        lbl("மகேந்திரப் பொருத்தம்", "Mahendra porutham"),
        if (n in setOf(4, 7, 10, 13, 16, 19, 22, 25)) Match.UTHAMAM else Match.NONE,
        lbl("4, 7, 10, 13, 16, 19, 22, 25-வது நட்சத்திரமாக இருந்தால் பொருத்தம் (இங்கு $n)",
            "Matches when the count is 4, 7, 10, 13, 16, 19, 22 or 25 (here $n)"),
    )

    private fun streeDeergham(n: Int) = PoruthamResult(
        lbl("ஸ்திரீ தீர்க்கப் பொருத்தம்", "Stree Deergha porutham"),
        when { n > 13 -> Match.UTHAMAM; n > 7 -> Match.MATHIMAM; else -> Match.NONE },
        lbl("13-க்கு மேல் உத்தமம், 7-க்கு மேல் மத்திமம் (இங்கு $n)", "Above 13 excellent, above 7 average (here $n)"),
    )

    private fun yoni(g: Int, b: Int): PoruthamResult {
        val gy = yonis[g]; val by = yonis[b]
        val enemy = yoniEnemies.any { (x, y) -> (x == gy && y == by) || (x == by && y == gy) }
        val m = when { gy == by -> Match.UTHAMAM; enemy -> Match.NONE; else -> Match.MATHIMAM }
        return PoruthamResult(
            lbl("யோனிப் பொருத்தம்", "Yoni porutham"), m,
            lbl("ஆண்: ${gy.label.ta}, பெண்: ${by.label.ta}${if (enemy) " – பகை யோனி" else ""}",
                "Groom: ${gy.label.en}, Bride: ${by.label.en}${if (enemy) " – enemy yonis" else ""}"),
        )
    }

    private fun rasi(groomRasi: Int, brideRasi: Int, sameStar: Boolean): PoruthamResult {
        val r = rasiCount(brideRasi, groomRasi)
        val m = when (r) {
            1 -> if (sameStar) Match.NONE else Match.MATHIMAM
            2, 6, 8, 12 -> Match.NONE
            7, 9, 10, 11 -> Match.UTHAMAM
            else -> Match.MATHIMAM // 3, 4, 5
        }
        val note = when (r) {
            1 -> lbl("ஒரே ராசி", "Same rasi")
            6, 8 -> lbl("சஷ்டாஷ்டகம் (6/8)", "Shashtashtakam (6/8)")
            2, 12 -> lbl("த்விர்த்வாதசம் (2/12)", "Dwirdwadasam (2/12)")
            7 -> lbl("சமசப்தமம் (7/7)", "Samasapthamam (7/7)")
            else -> lbl("பெண் ராசியிலிருந்து ஆண் ராசி $r-வது", "Groom's rasi is no. $r counting from the bride's")
        }
        return PoruthamResult(lbl("ராசிப் பொருத்தம்", "Rasi porutham"), m, note)
    }

    private fun rasiAdhipathi(groomRasi: Int, brideRasi: Int): PoruthamResult {
        val a = rasiLord[groomRasi]; val b = rasiLord[brideRasi]
        val m = if (a == b) Match.UTHAMAM else {
            val ab = friendship[a][b]; val ba = friendship[b][a]
            when {
                ab == 'E' || ba == 'E' -> Match.NONE
                ab == 'F' && ba == 'F' -> Match.UTHAMAM
                else -> Match.MATHIMAM
            }
        }
        return PoruthamResult(
            lbl("ராசி அதிபதி பொருத்தம்", "Rasi Adhipathi porutham"), m,
            lbl("ஆண்: ${planetNames[a].ta}, பெண்: ${planetNames[b].ta}", "Groom: ${planetNames[a].en}, Bride: ${planetNames[b].en}"),
        )
    }

    private fun vasyam(groomRasi: Int, brideRasi: Int): PoruthamResult {
        val ok = groomRasi in vasya[brideRasi] || brideRasi in vasya[groomRasi]
        return PoruthamResult(
            lbl("வசியப் பொருத்தம்", "Vasya porutham"),
            if (ok) Match.UTHAMAM else Match.NONE,
            if (ok) lbl("வசிய ராசிகள்", "Vasya rasis") else lbl("வசியம் இல்லை", "Not vasya rasis"),
        )
    }

    private fun rajju(g: Int, b: Int): PoruthamResult {
        val gr = rajju(g); val br = rajju(b)
        return PoruthamResult(
            lbl("ரஜ்ஜுப் பொருத்தம்", "Rajju porutham"),
            if (gr == br) Match.NONE else Match.UTHAMAM,
            if (gr == br) lbl("இருவரும் ${gr.label.ta} – ரஜ்ஜு தோஷம்", "Both in ${gr.label.en} – rajju dosham")
            else lbl("ஆண்: ${gr.label.ta}, பெண்: ${br.label.ta}", "Groom: ${gr.label.en}, Bride: ${br.label.en}"),
            essential = true,
        )
    }

    private fun vedhai(g: Int, b: Int): PoruthamResult {
        val bad = (g to b) in vedhaPairs
        return PoruthamResult(
            lbl("வேதைப் பொருத்தம்", "Vedhai porutham"),
            if (bad) Match.NONE else Match.UTHAMAM,
            if (bad) lbl("வேதை நட்சத்திரங்கள்", "Vedhai (obstructing) stars") else lbl("வேதை இல்லை", "No vedhai"),
            essential = true,
        )
    }
}
