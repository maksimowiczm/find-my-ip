package com.maksimowiczm.findmyip.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maksimowiczm.findmyip.presentation.home.Filter
import com.maksimowiczm.findmyip.presentation.home.InternetProtocolVersion
import com.maksimowiczm.findmyip.ui.shared.FindMyIpTheme
import findmyip.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersModal(
    filter: Filter,
    onDismiss: () -> Unit,
    onUpdateFilter: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        content = { FiltersModalContent(filter, onUpdateFilter) },
    )
}

@Composable
private fun FiltersModalContent(
    filter: Filter,
    onUpdateFilter: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(Res.string.headline_filters),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = stringResource(Res.string.headline_internet_protocol),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                FilterChip(
                    selected = filter.protocols.contains(InternetProtocolVersion.IPV4),
                    onClick = {
                        onUpdateFilter(filter.toggleInternetProtocol(InternetProtocolVersion.IPV4))
                    },
                    label = { Text(stringResource(Res.string.ipv4)) },
                )
            }
            item {
                FilterChip(
                    selected = filter.protocols.contains(InternetProtocolVersion.IPV6),
                    onClick = {
                        onUpdateFilter(filter.toggleInternetProtocol(InternetProtocolVersion.IPV6))
                    },
                    label = { Text(stringResource(Res.string.ipv6)) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun FiltersModalContentPreview() {
    FindMyIpTheme { Surface { FiltersModalContent(filter = Filter(setOf()), onUpdateFilter = {}) } }
}
