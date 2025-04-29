package com.maksimowiczm.findmyip.ui.page.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.infrastructure.android.defaultLocale
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme

@Composable
fun SettingsPage(
    onBackgroundServices: () -> Unit,
    onNotifications: () -> Unit,
    onLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locale = remember(context) {
        context.defaultLocale
    }

    SettingsPage(
        language = locale.displayName,
        onBackgroundServices = onBackgroundServices,
        onNotifications = onNotifications,
        onLanguage = onLanguage,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    language: String,
    onBackgroundServices: () -> Unit,
    onNotifications: () -> Unit,
    onLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.headline_settings),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.headline_background_services)
                        )
                    },
                    modifier = Modifier.clickable { onBackgroundServices() },
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.description_background_services)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Engineering,
                            contentDescription = null
                        )
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.headline_notifications)
                        )
                    },
                    modifier = Modifier.clickable { onNotifications() },
                    supportingContent = {
                        Text(
                            text = stringResource(R.string.description_notifications)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null
                        )
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.headline_language)
                        )
                    },
                    modifier = Modifier.clickable { onLanguage() },
                    supportingContent = {
                        Text(
                            text = language
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsPagePreview() {
    FindMyIPTheme {
        SettingsPage(
            language = "English (United States)",
            onBackgroundServices = {},
            onNotifications = {},
            onLanguage = {}
        )
    }
}
