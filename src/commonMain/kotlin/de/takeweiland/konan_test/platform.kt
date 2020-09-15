package de.takeweiland.konan_test

expect fun readLine(): String?
expect fun terminalWidth(): Int

internal fun tryTerminalWidths(vararg toTry: () -> Int): Int {
    for (t in toTry) {
        val w = t()
        if (w > 0) {
            return w
        }
    }
    return -1
}