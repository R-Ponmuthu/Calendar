package com.tamilcalendar.data

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual object LocalStore {
    private val dir: String? by lazy {
        val fm = NSFileManager.defaultManager
        val base = fm.URLsForDirectory(NSApplicationSupportDirectory, NSUserDomainMask).firstOrNull() as? NSURL
        val path = base?.path?.let { "$it/calendar-data" } ?: return@lazy null
        fm.createDirectoryAtPath(path, withIntermediateDirectories = true, attributes = null, error = null)
        path
    }

    actual fun read(name: String): String? {
        val d = dir ?: return null
        return NSString.stringWithContentsOfFile("$d/$name", encoding = NSUTF8StringEncoding, error = null)
    }

    actual fun write(name: String, text: String) {
        val d = dir ?: return
        NSString.create(string = text).writeToFile("$d/$name", atomically = true, encoding = NSUTF8StringEncoding, error = null)
    }
}
