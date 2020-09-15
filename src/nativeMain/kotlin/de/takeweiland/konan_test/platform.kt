package de.takeweiland.konan_test

import kotlinx.cinterop.*
import platform.posix.*;

actual fun readLine(): String? {
    return kotlin.io.readLine()
}

internal fun tryTerminalWidths(vararg toTry: () -> Int): Int {
    for (t in toTry) {
        val w = t()
        if (w > 0) {
            return w
        }
    }
    return -1
}

internal fun terminalWidthPosix(): Int {
    memScoped {
        val w: winsize = alloc()
        return if (ioctl(STDOUT_FILENO, TIOCGWINSZ, w.ptr) < 0) {
            -1
        } else {
            w.ws_col.toInt()
        }
    }
}

internal fun terminalWidthTput(): Int {
    val fp = popen("tput cols 2>/dev/null", "r")
    if (fp == null) {
        return -1
    } else {
        try {
            memScoped {
                val colsVar = alloc<IntVar>()
                fscanf(fp, "%d", colsVar)
                return if (colsVar.value == 0) {
                    -2
                } else {
                    colsVar.value
                }
            }
        } finally {
            val status = pclose(fp)
            if (!wIfExited(status) || wExitStatus(status) != 0) {
                return -3
            }
        }
    }
}

private fun wStatus(status: Int): Int {
    return status and 0x7f // 0177 oct
}

private fun wIfExited(status: Int): Boolean {
    return wStatus(status) == 0
}

private fun wExitStatus(status: Int): Int {
    return status shr 8
}