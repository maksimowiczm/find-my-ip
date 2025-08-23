package com.maksimowiczm.findmyip.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.maksimowiczm.findmyip.application.usecase.ObserveAddressHistoryUseCase
import com.maksimowiczm.findmyip.application.usecase.ObserveCurrentIpAddressUseCase
import com.maksimowiczm.findmyip.application.usecase.RefreshAddressUseCase
import com.maksimowiczm.findmyip.domain.entity.Ip4Address
import com.maksimowiczm.findmyip.domain.entity.Ip6Address
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class HomeViewModel(
    observeHistoryUseCase: ObserveAddressHistoryUseCase,
    observeCurrentIp4AddressUseCase: ObserveCurrentIpAddressUseCase<Ip4Address>,
    observeCurrentIp6AddressUseCase: ObserveCurrentIpAddressUseCase<Ip6Address>,
    private val refreshIp4AddressUseCase: RefreshAddressUseCase,
    private val refreshIp6AddressUseCase: RefreshAddressUseCase,
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    val ipv4 =
        observeCurrentIp4AddressUseCase
            .observe()
            .map(CurrentAddressUiModel::from)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(2_000),
                initialValue = CurrentAddressUiModel.Unavailable,
            )

    val ipv6 =
        observeCurrentIp6AddressUseCase
            .observe()
            .map(CurrentAddressUiModel::from)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(2_000),
                initialValue = CurrentAddressUiModel.Unavailable,
            )

    private val _filter = MutableStateFlow(Filter(setOf()))
    val filter = _filter.asStateFlow()

    fun updateFilter(newFilter: Filter) {
        _filter.value = newFilter
    }

    private val _searchQuery =
        MutableSharedFlow<String?>(replay = 1).apply { runBlocking { emit(null) } }
    val searchQuery = _searchQuery.asSharedFlow()

    fun search(query: String?) {
        viewModelScope.launch { _searchQuery.emit(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val history =
        combine(filter, searchQuery) { filter, query ->
                observeHistoryUseCase
                    .observe(
                        query = query,
                        ipv4 =
                            filter.protocols.contains(InternetProtocolVersion.IPV4) ||
                                filter.protocols.isEmpty(),
                        ipv6 =
                            filter.protocols.contains(InternetProtocolVersion.IPV6) ||
                                filter.protocols.isEmpty(),
                    )
                    .map { data -> data.map(::AddressHistoryUiModel) }
            }
            .flatMapLatest { it }
            .cachedIn(viewModelScope)

    init {
        viewModelScope.launch { refresh() }
    }

    fun refresh() {
        if (_isRefreshing.value) return

        _isRefreshing.value = true

        val ip4Job =
            viewModelScope.async {
                delay(REFRESH_DELAY_MS)
                refreshIp4AddressUseCase.refresh()
            }
        val ip6Job =
            viewModelScope.async {
                delay(REFRESH_DELAY_MS)
                refreshIp6AddressUseCase.refresh()
            }

        viewModelScope.launch {
            awaitAll(ip4Job, ip6Job)
            _isRefreshing.value = false
        }
    }

    companion object {
        private const val REFRESH_DELAY_MS = 1000L
    }
}
