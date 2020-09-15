package de.takeweiland.konan_test

import kotlinx.cinterop.*
import platform.windows.*

actual fun readLine(): String? {
    return kotlin.io.readLine()
}

@ExperimentalUnsignedTypes
actual fun terminalWidth(): Int {
    memScoped {
        val csbi: CONSOLE_SCREEN_BUFFER_INFO = alloc()
        GetConsoleScreenBufferInfo(GetStdHandle(STD_OUTPUT_HANDLE), csbi.ptr)
        return csbi.srWindow.Right - csbi.srWindow.Left + 1
    }
}