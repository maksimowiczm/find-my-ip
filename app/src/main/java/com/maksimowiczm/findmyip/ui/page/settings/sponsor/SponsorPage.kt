package com.maksimowiczm.findmyip.ui.page.settings.sponsor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Hail
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.BuildConfig
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.ext.add
import com.maksimowiczm.findmyip.ui.utils.LocalClipboardManager

@Composable
fun SponsorPage(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current

    SponsorPage(
        onBack = onBack,
        onCopy = { clipboardManager.copyText("address", it) },
        onContact = { uriHandler.openUri(BuildConfig.FEEDBACK_EMAIL_URI) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SponsorPage(
    onBack: () -> Unit,
    onCopy: (String) -> Unit,
    onContact: () -> Unit,
    modifier: Modifier = Modifier,
    methods: List<CryptoSponsorMethod> = CryptoSponsorMethod.all
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(R.string.headline_sponsor))
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
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
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = paddingValues.add(vertical = 8.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.description_sponsor_2),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            items(
                items = methods
            ) { method ->
                method.SponsorCard(
                    onClick = { onCopy(method.address) }
                )
            }

            item {
                ContactCard(
                    onContact = onContact
                )
            }
        }
    }
}

@Composable
private fun CryptoSponsorMethod.SponsorCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = name.uppercase(),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.labelLarge
        )
        SponsorCard(
            label = address,
            leadingIcon = { Icon(Modifier.height(24.dp)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = null
                )
            },
            onClick = onClick
        )
    }
}

@Composable
private fun SponsorCard(
    label: String,
    leadingIcon: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon()
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            trailingIcon()
        }
    }
}

@Composable
private fun ContactCard(onContact: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onContact,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Hail,
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.headline_contact),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = stringResource(R.string.description_sponsor_contact),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
private fun SponsorPagePreview() {
    SponsorPage(
        onBack = {},
        onCopy = {},
        onContact = {}
    )
}
