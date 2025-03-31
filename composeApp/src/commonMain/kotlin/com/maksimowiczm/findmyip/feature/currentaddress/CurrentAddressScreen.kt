package com.maksimowiczm.findmyip.feature.currentaddress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip._2.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.ext.toDp
import com.maksimowiczm.findmyip.infrastructure.di.container
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.action_tap_to_refresh
import findmyip.composeapp.generated.resources.error_unavailable
import findmyip.composeapp.generated.resources.headline_your_ip_address_is
import org.jetbrains.compose.resources.stringResource
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.DefaultLifecycle
import pro.respawn.flowmvi.compose.dsl.subscribe

@Composable
fun CurrentAddressScreen(modifier: Modifier = Modifier) {
    val container: CurrentAddressContainer = container()

    with(container.store) {
        val state by subscribe(DefaultLifecycle)

        CurrentAddressScreenContent(
            state = state,
            modifier = modifier
        )
    }
}

@Composable
private fun IntentReceiver<CurrentAddressIntent>.CurrentAddressScreenContent(
    state: CurrentAddressState,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        onClick = { intent(CurrentAddressIntent.Refresh) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.headline_your_ip_address_is),
                style = MaterialTheme.typography.titleLarge
            )
            Address(
                state = state.ipv4,
                internetProtocolVersion = InternetProtocolVersion.IPv4
            )
            Address(
                state = state.ipv6,
                internetProtocolVersion = InternetProtocolVersion.IPv6
            )
            Text(
                text = stringResource(Res.string.action_tap_to_refresh)
            )
        }
    }
}

@Composable
private fun Address(state: IpAddressState, internetProtocolVersion: InternetProtocolVersion) {
    when (state) {
        is IpAddressState.Loading -> AddressLoadingDisplay(
            internetProtocolVersion = internetProtocolVersion,
            shimmer = rememberShimmer(ShimmerBounds.Window)
        )

        is IpAddressState.Error -> AddressErrorDisplay(
            internetProtocolVersion = internetProtocolVersion
        )

        is IpAddressState.Success -> AddressDisplay(
            internetProtocolVersion = internetProtocolVersion,
            address = state.ip
        )
    }
}

@Composable
private fun AddressLoadingDisplay(
    internetProtocolVersion: InternetProtocolVersion,
    shimmer: Shimmer,
    modifier: Modifier = Modifier
) {
    AddressContainer(
        internetProtocolVersion = internetProtocolVersion,
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .shimmer(shimmer)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .height(MaterialTheme.typography.titleLarge.toDp())
                .width(120.dp)
        )
    }
}

@Composable
private fun AddressDisplay(
    address: String,
    internetProtocolVersion: InternetProtocolVersion,
    modifier: Modifier = Modifier
) {
    AddressContainer(
        internetProtocolVersion = internetProtocolVersion,
        modifier = modifier
    ) {
        Text(
            text = address,
            style = if (address.length < 16) {
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.titleSmall
            }
        )
    }
}

@Composable
private fun AddressErrorDisplay(
    internetProtocolVersion: InternetProtocolVersion,
    modifier: Modifier = Modifier
) {
    AddressContainer(
        internetProtocolVersion = internetProtocolVersion,
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.error_unavailable),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun AddressContainer(
    internetProtocolVersion: InternetProtocolVersion,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        content()
        Spacer(Modifier.width(8.dp))
        InternetProtocolVersionLabel(internetProtocolVersion)
    }
}

@Composable
private fun InternetProtocolVersionLabel(
    internetProtocolVersion: InternetProtocolVersion,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = internetProtocolVersion.name,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.outline
    )
}
