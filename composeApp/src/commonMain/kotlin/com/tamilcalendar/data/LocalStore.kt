package com.tamilcalendar.data

/** Tiny per-app text file store used to cache downloaded data files. */
expect object LocalStore {
    fun read(name: String): String?
    fun write(name: String, text: String)
}
