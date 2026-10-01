# Tamil Calendar – yearly data (free hosting)

The app reads festival / holiday / muhurtham / karinal / pournami / amavasai /
pradosham lists (and optional hand-written rasi palan) from JSON files:

```
v1/manifest.json            ← list of years + revision numbers (generated)
v1/calendar/2026.json       ← one file per year
v1/rasipalan/2026.json      ← optional, see v1/rasipalan/README.md
tools/publish.py            ← validates files, rebuilds manifest, syncs the app's bundled copy
```

How the app uses them:
1. A copy is **bundled** in the app (`composeApp/src/commonMain/composeResources/files`) so it works offline.
2. On every launch it fetches `manifest.json` from `RemoteConfig.DATA_BASE_URL`
   (`composeApp/src/commonMain/kotlin/com/tamilcalendar/data/RemoteConfig.kt`) and downloads
   any year whose `revision` is newer, then caches it on the device.
   **Adding 2027 later needs no app release** – just publish `2027.json`.

Panchangam, rasi palan, viratham and chandrashtamam are computed in the app for any year,
so only the lists above need yearly updates.

## Adding / correcting a year
1. Copy `v1/calendar/2026.json` → `v1/calendar/2027.json`; set `"year": 2027, "revision": 1`.
2. Replace the `events` (types: FESTIVAL, CHRISTIAN, MUSLIM, GOVT_HOLIDAY, MUHURTHAM, KARINAL,
   POURNAMI, AMAVASAI, PRADOSHAM) and, if you follow a Vakya panchangam, the `tamilMonthStarts`
   (month 0 = Chithirai … 11 = Panguni). Without `tamilMonthStarts` the app computes them.
3. To fix an already-published year, edit it and **increase `revision`**.
4. Run `python3 hosting/tools/publish.py` from the project root.
5. Publish (below).

## Option A – GitHub Pages (recommended, free)
1. Create a public repo named `tamilcalendar-data` on GitHub.
2. Copy the *contents* of this `hosting/` folder into it and push to `main`.
3. Repo ▸ Settings ▸ Pages ▸ Source: *Deploy from a branch*, `main` / `(root)`.
4. Your base URL: `https://<github-user>.github.io/tamilcalendar-data/v1/`
   – put it in `RemoteConfig.DATA_BASE_URL`. Updates go live ~1 minute after each push.

(Same repo via CDN: `https://cdn.jsdelivr.net/gh/<github-user>/tamilcalendar-data@main/v1/` –
faster worldwide but caches up to 12 h.)

## Option B – Firebase Hosting (free Spark plan)
```
npm i -g firebase-tools
firebase login
cd hosting && firebase init hosting   # choose your project, keep the provided firebase.json
firebase deploy --only hosting
```
Base URL: `https://<project-id>.web.app/v1/`
