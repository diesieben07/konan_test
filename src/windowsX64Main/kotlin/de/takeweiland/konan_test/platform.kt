package de.takeweiland.konan_test

import kotlinx.cinterop.*
import platform.posix.*;

actual fun readLine(): String? {
    return kotlin.io.readLine()
}

@ExperimentalUnsignedTypes
actual fun terminalWidth(): Int {
    memScoped {
        val w: winsize = alloc()
        ioctl(STDOUT_FILENO, TIOCGWINSZ, w.ptr)
        return w.ws_col.toInt()
    }
}