package com.maksimowiczm.findmyip.addresshistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.AddressHistory
import com.maksimowiczm.findmyip.ui.theme.FindMyIpAppTheme

@Composable
internal fun AddressHistoryList(items: List<AddressHistory>, modifier: Modifier = Modifier) {
    if (items.isEmpty()) {
        AddressHistoryEmptyList(modifier)
    } else {
        LazyColumn(modifier) {
            items(
                items = items.mapIndexed { index, item -> item to index },
                key = { (_, index) -> index }
            ) { (it, index) ->
                AddressHistoryListItem(address = it)
                if (index < items.size - 1) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun AddressHistoryListItem(address: AddressHistory, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text(text = address.ip) },
        supportingContent = { Text(text = address.date) }
    )
}

@Composable
private fun AddressHistoryEmptyList(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = stringResource(R.string.empty)
        )
    }
}

@PreviewLightDark
@Composable
private fun AddressHistoryListPreview() {
    FindMyIpAppTheme {
        Surface {
            AddressHistoryList(
                items = listOf(
                    AddressHistory(
                        ip = "127.0.0.1",
                        date = "January 1, 2024"
                    ),
                    AddressHistory(
                        ip = "192.168.0.1",
                        date = "January 1, 2024"
                    ),
                    AddressHistory(
                        ip = "10.0.0.1",
                        date = "January 1, 2024"
                    ),
                    AddressHistory(
                        ip = "::1",
                        date = "January 2, 2024"
                    )
                ),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AddressHistoryEmptyListPreview() {
    FindMyIpAppTheme {
        Surface {
            AddressHistoryEmptyList(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
