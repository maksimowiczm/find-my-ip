@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.ui.settings.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.maksimowiczm.findmyip._2.data.SystemInfoRepository
import java.util.Locale

class LanguageViewModel(private val systemInfoRepository: SystemInfoRepository) : ViewModel() {
    private val locale: Locale
        get() = systemInfoRepository.defaultLocale

    val tag: String
        get() = locale.toLanguageTag()

    val languageName: String
        get() {
            val tag = locale.toLanguageTag()
            return Locale.forLanguageTag(tag).displayName
        }

    /**
     * Set the application locale based on the language tag.
     *
     * @param tag The language tag to set the application locale to. If null, the locale will be set
     * to the default locale.
     */
    fun onLanguageSelect(tag: String?) {
        if (tag == null) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
        } else {
            val locale = LocaleListCompat.forLanguageTags(tag)
            AppCompatDelegate.setApplicationLocales(locale)
        }
    }
}
