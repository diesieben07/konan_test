package de.takeweiland.konan_test

import kotlinx.cinterop.*
import platform.posix.*;

actual typealias PlatformFilePointer = platform.posix._IO_FILE

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun platformPopen(command: String, mode: String): CPointer<FILE> = popen(command, mode)

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun platformPclose(file: CValuesRef<FILE>): Int = pclose(command, mode)

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



