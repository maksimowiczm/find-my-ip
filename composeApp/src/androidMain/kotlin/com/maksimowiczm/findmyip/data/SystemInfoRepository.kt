package com.maksimowiczm.findmyip.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

internal class SystemInfoRepository(private val context: Context) {
    val defaultLocale: Locale
        get() {
            val compat = AppCompatDelegate.getApplicationLocales().get(0)
            if (compat != null) {
                return compat
            }

            val config = context.resources.configuration.locales.get(0)

            if (config != null) {
                return config
            }

            val fallback = Locale.getDefault()

            return fallback
        }
}
