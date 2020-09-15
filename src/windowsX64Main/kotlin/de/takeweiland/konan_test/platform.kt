package de.takeweiland.konan_test

import kotlinx.cinterop.*
import platform.windows.*

@ExperimentalUnsignedTypes
actual fun terminalWidth(): Int {
    return tryTerminalWidths(
        { terminalWidthWin32() },
        { terminalWidthPosix() },
        { terminalWidthTput() }
    )
}

private fun terminalWidthWin32(): Int {
    memScoped {
        val csbi: CONSOLE_SCREEN_BUFFER_INFO = alloc()
        val returnValue = GetConsoleScreenBufferInfo(GetStdHandle(STD_OUTPUT_HANDLE), csbi.ptr)
        return if (returnValue != 0 && (csbi.srWindow.Right != 0 || csbi.srWindow.Left != 0)) {
            csbi.srWindow.Right - csbi.srWindow.Left + 1
        } else {
            -1
        }
    }
}