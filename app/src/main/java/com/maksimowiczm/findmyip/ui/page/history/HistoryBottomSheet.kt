package com.maksimowiczm.findmyip.ui.page.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.ui.theme.FindMyIPTheme
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryBottomSheet(
    state: SheetState,
    onDismissRequest: () -> Unit,
    address: Address,
    onCopy: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = onDismissRequest
    ) {
        HistoryBottomSheetContent(
            address = address,
            onCopy = onCopy,
            onDelete = onDelete,
            modifier = modifier
        )
    }
}

@Composable
private fun HistoryBottomSheetContent(
    address: Address,
    onCopy: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AddressListItem(
            address = address,
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.action_copy_ip)
                )
            },
            modifier = Modifier.clickable { onCopy() },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.action_delete_entry)
                )
            },
            modifier = Modifier.clickable { onDelete() },
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
                headlineColor = MaterialTheme.colorScheme.error,
                leadingIconColor = MaterialTheme.colorScheme.error
            )
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun HistoryBottomSheetPreview() {
    FindMyIPTheme {
        HistoryBottomSheetContent(
            address = Address(
                ip = "192.168.1.1",
                dateTime = LocalDateTime(2025, 4, 28, 20, 54, 0),
                networkType = NetworkType.WiFi
            ),
            onCopy = {},
            onDelete = {}
        )
    }
}
