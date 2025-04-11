package com.maksimowiczm.findmyip.feature.settings.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.maksimowiczm.findmyip.data.SystemInfoRepository
import java.util.Locale

internal class LanguageViewModel(private val androidSystemInfoRepository: SystemInfoRepository) :
    ViewModel() {
    private val locale: Locale
        get() = androidSystemInfoRepository.defaultLocale

    val tag: String
        get() = locale.toLanguageTag()

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
