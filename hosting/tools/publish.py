#!/usr/bin/env python3
"""
Validate the Tamil Calendar data files, rebuild v1/manifest.json and copy the data
into the app's bundled resources (offline fallback).

Usage (from the project root):
    python3 hosting/tools/publish.py            # validate + rebuild manifest + sync bundle
    python3 hosting/tools/publish.py --check    # validate only

Workflow for a new year (e.g. 2027):
    1. Copy hosting/v1/calendar/2026.json to 2027.json, set "year": 2027, "revision": 1
       and replace the events / tamilMonthStarts.
    2. Run this script. Fix any errors it prints.
    3. Commit + push the hosting/ folder (GitHub Pages / Firebase). Installed apps pick
       the new year up on their next launch - no app update needed.
    To correct an existing year, edit the file and increase its "revision".
"""
import json, re, shutil, sys
from datetime import date
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
V1 = ROOT / "hosting" / "v1"
BUNDLE = ROOT / "composeApp" / "src" / "commonMain" / "composeResources" / "files"
TYPES = {"FESTIVAL", "CHRISTIAN", "MUSLIM", "GOVT_HOLIDAY", "MUHURTHAM", "KARINAL",
         "POURNAMI", "AMAVASAI", "PRADOSHAM"}
errors = []


def err(msg):
    errors.append(msg)


def parse_date(s, where):
    try:
        return date.fromisoformat(s)
    except Exception:
        err(f"{where}: bad date '{s}' (use YYYY-MM-DD)")
        return None


def check_calendar(path):
    d = json.loads(path.read_text(encoding="utf-8"))
    year = d.get("year")
    if str(year) != path.stem:
        err(f"{path.name}: 'year' ({year}) must match the file name")
    if not isinstance(d.get("revision"), int) or d["revision"] < 1:
        err(f"{path.name}: 'revision' must be an integer >= 1")
    seen = set()
    for i, e in enumerate(d.get("events", [])):
        where = f"{path.name} events[{i}]"
        dt = parse_date(e.get("date", ""), where)
        if dt and dt.year != year:
            err(f"{where}: date {dt} is not in {year}")
        if e.get("type") not in TYPES:
            err(f"{where}: unknown type '{e.get('type')}' (allowed: {sorted(TYPES)})")
        if not e.get("ta") or not e.get("en"):
            err(f"{where}: both 'ta' and 'en' titles are required")
        key = (e.get("date"), e.get("type"), e.get("en"))
        if key in seen:
            err(f"{where}: duplicate event {key}")
        seen.add(key)
    prev = None
    for i, m in enumerate(d.get("tamilMonthStarts", [])):
        dt = parse_date(m.get("date", ""), f"{path.name} tamilMonthStarts[{i}]")
        if not (isinstance(m.get("month"), int) and 0 <= m["month"] <= 11):
            err(f"{path.name} tamilMonthStarts[{i}]: month must be 0 (Chithirai) .. 11 (Panguni)")
        if dt and prev and not (27 <= (dt - prev).days <= 33):
            err(f"{path.name} tamilMonthStarts[{i}]: {dt} is {(dt - prev).days} days after the previous start")
        prev = dt or prev
    return year, d["revision"], len(d.get("events", []))


def check_palan(path):
    d = json.loads(path.read_text(encoding="utf-8"))
    year = d.get("year")
    if str(year) != path.stem:
        err(f"{path.name}: 'year' ({year}) must match the file name")
    for i, e in enumerate(d.get("entries", [])):
        where = f"rasipalan/{path.name} entries[{i}]"
        dt = parse_date(e.get("date", ""), where)
        if dt and dt.year != year:
            err(f"{where}: date not in {year}")
        if not (isinstance(e.get("rasi"), int) and 0 <= e["rasi"] <= 11):
            err(f"{where}: rasi must be 0 (Mesham) .. 11 (Meenam)")
        if not e.get("ta") or not e.get("en"):
            err(f"{where}: both 'ta' and 'en' text are required")
    return year, d.get("revision", 1), len(d.get("entries", []))


def main():
    manifest = {"schemaVersion": 1, "calendar": {}, "rasiPalan": {}}
    for p in sorted((V1 / "calendar").glob("*.json")):
        y, rev, n = check_calendar(p)
        manifest["calendar"][str(y)] = {"revision": rev, "path": f"calendar/{p.name}"}
        print(f"calendar {y}: revision {rev}, {n} events")
    for p in sorted((V1 / "rasipalan").glob("*.json")):
        y, rev, n = check_palan(p)
        manifest["rasiPalan"][str(y)] = {"revision": rev, "path": f"rasipalan/{p.name}"}
        print(f"rasipalan {y}: revision {rev}, {n} entries")
    if errors:
        print("\nERRORS:")
        for e in errors:
            print("  -", e)
        sys.exit(1)
    if "--check" in sys.argv:
        print("OK")
        return
    (V1 / "manifest.json").write_text(json.dumps(manifest, indent=2) + "\n", encoding="utf-8")
    # Sync the bundled (offline) copy inside the app
    for src in V1.rglob("*.json"):
        dst = BUNDLE / src.relative_to(V1)
        dst.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(src, dst)
    print(f"\nmanifest.json written; bundled copy synced to {BUNDLE.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
