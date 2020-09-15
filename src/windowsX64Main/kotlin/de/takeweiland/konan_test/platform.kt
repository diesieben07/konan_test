package de.takeweiland.konan_test

import kotlinx.cinterop.*
import platform.windows.*

internal actual typealias PlatformFilePointer = platform.posix.FILE

actual fun terminalWidth(): Int {
    return tryTerminalWidths(
        { terminalWidthWin32() },
        { terminalWidthTput() }

    )
}

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun platformPopen(command: String, mode: String): CPointer<FILE> = _popen(command, mode)

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun platformPClose(file: CValuesRef<FILE>): Int = _pclose(file)

private fun terminalWidthWin32(): Int {
    println("trying width win32")
    memScoped {
        val csbi: CONSOLE_SCREEN_BUFFER_INFO = alloc()
        val returnValue = GetConsoleScreenBufferInfo(GetStdHandle(STD_OUTPUT_HANDLE), csbi.ptr)
        return if (returnValue != 0 && (csbi.srWindow.Right != 0.toShort() || csbi.srWindow.Left != 0.toShort())) {
            csbi.srWindow.Right - csbi.srWindow.Left + 1
        } else {
            -1
        }
    }
}