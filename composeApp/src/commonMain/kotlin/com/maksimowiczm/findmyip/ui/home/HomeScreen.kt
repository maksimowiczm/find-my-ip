package com.maksimowiczm.findmyip.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.maksimowiczm.findmyip.presentation.home.AddressHistoryUiModel
import com.maksimowiczm.findmyip.presentation.home.ProtocolVersion
import com.maksimowiczm.findmyip.ui.infrastructure.LocalClipboardManager
import com.maksimowiczm.findmyip.ui.infrastructure.LocalDateFormatter
import findmyip.composeapp.generated.resources.*
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    history: LazyPagingItems<AddressHistoryUiModel>,
    isRefreshing: Boolean,
    isError: Boolean,
    onRefresh: () -> Unit,
    onSearch: (String) -> Unit,
    onVolunteer: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    val pullState = rememberPullToRefreshState()

    val searchBarState = rememberSearchBarState()
    val searchTextState = rememberTextFieldState()
    val searchInputField =
        @Composable {
            SearchBarDefaults.InputField(
                textFieldState = searchTextState,
                searchBarState = searchBarState,
                onSearch = onSearch,
                placeholder = { Text(stringResource(Res.string.action_search)) },
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                // trailingIcon = { IconButton(onClick = {}) { Icon(Icons.Outlined.FilterAlt, null)
                // } },
            )
        }
    val topBar =
        @Composable {
            Row(
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .windowInsetsPadding(SearchBarDefaults.windowInsets)
                        .consumeWindowInsets(SearchBarDefaults.windowInsets),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onVolunteer,
                    colors =
                        IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        ),
                ) {
                    Icon(Icons.Outlined.VolunteerActivism, null)
                }
                TopSearchBar(
                    state = searchBarState,
                    inputField = searchInputField,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onSettings) { Icon(Icons.Filled.Settings, null) }
            }
        }

    ExpandedFullScreenSearchBar(state = searchBarState, inputField = searchInputField) {}

    Scaffold(modifier = modifier, topBar = topBar) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullState,
            indicator = {
                MyCustomIndicator(
                    state = pullState,
                    isRefreshing = isRefreshing,
                    contentPadding = PaddingValues(top = paddingValues.calculateTopPadding()),
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = paddingValues.add(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        AnimatedVisibility(
                            visible = isError,
                            enter = expandVertically(),
                            exit = shrinkVertically(),
                        ) {
                            ErrorCard()
                        }
                    }
                }

                items(count = history.itemCount, key = history.itemKey { it.id }) {
                    val item = history[it] ?: return@items

                    AddressButton(
                        address = item.address,
                        protocol = item.protocolVersion,
                        dateTime = item.dateTime,
                        onClick = { clipboardManager.copyToClipboard(item.address) },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
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
    address: String,
    protocol: ProtocolVersion,
    dateTime: LocalDateTime,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = LocalDateFormatter.current

    val label =
        when (protocol) {
            ProtocolVersion.IPV4 -> stringResource(Res.string.ipv4)
            ProtocolVersion.IPV6 -> stringResource(Res.string.ipv6)
        }

    Button(
        onClick = onClick,
        modifier = modifier,
        shapes = ButtonDefaults.shapes(shape = MaterialTheme.shapes.extraLarge),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = label, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = dateFormatter.formatDateTimeLong(dateTime),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Text(
                text = address,
                style = MaterialTheme.typography.headlineMediumEmphasized,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyCustomIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val topPadding = contentPadding.calculateTopPadding()

    Box(
        modifier =
            modifier.graphicsLayer {
                alpha = if (state.distanceFraction == 0f) 0f else 1f
                translationY =
                    topPadding.roundToPx() - size.height + state.distanceFraction * size.height
            },
        contentAlignment = Alignment.Center,
    ) {
        if (isRefreshing) {
            ContainedLoadingIndicator(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                indicatorColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        } else {
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                        modifier =
                            Modifier.size(32.dp).graphicsLayer {
                                this.rotationZ = state.distanceFraction * 90f
                            },
                    )
                }
            }
        }
    }
}
