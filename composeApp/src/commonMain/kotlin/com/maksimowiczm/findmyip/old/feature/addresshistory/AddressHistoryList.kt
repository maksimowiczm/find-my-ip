package com.maksimowiczm.findmyip.old.feature.addresshistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.maksimowiczm.findmyip.old.domain.AddressHistory
import findmyip.composeapp.generated.resources.Res
import findmyip.composeapp.generated.resources.empty
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddressHistoryList(items: List<AddressHistory>, modifier: Modifier = Modifier) {
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
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
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
            text = stringResource(Res.string.empty)
        )
    }
}
