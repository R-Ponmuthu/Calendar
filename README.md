# Tamil Calendar (Kotlin Multiplatform)

A Compose Multiplatform app (Android · iOS · Desktop) modelled on
[tamildailycalendar.com](https://www.tamildailycalendar.com/tamil_daily_calendar.php), with yearly data loaded from JSON (2026 bundled; new years published online).

## Menus (navigation drawer)
| Menu | Content |
|---|---|
| தினசரி காலண்டர் / Daily Calendar | Date sheet with Tamil date & year, Nalla Neram, Gowri Nalla Neram, Rahu Kalam, Yemagandam, Kuligai, Soolam & Parigaram, Chandrashtamam, Sunrise/Sunset, Tithi, Nakshatram, Yogam, Karanam (with end times), Paksham, Naal (Mel/Keezh/Sama nokku), Udhaya Lagnam with remaining naazhigai/vinadi, the day's festivals. Yesterday / Today / Tomorrow / date picker. |
| இன்றைய ராசி பலன் / Daily Rasi Palan | Palan for all 12 rasis for any date, from the Moon's transit (Chandra gochara) with star rating, chandrashtamam warning and the time the Moon changes sign. Hand-written text can override it per date via `rasipalan/<year>.json`. |
| மாத காலண்டர் / Monthly Calendar | Month grid with Tamil dates, holiday/muhurtham/pournami/amavasai/karinal markers and the month's important days. Navigates across years. |
| முகூர்த்த / திருமண நாட்கள் | Subha muhurtham / wedding dates with paksha, star, tithi, nalla neram (the duplicate Wedding menu was merged into this one). |
| திருமணப் பொருத்தம் / Thirumana Porutham | Choose groom's and bride's rasi (and star/padham) → 10 poruthams: Dinam, Ganam, Mahendram, Stree Deergham, Yoni, Rasi, Rasi Adhipathi, Vasyam, Rajju, Vedhai, with score and verdict. Rasi-only gives the 3 rasi-based poruthams. |
| பண்டிகை நாட்கள் / Festival Dates | Hindu, Christian, Muslim festivals per year (tabs). |
| பௌர்ணமி / அமாவாசை / பிரதோஷம் | Dates per year with tithi start/end times. |
| விரத நாட்கள் / Viratham | Ekadasi, Sashti, Chathurthi, Sankatahara Chathurthi, Masa Sivarathri, Karthigai, Thiruvonam (computed). |
| கரி நாள் / Karinal | Karinal days per year. |
| அரசு விடுமுறைகள் / TN Govt Holidays | Tamil Nadu government holidays per year. |
| சந்திராஷ்டமம் / Chandrashtamam | Chandrashtamam periods per rasi, any year. |

A Tamil ⇄ English toggle is in the top bar.

## How the data works
* `core/Astro.kt` – self-contained astronomy (Meeus: ELP-2000/82 Moon, Sun, Lahiri ayanamsa, sunrise, ascendant).
* `core/Panchangam.kt` – Tithi, Nakshatram, Yogam, Karanam with end times, Tamil date, Lagnam, Chandrashtamam, Naal, weekday tables. Works for any year (Chennai, IST).
* `core/RasiPalan.kt`, `core/Porutham.kt` – daily palan and marriage matching.
* **Yearly lists are JSON** – `hosting/v1/calendar/<year>.json` (festivals, govt holidays, muhurtham, karinal, pournami, amavasai, pradosham, Vakya Tamil month starts). A copy is bundled in `composeResources/files`; at launch the app checks the hosted `manifest.json` and downloads newer/extra years (cached on device). See **`hosting/README.md`** for adding 2027+ and free hosting on GitHub Pages or Firebase. Set the URL in `data/RemoteConfig.kt`.
* After editing any JSON run `python3 hosting/tools/publish.py` (validates, rebuilds the manifest, syncs the bundled copy).

**Note:** the website follows the *Vakya* panchangam; this app computes *Thirukanitha* (astronomical) times, so tithi/nakshatra end times can differ from the site by 1–3 hours.

## Ads (AdMob)
* Bottom **anchored adaptive banner** on every screen and an **interstitial** on menu changes (every 3rd change, at most once per 90 s – see `ads/Ads.kt › AdPolicy`).
* **Android:** debug builds always use Google test ads. For release add to `gradle.properties` (or `~/.gradle/gradle.properties`): `admob.appId`, `admob.bannerId`, `admob.interstitialId`. Add an `app-ads.txt` on your developer website and set up the consent (UMP) message in AdMob for EEA/UK users.
* **iOS:** optional – follow the steps at the top of `iosApp/iosApp/AdMobBridge.swift` (add the GoogleMobileAds Swift package, add the file to the target, register the bridge). Replace `GADApplicationIdentifier` in `Info.plist`.
* **Desktop:** no ads.

## Run
* **Android:** open in Android Studio (Ladybug+ with the Kotlin Multiplatform plugin) → run `composeApp`, or `./gradlew :composeApp:assembleDebug`.
* **iOS:** open `iosApp/iosApp.xcodeproj` in Xcode and run (set your Team in `iosApp/Configuration/Config.xcconfig`).
* **Desktop:** `./gradlew :composeApp:run`
* **Tests:** `./gradlew :composeApp:desktopTest`

Versions: Kotlin 2.2.20, Compose Multiplatform 1.9.0, AGP 8.11.1, Gradle 8.14.3 (adjust in `gradle/libs.versions.toml` if your IDE needs others).
