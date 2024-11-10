package com.maksimowiczm.whatismyip.current_address

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.maksimowiczm.whatismyip.ui.theme.RememberIPTheme

@Composable
fun CurrentAddressScreen(
    currentAddressViewModel: CurrentAddressViewModel,
) {
    val currentAddressUiState by currentAddressViewModel.currentAddressUiState.collectAsStateWithLifecycle()
    val uiState = currentAddressUiState

    when (uiState) {
        CurrentAddressUiState.Loading -> LoadingCurrentAddress()

        is CurrentAddressUiState.Success -> CurrentAddress(
            address = uiState.address,
            onRefresh = currentAddressViewModel::refreshCurrentAddress
        )

        CurrentAddressUiState.Error -> ErrorCurrentAddress(
            onRefresh = currentAddressViewModel::refreshCurrentAddress
        )
    }
}

@Composable
private fun CurrentAddress(
    address: String,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onRefresh() },
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = "Your IP address is",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = address,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = "Tap to refresh",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun LoadingCurrentAddress() {
    val infiniteTransition = rememberInfiniteTransition(label = "refresh")

    val rotation by infiniteTransition.animateFloat(
        label = "refresh",
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(48.dp)
                .rotate(rotation),
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refreshing",
        )

        Text(
            text = "Fetching your IP address",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
private fun ErrorCurrentAddress(
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onRefresh() },
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = "Failed to fetch your IP address",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = "Tap to refresh",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@PreviewLightDark
@Composable
private fun CurrentAddressPreview() {
    RememberIPTheme {
        Surface {
            CurrentAddress(
                address = "127.0.0.1",
                onRefresh = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun LoadingCurrentAddressPreview() {
    RememberIPTheme {
        Surface {
            LoadingCurrentAddress()
        }
    }
}

@PreviewLightDark
@Composable
private fun ErrorCurrentAddressPreview() {
    RememberIPTheme {
        Surface {
            ErrorCurrentAddress(
                onRefresh = {}
            )
        }
    }
}
