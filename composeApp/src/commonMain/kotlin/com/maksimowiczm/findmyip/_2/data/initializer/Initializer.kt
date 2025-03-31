@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data.initializer

fun interface Initializer {
    suspend operator fun invoke()
}
