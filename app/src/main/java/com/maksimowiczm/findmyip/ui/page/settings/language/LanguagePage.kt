package com.maksimowiczm.findmyip.ui.page.settings.language

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.infrastructure.android.defaultLocale
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme

@Composable
fun LanguagePage(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val helpTranslateLink = stringResource(R.string.link_help_translate)
    val uriHandler = LocalUriHandler.current

    val context = LocalContext.current
    val onSystemLanguageSettings = remember(context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
                val uri = Uri.fromParts("package", context.packageName, null)
                data = uri
            }

            context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_ALL)
                .takeIf { it.isNotEmpty() }
                ?.let {
                    { context.startActivity(intent) }
                }
        } else {
            null
        }
    }

    LanguagePage(
        tag = remember(context) {
            context.defaultLocale
                .toLanguageTag()
                .takeIf { languages.containsTag(it) }
        },
        onTag = { AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(it)) },
        onHelp = { uriHandler.openUri(helpTranslateLink) },
        onBack = onBack,
        modifier = modifier,
        onSystemLanguageSettings = onSystemLanguageSettings
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePage(
    tag: String?,
    onTag: (String?) -> Unit,
    onHelp: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onSystemLanguageSettings: (() -> Unit)? = null
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(R.string.headline_language))
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag(LanguagePageTestTags.BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_go_back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .testTag(LanguagePageTestTags.LANGUAGE_LIST)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues
        ) {
            item {
                Card(
                    onClick = onHelp,
                    modifier = Modifier
                        .testTag(LanguagePageTestTags.HELP_BUTTON)
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = null
                        )
                        Column {
                            Text(
                                text = stringResource(R.string.action_translate),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = stringResource(R.string.description_translate),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(R.string.headline_system))
                    },
                    modifier = Modifier
                        .testTag(LanguagePageTestTags.Language(null).toString())
                        .clickable { onTag(null) },
                    leadingContent = {
                        RadioButton(
                            selected = tag == null,
                            onClick = { onTag(null) }
                        )
                    }
                )
            }

            languages.forEach { (name, translation) ->
                item {
                    ListItem(
                        headlineContent = { Text(name) },
                        modifier = Modifier
                            .testTag(LanguagePageTestTags.Language(translation.tag).toString())
                            .clickable { onTag(translation.tag) },
                        supportingContent = {
                            translation.authors.takeIf { it.isNotEmpty() }?.let {
                                Column {
                                    it.forEach { author ->
                                        Text(author.toAnnotatedString())
                                    }
                                }
                            }
                        },
                        leadingContent = {
                            RadioButton(
                                selected = tag == translation.tag,
                                onClick = { onTag(translation.tag) }
                            )
                        }
                    )
                }
            }

            if (onSystemLanguageSettings != null) {
                item {
                    HorizontalDivider()
                    ListItem(
                        headlineContent = {
                            Text(stringResource(R.string.headline_system_language_settings))
                        },
                        modifier = Modifier
                            .testTag(LanguagePageTestTags.SYSTEM_LANGUAGE_SETTINGS)
                            .clickable { onSystemLanguageSettings() },
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

@Preview
@Composable
private fun LanguagePagePreview() {
    FindMyIPTheme {
        LanguagePage(
            tag = null,
            onHelp = {},
            onTag = {},
            onBack = {}
        )
    }
}
