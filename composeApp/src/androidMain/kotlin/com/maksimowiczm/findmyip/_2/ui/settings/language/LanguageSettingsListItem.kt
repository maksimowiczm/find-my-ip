@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.ui.settings.language

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun LanguageSettingsListItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = koinViewModel()
) {
    val language = remember { viewModel.languageName }

    LanguageSettingsListItem(
        onClick = onClick,
        language = language,
        modifier = modifier
    )
}

@Composable
private fun LanguageSettingsListItem(
    onClick: () -> Unit,
    language: String,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(stringResource(Res.string.headline_language))
        },
        modifier = modifier.clickable { onClick() },
        supportingContent = {
            Text(language)
        }
    )
}
