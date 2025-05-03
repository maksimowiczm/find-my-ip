package com.maksimowiczm.findmyip.ui.page.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.R
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.ui.res.stringResource
import com.maksimowiczm.findmyip.ui.utils.LocalClipboardManager
import com.maksimowiczm.findmyip.ui.utils.LocalDateFormatter
import kotlin.String
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryPage(modifier: Modifier = Modifier, viewModel: HistoryPageViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryPage(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryPage(
    state: HistoryPageState,
    onIntent: (HistoryPageIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    val layoutDirection = LocalLayoutDirection.current

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    // Copy paste from TopAppBar
    val topAppBarColors = TopAppBarDefaults.topAppBarColors()
    val colorTransitionFraction by remember(scrollBehavior) {
        derivedStateOf {
            val overlappingFraction = scrollBehavior.state.overlappedFraction
            if (overlappingFraction > 0.01f) 1f else 0f
        }
    }
    val appBarContainerColor by animateColorAsState(
        targetValue = lerp(
            topAppBarColors.containerColor,
            topAppBarColors.scrolledContainerColor,
            FastOutLinearInEasing.transform(colorTransitionFraction)
        ),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
    val selectedAddress by remember(selectedIndex) {
        derivedStateOf { state.addressList.getOrNull(selectedIndex) }
    }
    val sheetState = rememberModalBottomSheetState()

    when (val address = selectedAddress) {
        null -> Unit
        else -> {
            var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

            if (showDeleteDialog) {
                DeleteDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    onDelete = {
                        showDeleteDialog = false
                        coroutineScope.launch {
                            sheetState.hide()
                            selectedIndex = -1
                            onIntent(HistoryPageIntent.DeleteAddress(address))
                        }
                    }
                )
            }

            HistoryBottomSheet(
                state = sheetState,
                onDismissRequest = { selectedIndex = -1 },
                address = address,
                onCopy = {
                    clipboard.copyText(
                        label = address.ip,
                        text = address.ip
                    )
                    coroutineScope.launch {
                        sheetState.hide()
                        selectedIndex = -1
                    }
                },
                onDelete = { showDeleteDialog = true }
            )
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.headline_history),
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                actions = {
                    if (state.showSearch) {
                        HideSearchBarIconButton(
                            onClick = {
                                onIntent(HistoryPageIntent.HideSearch)
                            },
                            modifier = Modifier.testTag(
                                HistoryPageTestTag.HIDE_SEARCHBAR_ICON_BUTTON
                            )
                        )
                    } else {
                        ShowSearchBarIconButton(
                            onClick = {
                                onIntent(HistoryPageIntent.ShowSearch)
                            },
                            modifier = Modifier.testTag(
                                HistoryPageTestTag.SHOW_SEARCHBAR_ICON_BUTTON
                            )
                        )
                    }
                },
                colors = topAppBarColors,
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        val outerPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(layoutDirection),
            end = paddingValues.calculateEndPadding(layoutDirection)
        )

        Column(
            modifier = Modifier
                .padding(outerPadding)
                .consumeWindowInsets(outerPadding)
        ) {
            AnimatedVisibility(
                visible = state.showSearch,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .drawBehind { drawRect(appBarContainerColor) }
                        .padding(top = 8.dp)
                ) {
                    SearchBar(
                        query = state.searchQuery,
                        onSearchQueryChange = {
                            onIntent(HistoryPageIntent.Search(it))
                        },
                        onClearSearch = {
                            onIntent(HistoryPageIntent.Search(""))
                        },
                        onSearch = {
                            onIntent(HistoryPageIntent.Search(it))
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            InternetProtocolFilterChip(
                                filters = state.internetProtocolsFilters,
                                onIntent = onIntent
                            )
                        }

                        item {
                            NetworkTypeFilterChip(
                                filters = state.networkTypeFilters,
                                onIntent = onIntent
                            )
                        }

                        item {
                            DateFilterChip(
                                dateRange = state.dateRange,
                                onIntent = onIntent
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .testTag(HistoryPageTestTag.ADDRESS_LIST)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(
                    bottom = paddingValues.calculateBottomPadding()
                )
            ) {
                itemsIndexed(
                    items = state.addressList,
                    key = { i, addr -> "$i-${addr.ip}" }
                ) { i, address ->
                    AddressListItem(
                        address = address,
                        onClick = { selectedIndex = i }
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowSearchBarIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier.testTag(HistoryPageTestTag.SHOW_SEARCHBAR_ICON_BUTTON)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.action_show_search_bar)
        )
    }
}

@Composable
private fun HideSearchBarIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier.testTag(HistoryPageTestTag.HIDE_SEARCHBAR_ICON_BUTTON)
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = stringResource(R.string.action_hide_search_bar)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = CircleShape
    ) {
        SearchBarDefaults.InputField(
            query = query,
            onQueryChange = onSearchQueryChange,
            onSearch = onSearch,
            expanded = false,
            onExpandedChange = {},
            modifier = Modifier
                .testTag(HistoryPageTestTag.SEARCHBAR)
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = onClearSearch
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.action_clear_input)
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InternetProtocolFilterChip(
    filters: List<InternetProtocol>,
    onIntent: (HistoryPageIntent.FilterByProtocols) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstFilter = filters.firstOrNull()

    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState()
        val filtersState = rememberInternetProtocolsFiltersState(filters)

        InternetProtocolsFiltersBottomSheet(
            state = filtersState,
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            },
            onConfirm = {
                coroutineScope.launch {
                    onIntent(HistoryPageIntent.FilterByProtocols(filtersState.filters))
                    sheetState.hide()
                    showBottomSheet = false
                }
            }
        )
    }

    FilterChip(
        selected = firstFilter != null,
        onClick = { showBottomSheet = true },
        label = {
            if (firstFilter == null) {
                Text(stringResource(R.string.internet_protocol))
            } else {
                val filterStr = firstFilter.stringResource()
                val str = buildString {
                    append(filterStr)
                    if (filters.size > 1) {
                        append("+${filters.size - 1}")
                    }
                }
                Text(str)
            }
        },
        modifier = modifier.testTag(HistoryPageTestTag.INTERNET_PROTOCOL_FILTER_CHIP),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.action_show_filters),
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NetworkTypeFilterChip(
    filters: List<NetworkType>,
    onIntent: (HistoryPageIntent.FilterByNetworkType) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstFilter = filters.firstOrNull()

    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState()
        val filtersState = rememberNetworkTypeFiltersState(filters)

        NetworkTypeFiltersBottomSheet(
            state = filtersState,
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            },
            onConfirm = {
                coroutineScope.launch {
                    onIntent(HistoryPageIntent.FilterByNetworkType(filtersState.filters))
                    sheetState.hide()
                    showBottomSheet = false
                }
            }
        )
    }

    FilterChip(
        selected = firstFilter != null,
        onClick = { showBottomSheet = true },
        label = {
            if (firstFilter == null) {
                Text(stringResource(R.string.network_type))
            } else {
                val filterStr = firstFilter.stringResource()
                val str = buildString {
                    append(filterStr)
                    if (filters.size > 1) {
                        append("+${filters.size - 1}")
                    }
                }
                Text(str)
            }
        },
        modifier = modifier.testTag(HistoryPageTestTag.NETWORK_TYPE_FILTER_CHIP),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.action_show_filters),
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateFilterChip(
    dateRange: DateRange?,
    onIntent: (HistoryPageIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = LocalDateFormatter.current

    val isSelected = dateRange != null

    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    if (showDatePicker) {
        val state = rememberDateRangePickerState()

        val onConfirm = {
            val timezone = TimeZone.currentSystemDefault()
            val start = state.selectedStartDateMillis?.let {
                Instant.fromEpochMilliseconds(it).toLocalDateTime(timezone)
            }
            val end = state.selectedEndDateMillis?.let {
                Instant.fromEpochMilliseconds(it).toLocalDateTime(timezone)
            }

            if (start != null && end != null) {
                onIntent(
                    HistoryPageIntent.FilterByDate(
                        start = start.date,
                        end = end.date
                    )
                )

                showDatePicker = false
            }
        }

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
                    enabled =
                    state.selectedStartDateMillis != null && state.selectedEndDateMillis != null
                ) {
                    Text(stringResource(R.string.action_ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onIntent(HistoryPageIntent.ClearDateFilter)
                        showDatePicker = false
                    }
                ) {
                    Text(stringResource(R.string.action_clear))
                }
            }
        ) {
            DateRangePicker(
                state = state
            )
        }
    }

    FilterChip(
        selected = isSelected,
        onClick = { showDatePicker = true },
        label = {
            if (!isSelected) {
                Text(stringResource(R.string.date))
            } else {
                val enDash = stringResource(R.string.en_dash)
                val str = remember(dateFormatter, dateRange) {
                    buildString {
                        append(dateFormatter.formatDateShort(dateRange.start))
                        append(" $enDash ")
                        append(dateFormatter.formatDateShort(dateRange.end))
                    }
                }

                Text(str)
            }
        },
        modifier = modifier.testTag(HistoryPageTestTag.DATE_FILTER_CHIP),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.action_show_filters),
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        }
    )
}
