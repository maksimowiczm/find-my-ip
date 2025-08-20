package com.maksimowiczm.findmyip.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ContainedLoadingIndicator
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.maksimowiczm.findmyip.presentation.home.AddressHistoryUiModel
import com.maksimowiczm.findmyip.presentation.home.CurrentAddressUiModel
import com.maksimowiczm.findmyip.presentation.home.Filter
import com.maksimowiczm.findmyip.presentation.home.InternetProtocolVersion
import com.maksimowiczm.findmyip.ui.infrastructure.LocalClipboardManager
import com.maksimowiczm.findmyip.ui.infrastructure.LocalDateFormatter
import findmyip.composeapp.generated.resources.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    FlowPreview::class,
)
@Composable
fun HomeScreen(
    ip4: CurrentAddressUiModel,
    ip6: CurrentAddressUiModel,
    history: LazyPagingItems<AddressHistoryUiModel>,
    filter: Filter,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onSearch: (String) -> Unit,
    onVolunteer: () -> Unit,
    onSettings: () -> Unit,
    onFilterUpdate: (Filter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    val pullState = rememberPullToRefreshState()

    var showFilters by rememberSaveable { mutableStateOf(false) }
    if (showFilters) {
        FiltersModal(
            filter = filter,
            onDismiss = { showFilters = false },
            onUpdateFilter = onFilterUpdate,
        )
    }

    val searchTextState = rememberTextFieldState()
    LaunchedEffect(searchTextState, onSearch) {
        snapshotFlow { searchTextState.text }
            .debounce(100)
            .collectLatest { onSearch(it.toString()) }
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                SearchBar(
                    filtersCount = filter.filtersCount,
                    state = searchTextState,
                    onSearch = onSearch,
                    onFilter = { showFilters = true },
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onSettings) { Icon(Icons.Filled.Settings, null) }
            }
        }

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
            ) {
                if (ip4 is CurrentAddressUiModel.Address || ip6 is CurrentAddressUiModel.Address) {
                    item {
                        Text(
                            text = stringResource(Res.string.headline_current),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                }

                if (ip4 is CurrentAddressUiModel.Address) {
                    item(key = "ip4") {
                        AddressButton(
                            address = ip4.address,
                            protocol = ip4.internetProtocolVersion,
                            dateTime = ip4.dateTime,
                            onClick = { clipboardManager.copyToClipboard(ip4.address) },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (ip6 is CurrentAddressUiModel.Address) {
                    item(key = "ip6") {
                        AddressButton(
                            address = ip6.address,
                            protocol = ip6.internetProtocolVersion,
                            dateTime = ip6.dateTime,
                            onClick = { clipboardManager.copyToClipboard(ip6.address) },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (history.itemCount > 0) {
                    item {
                        Text(
                            text = stringResource(Res.string.headline_history),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                }

                items(count = history.itemCount, key = history.itemKey { it.id }) {
                    val item = history[it] ?: return@items

                    AddressButton(
                        address = item.address,
                        protocol = item.internetProtocolVersion,
                        dateTime = item.dateTime,
                        onClick = { clipboardManager.copyToClipboard(item.address) },
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.animateItem(),
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalAnimationApi::class)
@Composable
private fun AddressButton(
    address: String,
    protocol: InternetProtocolVersion,
    dateTime: LocalDateTime,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    val dateFormatter = LocalDateFormatter.current

    val label =
        when (protocol) {
            InternetProtocolVersion.IPV4 -> stringResource(Res.string.ipv4)
            InternetProtocolVersion.IPV6 -> stringResource(Res.string.ipv6)
        }

    val timeTransition = updateTransition(dateTime)

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
                timeTransition.AnimatedContent(
                    contentKey = { it.toString() },
                    transitionSpec = {
                        fadeIn(tween(durationMillis = 1_000)) togetherWith
                            fadeOut(tween(durationMillis = 200))
                    },
                ) {
                    Text(
                        text = dateFormatter.formatDateTimeLong(dateTime),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
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
private fun MyCustomIndicator(
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
