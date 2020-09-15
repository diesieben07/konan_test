package de.takeweiland.konan_test

import kotlinx.cinterop.*
import platform.posix.*;

actual fun terminalWidth(): Int {
    return tryTerminalWidths(
        { terminalWidthPosix() },
        { terminalWidthTput() }
    )
}

internal fun terminalWidthPosix(): Int {
    println("trying width posix")
    memScoped {
        val w: winsize = alloc()
        return if (ioctl(STDOUT_FILENO, TIOCGWINSZ, w.ptr) < 0) {
            -1
        } else {
            w.ws_col.toInt()
        }
    }
}



