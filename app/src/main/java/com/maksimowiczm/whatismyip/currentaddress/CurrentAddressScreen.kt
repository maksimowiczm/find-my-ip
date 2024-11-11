package com.maksimowiczm.whatismyip.currentaddress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.whatismyip.R
import com.maksimowiczm.whatismyip.ui.theme.WhatsMyIpAppTheme

@Composable
fun CurrentAddressScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: CurrentAddressViewModel = hiltViewModel()
) {
    val currentAddressUiState = viewModel.currentAddressUiState.collectAsStateWithLifecycle()
    val uiState = currentAddressUiState.value

    when (uiState) {
        CurrentAddressUiState.Loading -> LoadingCurrentAddress(modifier = modifier)

        is CurrentAddressUiState.Success ->
            CurrentAddress(
                modifier = modifier,
                address = uiState.address,
                onRefresh = viewModel::refreshCurrentAddress
            )

        CurrentAddressUiState.Error ->
            ErrorCurrentAddress(
                modifier = modifier,
                onRefresh = viewModel::refreshCurrentAddress
            )
    }
}

@Composable
private fun CurrentAddress(address: String, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable { onRefresh() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.your_ip_address_is),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = address,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.tap_to_refresh),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun LoadingCurrentAddress(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "refreshTransition")

    val rotation by infiniteTransition.animateFloat(
        label = "refreshAnimation",
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
        infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        )
    )

    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier =
            Modifier
                .padding(bottom = 16.dp)
                .size(48.dp)
                .rotate(rotation),
            imageVector = Icons.Default.Refresh,
            contentDescription = stringResource(R.string.refreshing)
        )

        Text(
            text = stringResource(R.string.fetching_your_ip_address),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ErrorCurrentAddress(modifier: Modifier = Modifier, onRefresh: () -> Unit) {
    Column(
        modifier = modifier.clickable { onRefresh() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = stringResource(R.string.failed_to_fetch_your_ip_address),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.tap_to_refresh),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@PreviewLightDark
@Composable
private fun CurrentAddressPreview() {
    WhatsMyIpAppTheme {
        Surface {
            CurrentAddress(
                modifier = Modifier.fillMaxSize(),
                address = "127.0.0.1",
                onRefresh = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun LoadingCurrentAddressPreview() {
    WhatsMyIpAppTheme {
        Surface {
            LoadingCurrentAddress(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ErrorCurrentAddressPreview() {
    WhatsMyIpAppTheme {
        Surface {
            ErrorCurrentAddress(
                modifier = Modifier.fillMaxSize(),
                onRefresh = {}
            )
        }
    }
}
