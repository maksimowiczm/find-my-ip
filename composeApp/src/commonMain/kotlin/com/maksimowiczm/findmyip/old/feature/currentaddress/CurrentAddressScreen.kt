package com.maksimowiczm.findmyip.old.feature.currentaddress

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.old.data.model.InternetProtocolVersion
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.tap_to_refresh
import findmyip.composeapp.generated.resources.unavailable
import findmyip.composeapp.generated.resources.your_ip_address_is
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CurrentAddressScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrentAddressViewModel = koinViewModel()
) {
    val ipv4 by viewModel.ipv4.collectAsStateWithLifecycle()
    val ipv6 by viewModel.ipv6.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    CurrentAddressScreen(
        modifier = modifier,
        isLoading = isLoading,
        ipv4 = ipv4,
        ipv6 = ipv6,
        onRefresh = viewModel::refresh
    )
}

@Composable
private fun CurrentAddressScreen(
    isLoading: Boolean,
    ipv4: AddressState,
    ipv6: AddressState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = if (!isLoading) {
            modifier.clickable(onClick = onRefresh)
        } else {
            modifier
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading || ipv4 is AddressState.Loading || ipv6 is AddressState.Loading) {
            CircularProgressIndicator()
            return
        }

        Text(
            text = stringResource(Res.string.your_ip_address_is),
            style = MaterialTheme.typography.titleLarge
        )

        when (ipv4) {
            AddressState.Error -> ErrorAddress(
                modifier = Modifier.padding(top = 8.dp),
                internetProtocolVersion = InternetProtocolVersion.IPv4
            )

            is AddressState.Loaded -> LoadedAddress(
                modifier = Modifier.padding(top = 8.dp),
                internetProtocolVersion = InternetProtocolVersion.IPv4,
                addressState = ipv4
            )

            AddressState.Loading,
            AddressState.Disabled -> Unit
        }
        when (ipv6) {
            AddressState.Error -> ErrorAddress(
                modifier = Modifier.padding(top = 8.dp),
                internetProtocolVersion = InternetProtocolVersion.IPv6
            )

            is AddressState.Loaded -> LoadedAddress(
                modifier = Modifier.padding(top = 8.dp),
                internetProtocolVersion = InternetProtocolVersion.IPv6,
                addressState = ipv6
            )

            AddressState.Loading,
            AddressState.Disabled -> Unit
        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(Res.string.tap_to_refresh),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun LoadedAddress(
    internetProtocolVersion: InternetProtocolVersion,
    addressState: AddressState.Loaded,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = addressState.address,
            style = if (addressState.address.length < 16) {
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.titleSmall
            }
        )
        InternetProtocolVersionLabel(internetProtocolVersion, Modifier.padding(start = 4.dp))
    }
}

@Composable
private fun ErrorAddress(
    internetProtocolVersion: InternetProtocolVersion,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = stringResource(Res.string.unavailable),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
        InternetProtocolVersionLabel(internetProtocolVersion, Modifier.padding(start = 4.dp))
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
