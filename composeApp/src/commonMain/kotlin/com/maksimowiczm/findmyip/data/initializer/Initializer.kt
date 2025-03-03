package com.maksimowiczm.findmyip.data.initializer

fun interface Initializer {
    suspend operator fun invoke()
}
