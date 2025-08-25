package com.maksimowiczm.findmyip.screenshot

import androidx.compose.runtime.Composable

interface Screenshot {
    val name: String

    @Composable fun Content()
}
