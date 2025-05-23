package com.maksimowiczm.findmyip.ui.page.settings.about

import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme
import com.maksimowiczm.findmyip.ui.utils.LocalClipboardManager
import java.util.Collections

@Composable
fun AboutPage(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val version = remember { BuildConfig.VERSION_NAME }
    val versionLabel = stringResource(R.string.headline_version)

    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    val isCustomTabsSupported = remember(context) {
        CustomTabsClient.getPackageName(context, Collections.emptyList()) != null
    }

    val sourceCodeUrl = stringResource(R.string.link_source_code)
    val onSourceCode = remember(isCustomTabsSupported, sourceCodeUrl, uriHandler) {
        if (isCustomTabsSupported) {
            { CustomTabsIntent.Builder().build().launchUrl(context, sourceCodeUrl.toUri()) }
        } else {
            { uriHandler.openUri(sourceCodeUrl) }
        }
    }

    val privacyPolicyUrl = stringResource(R.string.link_privacy_policy)
    val onPrivacyPolicy = remember(isCustomTabsSupported, privacyPolicyUrl, uriHandler) {
        if (isCustomTabsSupported) {
            { CustomTabsIntent.Builder().build().launchUrl(context, privacyPolicyUrl.toUri()) }
        } else {
            { uriHandler.openUri(privacyPolicyUrl) }
        }
    }

    AboutPage(
        onBack = onBack,
        onSourceCode = onSourceCode,
        onPrivacyPolicy = onPrivacyPolicy,
        version = version,
        onVersion = { clipboardManager.copyText(versionLabel, version) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutPage(
    onBack: () -> Unit,
    onSourceCode: () -> Unit,
    onPrivacyPolicy: () -> Unit,
    version: String,
    onVersion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.headline_about)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
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
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues
        ) {
            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(R.string.headline_source_code))
                    },
                    modifier = Modifier.clickable { onSourceCode() },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null
                        )
                    },
                    supportingContent = {
                        Text(stringResource(R.string.description_source_code))
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(R.string.headline_privacy_policy))
                    },
                    modifier = Modifier.clickable { onPrivacyPolicy() },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.PrivacyTip,
                            contentDescription = null
                        )
                    },
                    supportingContent = {
                        Text(stringResource(R.string.description_privacy_policy))
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(R.string.headline_version))
                    },
                    modifier = Modifier.clickable { onVersion() },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null
                        )
                    },
                    supportingContent = {
                        Text(version)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AboutPagePreview() {
    FindMyIPTheme {
        AboutPage(
            onBack = {},
            onSourceCode = {},
            onPrivacyPolicy = {},
            version = BuildConfig.VERSION_NAME,
            onVersion = {}
        )
    }
}
