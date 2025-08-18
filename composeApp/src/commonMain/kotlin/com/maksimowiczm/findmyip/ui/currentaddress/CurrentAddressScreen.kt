package com.maksimowiczm.findmyip.ui.currentaddress

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.presentation.currentaddress.CurrentAddressUiState
import com.maksimowiczm.findmyip.presentation.currentaddress.IpAddressUiState
import com.maksimowiczm.findmyip.ui.infrastructure.LocalClipboardManager
import com.maksimowiczm.findmyip.ui.shared.FindMyIpTheme
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CurrentAddressScreen(
    uiState: CurrentAddressUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = modifier,
        topBar = { TopBar(isLoading = uiState.isLoading, onRefresh = onRefresh) },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(
                visible = uiState.isError,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                ErrorCard()
            }
            uiState.ip4.address?.let { addr ->
                AddressButton(
                    label = stringResource(Res.string.ipv4),
                    address = addr,
                    onClick = { clipboardManager.copyToClipboard(addr) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            uiState.ip6.address?.let { addr ->
                AddressButton(
                    label = stringResource(Res.string.ipv6),
                    address = addr,
                    onClick = { clipboardManager.copyToClipboard(addr) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(modifier: Modifier = Modifier) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Outlined.ErrorOutline, null)
            Text(
                text = stringResource(Res.string.error_failed_to_fetch_your_address),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AddressButton(
    label: String,
    address: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shapes = ButtonDefaults.shapes(shape = MaterialTheme.shapes.extraLarge),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = label, style = MaterialTheme.typography.titleMedium)
            Text(
                text = address,
                style = MaterialTheme.typography.headlineMediumEmphasized,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopBar(isLoading: Boolean, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    val stateTransition = updateTransition(isLoading)

    val insets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
    val motionScheme = MaterialTheme.motionScheme

    Column(modifier = modifier.windowInsetsPadding(insets).consumeWindowInsets(insets)) {
        stateTransition.AnimatedContent(
            contentKey = { it },
            transitionSpec = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    motionScheme.slowEffectsSpec(),
                ) togetherWith
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        motionScheme.slowEffectsSpec(),
                    )
            },
        ) {
            if (it) {
                LinearWavyProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                Spacer(Modifier.height(10.dp))
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            FilledTonalIconButton(onClick = onRefresh, shapes = IconButtonDefaults.shapes()) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = stringResource(Res.string.action_refresh),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CurrentAddressScreenPreview() {
    val uiState =
        CurrentAddressUiState(
            ip4 = IpAddressUiState.Success("127.0.0.1"),
            ip6 = IpAddressUiState.Success("::1"),
        )

    FindMyIpTheme { CurrentAddressScreen(uiState = uiState, onRefresh = {}) }
}

@Preview
@Composable
private fun CurrentAddressScreenLoadingPreview() {
    val uiState =
        CurrentAddressUiState(
            ip4 = IpAddressUiState.Loading(null),
            ip6 = IpAddressUiState.Loading(null),
        )

    FindMyIpTheme { CurrentAddressScreen(uiState = uiState, onRefresh = {}) }
}

@Preview
@Composable
private fun CurrentAddressScreenErrorPreview() {
    val uiState =
        CurrentAddressUiState(
            ip4 = IpAddressUiState.Error(null),
            ip6 = IpAddressUiState.Error(null),
        )

    FindMyIpTheme { CurrentAddressScreen(uiState = uiState, onRefresh = {}) }
}
