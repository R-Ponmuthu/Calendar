Optional hand-written Rasi Palan text. Without these files the app shows the
computed (Chandra gochara) palan. Create `<year>.json` like:

{
  "year": 2026,
  "revision": 1,
  "entries": [
    {"date": "2026-10-01", "rasi": 0, "wordTa": "லாபம்", "wordEn": "Profit",
     "ta": "இன்று ...", "en": "Today ..."}
  ]
}

rasi: 0 Mesham, 1 Rishabam, 2 Mithunam, 3 Kadagam, 4 Simmam, 5 Kanni,
6 Thulam, 7 Viruchigam, 8 Dhanusu, 9 Magaram, 10 Kumbam, 11 Meenam.
Entries override the computed one-word result and text only for that date + rasi
(wordTa/wordEn are optional; without them the computed word is kept).
