plugins {
    kotlin("multiplatform") version "1.4.10"
}
group = "de.takeweiland.konan_test"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.useIR = true
            kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
        }
    }
    js {
        nodejs()
    }

    val nativeTargets = listOf(
        linuxX64("linuxX64"),
        macosX64("macosX64")
//        mingwX64("windowsX64")
    )

    for (nativeTarget in nativeTargets) {
        with(nativeTarget) {
            binaries {
                executable {

                }
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

//        val nativePosixMain by creating {
//            dependsOn(nativeMain)
//        }
//
//        val nativeOtherMain by creating {
//            dependsOn(nativeMain)
//        }

        val linuxX64Main by getting {
            dependsOn(nativeMain)
        }
        val macosX64Main by getting {
            dependsOn(nativeMain)
        }
//        val windowsX64Main by getting {
//            dependsOn(nativeOtherMain)
//        }
    }
}