package com.maksimowiczm.findmyip.shared.feature.language.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.core.os.LocaleListCompat

@Composable
internal actual fun LanguageScreen(
    onBack: () -> Unit,
    onTranslate: () -> Unit,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val language = Locale.current.toLanguageTag().takeIf { languages.containsTag(it) }
    val onUpdateLanguage =
        remember(context) {
            { tag: String? ->
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
            }
        }

    LanguageScreen(
        currentTag = language,
        onUpdateLanguage = onUpdateLanguage,
        onBack = onBack,
        onTranslate = onTranslate,
        modifier = modifier,
    )
}
