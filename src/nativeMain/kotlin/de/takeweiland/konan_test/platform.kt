package de.takeweiland.konan_test

import kotlinx.cinterop.*
import platform.posix.*;

actual fun readLine(): String? {
    return kotlin.io.readLine()
}

internal expect class PlatformFilePointer : CPointed

internal expect inline fun platformPopen(command: String, mode: String): CPointer<PlatformFilePointer>
internal expect inline fun platformPclose(file: CValuesRef<PlatformFilePointer>): Int

internal fun terminalWidthTput(): Int {
    println("trying width tput")
    val fp = platformPopen("tput cols 2>/dev/null", "r")
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
            val status = platformPclose(fp)
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