package com.tamilcalendar.data

import android.content.Context
import java.io.File

actual object LocalStore {
    private var dir: File? = null

    /** Called from MainActivity before the UI starts. */
    fun init(context: Context) {
        dir = File(context.filesDir, "calendar-data").apply { mkdirs() }
    }

    actual fun read(name: String): String? =
        dir?.let { File(it, name) }?.takeIf { it.isFile }?.let { runCatching { it.readText() }.getOrNull() }

    actual fun write(name: String, text: String) {
        val d = dir ?: return
        runCatching {
            val tmp = File(d, "$name.tmp")
            tmp.writeText(text)
            val target = File(d, name)
            if (!tmp.renameTo(target)) { target.writeText(text); tmp.delete() }
        }
    }
}
