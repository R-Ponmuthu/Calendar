package com.tamilcalendar.core

/** A bilingual label. */
data class Label(val ta: String, val en: String) {
    fun get(lang: Lang): String = if (lang == Lang.TAMIL) ta else en
    fun both(): String = "$ta ($en)"
}

enum class Lang { TAMIL, ENGLISH }

object Names {
    val weekdays = listOf(
        Label("ஞாயிறு", "Sunday"), Label("திங்கள்", "Monday"), Label("செவ்வாய்", "Tuesday"),
        Label("புதன்", "Wednesday"), Label("வியாழன்", "Thursday"), Label("வெள்ளி", "Friday"),
        Label("சனி", "Saturday"),
    )

    val englishMonths = listOf(
        Label("ஜனவரி", "January"), Label("பிப்ரவரி", "February"), Label("மார்ச்", "March"),
        Label("ஏப்ரல்", "April"), Label("மே", "May"), Label("ஜூன்", "June"),
        Label("ஜூலை", "July"), Label("ஆகஸ்ட்", "August"), Label("செப்டம்பர்", "September"),
        Label("அக்டோபர்", "October"), Label("நவம்பர்", "November"), Label("டிசம்பர்", "December"),
    )

    /** Index 0 = Chithirai (Sun in Mesham). */
    val tamilMonths = listOf(
        Label("சித்திரை", "Chithirai"), Label("வைகாசி", "Vaikasi"), Label("ஆனி", "Aani"),
        Label("ஆடி", "Aadi"), Label("ஆவணி", "Avani"), Label("புரட்டாசி", "Purattasi"),
        Label("ஐப்பசி", "Aippasi"), Label("கார்த்திகை", "Karthigai"), Label("மார்கழி", "Margazhi"),
        Label("தை", "Thai"), Label("மாசி", "Masi"), Label("பங்குனி", "Panguni"),
    )

    val rasis = listOf(
        Label("மேஷம்", "Mesham"), Label("ரிஷபம்", "Rishabam"), Label("மிதுனம்", "Mithunam"),
        Label("கடகம்", "Kadagam"), Label("சிம்மம்", "Simmam"), Label("கன்னி", "Kanni"),
        Label("துலாம்", "Thulam"), Label("விருச்சிகம்", "Viruchigam"), Label("தனுசு", "Dhanusu"),
        Label("மகரம்", "Magaram"), Label("கும்பம்", "Kumbam"), Label("மீனம்", "Meenam"),
    )

    val nakshatras = listOf(
        Label("அசுவினி", "Ashwini"), Label("பரணி", "Bharani"), Label("கார்த்திகை", "Karthigai"),
        Label("ரோகிணி", "Rohini"), Label("மிருகசீரிடம்", "Mirugaseerisham"), Label("திருவாதிரை", "Thiruvathirai"),
        Label("புனர்பூசம்", "Punarpoosam"), Label("பூசம்", "Poosam"), Label("ஆயில்யம்", "Ayilyam"),
        Label("மகம்", "Magam"), Label("பூரம்", "Pooram"), Label("உத்திரம்", "Uthiram"),
        Label("அஸ்தம்", "Hastham"), Label("சித்திரை", "Chithirai"), Label("சுவாதி", "Swathi"),
        Label("விசாகம்", "Visakam"), Label("அனுஷம்", "Anusham"), Label("கேட்டை", "Kettai"),
        Label("மூலம்", "Moolam"), Label("பூராடம்", "Pooradam"), Label("உத்திராடம்", "Uthiradam"),
        Label("திருவோணம்", "Thiruvonam"), Label("அவிட்டம்", "Avittam"), Label("சதயம்", "Sathayam"),
        Label("பூரட்டாதி", "Poorattathi"), Label("உத்திரட்டாதி", "Uthirattathi"), Label("ரேவதி", "Revathi"),
    )

    private val tithiBase = listOf(
        Label("பிரதமை", "Prathamai"), Label("துவிதியை", "Dwithiyai"), Label("திருதியை", "Thrithiyai"),
        Label("சதுர்த்தி", "Chathurthi"), Label("பஞ்சமி", "Panchami"), Label("சஷ்டி", "Sashti"),
        Label("சப்தமி", "Sapthami"), Label("அஷ்டமி", "Ashtami"), Label("நவமி", "Navami"),
        Label("தசமி", "Dasami"), Label("ஏகாதசி", "Ekadasi"), Label("துவாதசி", "Dwadasi"),
        Label("திரயோதசி", "Thrayodasi"), Label("சதுர்த்தசி", "Chathurdasi"),
    )

    /** 30 tithis: 0..14 Valarpirai (ending Pournami), 15..29 Theipirai (ending Amavasai). */
    val tithis: List<Label> = tithiBase + Label("பௌர்ணமி", "Pournami") + tithiBase + Label("அமாவாசை", "Amavasai")

    val yogas = listOf(
        Label("விஷ்கம்பம்", "Vishkambam"), Label("ப்ரீதி", "Preethi"), Label("ஆயுஷ்மான்", "Ayushman"),
        Label("சௌபாக்யம்", "Saubhagyam"), Label("சோபனம்", "Sobanam"), Label("அதிகண்டம்", "Athigandam"),
        Label("சுகர்மம்", "Sukarmam"), Label("திருதி", "Dhriti"), Label("சூலம்", "Soolam"),
        Label("கண்டம்", "Gandam"), Label("விருத்தி", "Vruddhi"), Label("துருவம்", "Dhruvam"),
        Label("வியாகாதம்", "Vyagatham"), Label("ஹர்ஷணம்", "Harshanam"), Label("வஜ்ரம்", "Vajram"),
        Label("சித்தி", "Siddhi"), Label("வியதீபாதம்", "Vyatheepatham"), Label("வரீயான்", "Variyan"),
        Label("பரிகம்", "Parigam"), Label("சிவம்", "Sivam"), Label("சித்தம்", "Siddham"),
        Label("சாத்தியம்", "Sadhyam"), Label("சுபம்", "Subham"), Label("சுப்பிரம்", "Subhram"),
        Label("பிராம்மியம்", "Brahmam"), Label("ஐந்திரம்", "Aindram"), Label("வைதிருதி", "Vaidhruthi"),
    )

    private val movableKaranas = listOf(
        Label("பவம்", "Bavam"), Label("பாலவம்", "Balavam"), Label("கௌலவம்", "Kaulavam"),
        Label("தைதுலம்", "Thaithulam"), Label("கரசை", "Karasai"), Label("வணிசை", "Vanisai"),
        Label("பத்திரை", "Bhadrai (Vishti)"),
    )

    /** Karana for half-tithi index 0..59. */
    fun karana(i: Int): Label = when (i) {
        0 -> Label("கிம்ஸ்துக்னம்", "Kimsthugnam")
        57 -> Label("சகுனி", "Sakuni")
        58 -> Label("சதுஷ்பாதம்", "Chathushpadam")
        59 -> Label("நாகவம்", "Nagavam")
        else -> movableKaranas[(i - 1) % 7]
    }

    val tamilYears = listOf(
        "பிரபவ" to "Prabhava", "விபவ" to "Vibhava", "சுக்ல" to "Sukla", "பிரமோதூத" to "Pramodhootha",
        "பிரசோற்பத்தி" to "Prajorpathi", "ஆங்கீரச" to "Aangirasa", "ஸ்ரீமுக" to "Srimukha", "பவ" to "Bhava",
        "யுவ" to "Yuva", "தாது" to "Dhaathu", "ஈஸ்வர" to "Eswara", "வெகுதானிய" to "Vehudhanya",
        "பிரமாதி" to "Pramathi", "விக்கிரம" to "Vikrama", "விஷு" to "Vishu", "சித்திரபானு" to "Chitrabhanu",
        "சுபானு" to "Subhanu", "தாரண" to "Dharana", "பார்த்திப" to "Parthiba", "விய" to "Viya",
        "சர்வசித்து" to "Sarvajith", "சர்வதாரி" to "Sarvadhari", "விரோதி" to "Virodhi", "விக்ருதி" to "Vikruthi",
        "கர" to "Kara", "நந்தன" to "Nandhana", "விஜய" to "Vijaya", "ஜய" to "Jaya",
        "மன்மத" to "Manmatha", "துன்முகி" to "Dhunmuki", "ஹேவிளம்பி" to "Hevilambi", "விளம்பி" to "Vilambi",
        "விகாரி" to "Vikari", "சார்வரி" to "Sarvari", "பிலவ" to "Plava", "சுபகிருது" to "Subakiruthu",
        "சோபகிருது" to "Sobakiruthu", "குரோதி" to "Krodhi", "விசுவாவசு" to "Visuvavasu", "பராபவ" to "Parabhava",
        "பிலவங்க" to "Plavanga", "கீலக" to "Keelaka", "சௌமிய" to "Saumya", "சாதாரண" to "Sadharana",
        "விரோதிகிருது" to "Virodhikiruthu", "பரிதாபி" to "Paridhabi", "பிரமாதீச" to "Pramadhisa", "ஆனந்த" to "Aanandha",
        "ராட்சச" to "Rakshasa", "நள" to "Nala", "பிங்கள" to "Pingala", "காளயுக்தி" to "Kalayukthi",
        "சித்தார்த்தி" to "Siddharthi", "ரௌத்திரி" to "Raudhri", "துன்மதி" to "Dhunmathi", "துந்துபி" to "Dhundhubi",
        "ருத்ரோத்காரி" to "Rudhrodhgari", "ரக்தாட்சி" to "Raktakshi", "குரோதன" to "Krodhana", "அட்சய" to "Akshaya",
    ).map { Label(it.first, it.second) }

    val directions = mapOf(
        "E" to Label("கிழக்கு", "East"), "W" to Label("மேற்கு", "West"),
        "N" to Label("வடக்கு", "North"), "S" to Label("தெற்கு", "South"),
    )

    val valarpirai = Label("வளர்பிறை", "Valarpirai (Shukla Paksha)")
    val theipirai = Label("தேய்பிறை", "Theipirai (Krishna Paksha)")

    val melNokku = Label("மேல் நோக்கு நாள்", "Mel Nokku Naal (upward day)")
    val keezhNokku = Label("கீழ் நோக்கு நாள்", "Keezh Nokku Naal (downward day)")
    val samaNokku = Label("சம நோக்கு நாள்", "Sama Nokku Naal (level day)")
}
