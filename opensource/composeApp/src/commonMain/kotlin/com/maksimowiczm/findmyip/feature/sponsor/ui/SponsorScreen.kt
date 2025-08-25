package com.maksimowiczm.findmyip.feature.sponsor.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.domain.SponsorshipMethod
import com.maksimowiczm.findmyip.shared.core.feature.ui.ArrowBackIconButton
import com.maksimowiczm.findmyip.shared.core.feature.ui.FindMyIpTheme
import com.maksimowiczm.findmyip.shared.core.feature.ui.LocalClipboardManager
import findmyip.composeapp.generated.resources.*
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun SponsorScreen(
    onBack: () -> Unit,
    methods: List<SponsorshipMethod>,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(stringResource(Res.string.headline_become_a_sponsor)) },
                navigationIcon = { ArrowBackIconButton(onBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues.add(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text(
                    text = stringResource(Res.string.description_become_a_sponsor),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            items(methods) { method -> method.Card() }
        }
    }
}

@Composable
private fun SponsorshipMethod.Card(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = name, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(2.dp))
        Button()
    }
}

@Composable
private fun SponsorshipMethod.Button(modifier: Modifier = Modifier) {
    when (this) {
        is SponsorshipMethod.Url -> this.Button(modifier)
        is SponsorshipMethod.Crypto -> this.Button(modifier)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SponsorshipMethod.Url.Button(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    val containerColor =
        if (primary) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer
    val contentColor =
        if (primary) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurface

    Button(
        onClick = { uriHandler.openUri(url) },
        shapes = ButtonDefaults.shapes(),
        modifier = modifier.heightIn(min = 64.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        colors = ButtonDefaults.buttonColors(containerColor, contentColor),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(Modifier.height(48.dp), Alignment.Center) {
                Image(
                    painter = painterResource(icon()),
                    contentDescription = null,
                    modifier = Modifier.sizeIn(maxWidth = 24.dp, maxHeight = 24.dp),
                )
            }
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            Box(Modifier.height(48.dp), Alignment.Center) {
                Icon(imageVector = Icons.Outlined.Link, contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SponsorshipMethod.Crypto.Button(modifier: Modifier = Modifier) {
    val clipboard = LocalClipboardManager.current
    val label = stringResource(Res.string.clipboard_label_cryptocurrency_address)

    val containerColor =
        if (primary) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer
    val contentColor =
        if (primary) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurface

    var copied by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(copied) {
        if (copied) {
            delay(2.seconds)
            copied = false
        }
    }

    Button(
        onClick = {
            clipboard.copyToClipboard(address, label)
            copied = true
        },
        shapes = ButtonDefaults.shapes(MaterialTheme.shapes.extraExtraLarge),
        modifier = modifier.heightIn(min = 64.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor, contentColor),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(Modifier.height(48.dp), Alignment.Center) {
                Image(
                    painter = painterResource(icon()),
                    contentDescription = null,
                    modifier = Modifier.sizeIn(maxWidth = 24.dp, maxHeight = 24.dp),
                )
            }
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            Box(Modifier.height(48.dp), Alignment.Center) {
                if (copied) {
                    Icon(Icons.Outlined.Check, null, tint = MaterialTheme.colorScheme.primary)
                } else {
                    Icon(Icons.Outlined.ContentCopy, null)
                }
            }
        }
    }
}

@Composable
private fun SponsorshipMethod.icon(): DrawableResource =
    when (this.name) {
        "Ko-fi" -> Res.drawable.kofi_logo
        "Bitcoin" -> Res.drawable.bitcoin_logo
        "Monero" -> Res.drawable.monero_logo
        "Ethereum" -> Res.drawable.eth_diamond_purple_purple
        "Solana" -> Res.drawable.solana_logomark
        "Litecoin" -> Res.drawable.litecoin_ltc_logo
        "Zcash" -> Res.drawable.zcash_icon
        "Dash" -> Res.drawable.dash_coin
        "Avalanche" -> Res.drawable.avalanche_token
        else -> error("Unknown sponsorship method: ${this.name}")
    }

@Preview
@Composable
private fun SponsorScreenPreview() {
    FindMyIpTheme { SponsorScreen(methods = SponsorshipMethod.methods, onBack = {}) }
}
