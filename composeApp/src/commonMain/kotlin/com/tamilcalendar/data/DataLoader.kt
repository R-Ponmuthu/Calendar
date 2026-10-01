package com.tamilcalendar.data

import com.tamilcalendar.resources.Res
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.decodeFromString
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * Loads the yearly data files.
 *  1. Bundled copy (composeResources/files, synced by hosting/tools/publish.py) – always works offline.
 *  2. Previously downloaded copies (LocalStore) – used when their revision is higher.
 *  3. [refreshRemote] downloads any year whose revision in the hosted manifest is newer.
 */
object DataLoader {
    private const val CACHE_INDEX = "cache_manifest.json"

    private fun cacheName(path: String) = path.replace('/', '_')

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun bundled(path: String): String? =
        runCatching { Res.readBytes("files/$path").decodeToString() }.getOrNull()

    private inline fun <reified T> parse(text: String?): T? =
        text?.let { runCatching { DataJson.decodeFromString<T>(it) }.getOrNull() }

    suspend fun loadLocal(): Dataset {
        val calendars = HashMap<Int, YearFileDto>()
        val palans = HashMap<Int, PalanFileDto>()

        parse<ManifestDto>(bundled("manifest.json"))?.let { m ->
            m.calendar.values.forEach { e -> parse<YearFileDto>(bundled(e.path))?.let { calendars[it.year] = it } }
            m.rasiPalan.values.forEach { e -> parse<PalanFileDto>(bundled(e.path))?.let { palans[it.year] = it } }
        }
        parse<ManifestDto>(LocalStore.read(CACHE_INDEX))?.let { m ->
            m.calendar.values.forEach { e ->
                parse<YearFileDto>(LocalStore.read(cacheName(e.path)))?.let {
                    if (it.revision > (calendars[it.year]?.revision ?: 0)) calendars[it.year] = it
                }
            }
            m.rasiPalan.values.forEach { e ->
                parse<PalanFileDto>(LocalStore.read(cacheName(e.path)))?.let {
                    if (it.revision > (palans[it.year]?.revision ?: 0)) palans[it.year] = it
                }
            }
        }
        return Dataset(calendars, palans)
    }

    /** Returns a new dataset when something newer was downloaded, otherwise null. Never throws. */
    suspend fun refreshRemote(current: Dataset): Dataset? {
        if (!RemoteConfig.enabled) return null
        val client = runCatching {
            HttpClient {
                install(HttpTimeout) {
                    requestTimeoutMillis = 15_000
                    connectTimeoutMillis = 10_000
                }
            }
        }.getOrNull() ?: return null
        try {
            suspend fun fetch(path: String): String? = runCatching {
                val res = client.get(RemoteConfig.DATA_BASE_URL + path)
                if (res.status.isSuccess()) res.bodyAsText() else null
            }.getOrNull()

            val manifest = parse<ManifestDto>(fetch("manifest.json")) ?: return null
            val cacheIndex = parse<ManifestDto>(LocalStore.read(CACHE_INDEX)) ?: ManifestDto()
            val cachedCal = cacheIndex.calendar.toMutableMap()
            val cachedPalan = cacheIndex.rasiPalan.toMutableMap()
            val calendars = current.calendars.toMutableMap()
            val palans = current.palans.toMutableMap()
            var changed = false

            for ((key, entry) in manifest.calendar) {
                val year = key.toIntOrNull() ?: continue
                if ((calendars[year]?.revision ?: 0) >= entry.revision) continue
                val text = fetch(entry.path) ?: continue
                val dto = parse<YearFileDto>(text)?.takeIf { it.year == year } ?: continue
                LocalStore.write(cacheName(entry.path), text)
                cachedCal[key] = ManifestEntry(dto.revision, entry.path)
                calendars[year] = dto
                changed = true
            }
            for ((key, entry) in manifest.rasiPalan) {
                val year = key.toIntOrNull() ?: continue
                if ((palans[year]?.revision ?: 0) >= entry.revision) continue
                val text = fetch(entry.path) ?: continue
                val dto = parse<PalanFileDto>(text)?.takeIf { it.year == year } ?: continue
                LocalStore.write(cacheName(entry.path), text)
                cachedPalan[key] = ManifestEntry(dto.revision, entry.path)
                palans[year] = dto
                changed = true
            }
            if (!changed) return null
            LocalStore.write(
                CACHE_INDEX,
                DataJson.encodeToString(ManifestDto.serializer(), ManifestDto(1, cachedCal, cachedPalan)),
            )
            return Dataset(calendars, palans)
        } catch (t: Throwable) {
            return null
        } finally {
            client.close()
        }
    }
}
