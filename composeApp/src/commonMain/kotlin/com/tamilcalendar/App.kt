package com.tamilcalendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamilcalendar.core.Label
import com.tamilcalendar.core.Lang
import com.tamilcalendar.ads.AdPolicy
import com.tamilcalendar.ads.BannerAd
import com.tamilcalendar.core.SimpleDate
import com.tamilcalendar.data.CalendarRepository
import com.tamilcalendar.data.DataLoader
import com.tamilcalendar.data.EventType
import com.tamilcalendar.ui.AboutScreen
import com.tamilcalendar.ui.ChandrashtamamScreen
import com.tamilcalendar.ui.DailyScreen
import com.tamilcalendar.ui.EventListScreen
import com.tamilcalendar.ui.FestivalScreen
import com.tamilcalendar.ui.MonthlyScreen
import com.tamilcalendar.ui.PoruthamScreen
import com.tamilcalendar.ui.RasiPalanScreen
import com.tamilcalendar.ui.TamilCalendarTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Navigation-drawer menus. */
enum class Screen(val title: Label, val group: Int) {
    DAILY(Label("தினசரி காலண்டர்", "Daily Calendar"), 0),
    RASI_PALAN(Label("இன்றைய ராசி பலன்", "Daily Rasi Palan"), 0),
    MONTHLY(Label("மாத காலண்டர்", "Monthly Calendar"), 0),
    MUHURTHAM(Label("முகூர்த்த / திருமண நாட்கள்", "Muhurtham / Wedding Dates"), 1),
    PORUTHAM(Label("திருமணப் பொருத்தம்", "Thirumana Porutham"), 1),
    FESTIVALS(Label("பண்டிகை நாட்கள்", "Festival Dates"), 1),
    POURNAMI(Label("பௌர்ணமி", "Pournami"), 2),
    AMAVASAI(Label("அமாவாசை", "Amavasai"), 2),
    PRADOSHAM(Label("பிரதோஷம்", "Pradosham"), 2),
    VIRATHAM(Label("விரத நாட்கள்", "Viratham Days"), 2),
    KARINAL(Label("கரி நாள்", "Karinal"), 3),
    HOLIDAYS(Label("அரசு விடுமுறைகள்", "TN Govt Holidays"), 3),
    CHANDRASHTAMAM(Label("சந்திராஷ்டமம் (ராசி)", "Chandrashtamam (Rasi)"), 3),
    ABOUT(Label("பற்றி", "About"), 4),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    TamilCalendarTheme {
        var ready by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            // 1. Bundled + cached data files (offline), 2. pre-compute this year's panchangam.
            val local = withContext(Dispatchers.Default) { DataLoader.loadLocal() }
            CalendarRepository.apply(local)
            withContext(Dispatchers.Default) { CalendarRepository.viratham(realTodayIst().year) }
            ready = true
            // 3. Check the hosted manifest for new / corrected years in the background.
            val updated = withContext(Dispatchers.Default) { DataLoader.refreshRemote(local) }
            if (updated != null) CalendarRepository.apply(updated)
        }
        if (!ready) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                    Text("தமிழ் காலண்டர்")
                }
            }
            return@TamilCalendarTheme
        }

        var screen by remember { mutableStateOf(Screen.DAILY) }
        var lang by remember { mutableStateOf(Lang.TAMIL) }
        var selectedDate by remember { mutableStateOf(todayIst()) }
        var monthShown by remember { mutableStateOf(selectedDate.year to selectedDate.month) }
        var listYear by remember { mutableStateOf(selectedDate.year) }
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        fun navigate(s: Screen) {
            if (s != screen) {
                screen = s
                AdPolicy.onScreenChanged()
            }
        }
        val openDay: (SimpleDate) -> Unit = {
            selectedDate = it
            navigate(Screen.DAILY)
        }
        val setYear: (Int) -> Unit = { listYear = it }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            if (lang == Lang.TAMIL) "தமிழ் காலண்டர்" else "Tamil Calendar",
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        var lastGroup = 0
                        Screen.entries.forEach { s ->
                            if (s.group != lastGroup) {
                                HorizontalDivider(Modifier.padding(vertical = 4.dp, horizontal = 16.dp))
                                lastGroup = s.group
                            }
                            NavigationDrawerItem(
                                label = { Text(s.title.get(lang)) },
                                selected = s == screen,
                                onClick = {
                                    navigate(s)
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(horizontal = 12.dp),
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            },
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(screen.title.get(lang), maxLines = 1) },
                        navigationIcon = {
                            TextButton(onClick = { scope.launch { drawerState.open() } }) {
                                Text("☰", fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimary)
                            }
                        },
                        actions = {
                            TextButton(onClick = { lang = if (lang == Lang.TAMIL) Lang.ENGLISH else Lang.TAMIL }) {
                                Text(if (lang == Lang.TAMIL) "EN" else "தமிழ்", color = MaterialTheme.colorScheme.onPrimary)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                },
                bottomBar = { BannerAd(Modifier.navigationBarsPadding()) },
            ) { padding ->
                Box(Modifier.padding(padding).fillMaxSize()) {
                    val y = listYear
                    when (screen) {
                        Screen.DAILY -> DailyScreen(selectedDate, lang, onDateChange = { selectedDate = it },
                            onOpenPalan = { navigate(Screen.RASI_PALAN) })
                        Screen.RASI_PALAN -> RasiPalanScreen(selectedDate, lang, onDateChange = { selectedDate = it })
                        Screen.MONTHLY -> MonthlyScreen(monthShown.first, monthShown.second, lang,
                            onMonthChange = { yr, m -> monthShown = yr to m }, onDayClick = openDay)
                        Screen.MUHURTHAM -> EventListScreen(CalendarRepository.events(y, EventType.MUHURTHAM), y, lang, openDay, setYear,
                            showPanchangDetail = true,
                            note = Label("சுப முகூர்த்த / திருமண நாட்கள் – வளர்பிறை/தேய்பிறை, நட்சத்திரம், திதி விவரங்களுடன். திருமண நாளை இறுதி செய்யும் முன் ஜோதிடரை அணுகவும்.",
                                "Subha muhurtham / wedding dates with paksha, star and tithi. Consult an astrologer before finalising a wedding date."))
                        Screen.PORUTHAM -> PoruthamScreen(lang)
                        Screen.FESTIVALS -> FestivalScreen(y, lang, openDay, setYear)
                        Screen.POURNAMI -> EventListScreen(CalendarRepository.events(y, EventType.POURNAMI), y, lang, openDay, setYear, showTithiTimes = true)
                        Screen.AMAVASAI -> EventListScreen(CalendarRepository.events(y, EventType.AMAVASAI), y, lang, openDay, setYear, showTithiTimes = true)
                        Screen.PRADOSHAM -> EventListScreen(CalendarRepository.events(y, EventType.PRADOSHAM), y, lang, openDay, setYear, showTithiTimes = true,
                            note = Label("பிரதோஷ வேளை: மாலை 4:30 – 6:00", "Pradosha time: 4:30 PM – 6:00 PM"))
                        Screen.VIRATHAM -> EventListScreen(CalendarRepository.viratham(y), y, lang, openDay, setYear, showTitle = true, computed = true,
                            note = Label("திருக்கணித முறைப்படி கணக்கிடப்பட்டது (சென்னை).", "Computed using Thirukanitha (drik) method for Chennai."))
                        Screen.KARINAL -> EventListScreen(CalendarRepository.events(y, EventType.KARINAL), y, lang, openDay, setYear,
                            note = Label("கரி நாட்களில் சுப காரியங்களைத் தவிர்க்கவும்.", "Avoid auspicious events on Karinal days."))
                        Screen.HOLIDAYS -> EventListScreen(CalendarRepository.events(y, EventType.GOVT_HOLIDAY), y, lang, openDay, setYear, showTitle = true)
                        Screen.CHANDRASHTAMAM -> ChandrashtamamScreen(y, lang, openDay, setYear)
                        Screen.ABOUT -> AboutScreen(lang)
                    }
                }
            }
        }
    }
}
