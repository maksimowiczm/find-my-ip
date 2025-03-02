package com.maksimowiczm.findmyip.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.data.AddressStatus
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.ui.ext.toDp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.tap_to_refresh
import findmyip.composeapp.generated.resources.unavailable
import findmyip.composeapp.generated.resources.your_ip_address_is
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = koinViewModel()) {
    val ipv4 by viewModel.ipv4Address.collectAsStateWithLifecycle()
    val ipv6 by viewModel.ipv6Address.collectAsStateWithLifecycle()

    HomeScreen(
        ipv4 = ipv4,
        ipv6 = ipv6,
        onRefresh = viewModel::onRefresh,
        modifier = modifier
    )
}

@Composable
private fun HomeScreen(
    ipv4: AddressStatus,
    ipv6: AddressStatus,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View
    )

    val ipv4IsLoading by remember(ipv4) {
        derivedStateOf { ipv4 is AddressStatus.Loading && ipv6 != AddressStatus.Disabled }
    }
    val ipv6IsLoading by remember(ipv6) {
        derivedStateOf { ipv6 is AddressStatus.Loading && ipv4 != AddressStatus.Disabled }
    }

    Surface(
        onClick = onRefresh,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.your_ip_address_is),
                    style = MaterialTheme.typography.titleLarge
                )

                when (ipv4) {
                    AddressStatus.Disabled -> Unit
                    AddressStatus.Loading -> AddressLoadingDisplay(
                        shimmer = shimmer,
                        protocolVersion = InternetProtocolVersion.IPv4,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    is AddressStatus.Success -> AddressDisplay(
                        address = ipv4.address,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    is AddressStatus.Error -> AddressErrorDisplay(
                        modifier = Modifier.padding(top = 8.dp),
                        internetProtocolVersion = InternetProtocolVersion.IPv4
                    )
                }

                when (ipv6) {
                    AddressStatus.Disabled -> Unit
                    AddressStatus.Loading -> AddressLoadingDisplay(
                        shimmer = shimmer,
                        protocolVersion = InternetProtocolVersion.IPv6,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    is AddressStatus.Success -> AddressDisplay(
                        address = ipv6.address,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    is AddressStatus.Error -> AddressErrorDisplay(
                        modifier = Modifier.padding(top = 8.dp),
                        internetProtocolVersion = InternetProtocolVersion.IPv6
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(Res.string.tap_to_refresh),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun AddressContainer(
    protocolVersion: InternetProtocolVersion,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        content()
        Spacer(Modifier.width(8.dp))
        InternetProtocolVersionLabel(
            internetProtocolVersion = protocolVersion
        )
    }
}

@Composable
private fun AddressLoadingDisplay(
    shimmer: Shimmer,
    protocolVersion: InternetProtocolVersion,
    modifier: Modifier = Modifier
) {
    AddressContainer(
        modifier = modifier,
        protocolVersion = protocolVersion
    ) {
        Spacer(
            modifier = Modifier
                .shimmer(shimmer)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .height(MaterialTheme.typography.titleLarge.toDp())
                .width(
                    if (protocolVersion == InternetProtocolVersion.IPv4) 120.dp else 200.dp
                )
        )
    }
}

@Composable
private fun AddressDisplay(address: Address, modifier: Modifier = Modifier) {
    AddressContainer(
        modifier = modifier,
        protocolVersion = address.protocolVersion
    ) {
        Text(
            text = address.ip,
            style = if (address.ip.length < 16) {
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
        modifier = modifier,
        protocolVersion = internetProtocolVersion
    ) {
        Text(
            text = stringResource(Res.string.unavailable),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
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
