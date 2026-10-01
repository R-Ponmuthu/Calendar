package com.tamilcalendar.data

import java.io.File

actual object LocalStore {
    private val dir: File by lazy {
        File(System.getProperty("user.home"), ".tamilcalendar").apply { mkdirs() }
    }

    actual fun read(name: String): String? =
        File(dir, name).takeIf { it.isFile }?.let { runCatching { it.readText() }.getOrNull() }

    actual fun write(name: String, text: String) {
        runCatching { File(dir, name).writeText(text) }
    }
}
