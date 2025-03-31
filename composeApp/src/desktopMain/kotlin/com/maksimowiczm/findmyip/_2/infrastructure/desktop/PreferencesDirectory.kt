@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.infrastructure.desktop

import java.io.File

val preferencesDirectory: String
    get() {
        val file = File(System.getProperty("java.io.tmpdir"), "FindMyIp").apply {
            if (!exists()) {
                mkdir()
            }
        }

        return file.absolutePath
    }
