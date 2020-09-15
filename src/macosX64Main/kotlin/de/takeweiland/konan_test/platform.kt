package de.takeweiland.konan_test

actual fun terminalWidth(): Int {
    return tryTerminalWidths(
        { terminalWidthPosix() },
        { terminalWidthTput() }
    )
}