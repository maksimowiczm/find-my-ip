package com.maksimowiczm.findmyip.feature.settings.language

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.ui.res.languages
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
actual fun LanguageSettingsScreen(modifier: Modifier) {
    LanguageSettingsScreen(
        modifier = modifier,
        viewModel = koinViewModel()
    )
}

@Composable
internal fun LanguageSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = koinViewModel()
) {
    val languageTag = remember { viewModel.tag }
    val onLanguageName = remember(viewModel) {
        { tag: String? ->
            viewModel.onLanguageSelect(tag)
        }
    }

    LanguageSettingsScreen(
        languageTag = languageTag,
        onLanguageSelect = { onLanguageName(it) },
        onSystemLanguageSelect = { onLanguageName(null) },
        modifier = modifier
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun LanguageSettingsScreen(
    languageTag: String,
    onLanguageSelect: (String) -> Unit,
    onSystemLanguageSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val translateLink = stringResource(Res.string.link_translate)
    val onHelpTranslate = remember(uriHandler, translateLink) {
        { uriHandler.openUri(translateLink) }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.headline_language)
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues
        ) {
            item {
                Card(
                    onClick = onHelpTranslate,
                    modifier = Modifier.padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = null
                        )
                        Column {
                            Text(
                                text = stringResource(Res.string.action_translate),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = stringResource(Res.string.neutral_help_translating_the_app),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(Res.string.headline_system))
                    },
                    leadingContent = {
                        RadioButton(
                            selected = languages.none { it.value.tag == languageTag },
                            onClick = { onSystemLanguageSelect() }
                        )
                    },
                    modifier = Modifier.clickable {
                        onSystemLanguageSelect()
                    }
                )
            }

            languages.forEach { (name, translation) ->
                item {
                    ListItem(
                        headlineContent = {
                            Text(name)
                        },
                        supportingContent = {
                            translation.authorsStrings.takeIf { it.isNotEmpty() }?.let {
                                Column {
                                    Text(stringResource(Res.string.headline_authors))

                                    it.forEach { author ->
                                        Text(author.toAnnotatedString())
                                    }
                                }
                            }
                        },
                        leadingContent = {
                            RadioButton(
                                selected = languageTag == translation.tag,
                                onClick = { onLanguageSelect(translation.tag) }
                            )
                        },
                        modifier = Modifier.clickable { onLanguageSelect(translation.tag) }
                    )
                }
            }

            item {
                val context = LocalContext.current
                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
                        val uri = Uri.fromParts("package", context.packageName, null)
                        data = uri
                    }
                } else {
                    Intent()
                }

                val isSystemLocaleSettingsAvailable =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.packageManager
                            .queryIntentActivities(intent, PackageManager.MATCH_ALL)
                            .isNotEmpty()
                    } else {
                        false
                    }

                if (!isSystemLocaleSettingsAvailable) {
                    return@item
                }

                Column {
                    HorizontalDivider()
                    ListItem(
                        headlineContent = {
                            Text(
                                stringResource(Res.string.headline_system_language_settings)
                            )
                        },
                        modifier = Modifier.clickable { context.startActivity(intent) },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}
