package com.maksimowiczm.findmyip.ui.page.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.domain.repository.AddressRepository
import com.maksimowiczm.findmyip.ext.launch
import com.maksimowiczm.findmyip.ext.mapValues
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class HistoryPageViewModel(private val addressRepository: AddressRepository) : ViewModel() {
    private val showSearch = MutableStateFlow(false)
    private val searchQuery = MutableStateFlow("")
    private val protocolFilters = MutableStateFlow<List<InternetProtocol>>(emptyList())
    private val networkTypeFilters = MutableStateFlow<List<NetworkType>>(emptyList())
    private val dateRange = MutableStateFlow<DateRange?>(null)

    private val noAddressListState = combine(
        showSearch,
        searchQuery,
        protocolFilters,
        networkTypeFilters,
        dateRange
    ) { showSearch, searchQuery, protocolFilters, networkTypeFilters, dateRange ->
        HistoryPageState(
            showSearch = showSearch,
            searchQuery = searchQuery,
            internetProtocolsFilters = protocolFilters,
            networkTypeFilters = networkTypeFilters,
            dateRange = dateRange,
            addressList = emptyList()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val addressList = noAddressListState.flatMapLatest {
        addressRepository.observeAddresses(
            query = it.searchQuery,
            internetProtocolFilters = it.internetProtocolsFilters,
            networkTypeFilters = it.networkTypeFilters,
            startDate = it.dateRange?.start,
            endDate = it.dateRange?.end
        )
    }.mapValues {
        Address.fromDomain(it)
    }

    val state = combine(noAddressListState, addressList) { state, list ->
        state.copy(addressList = list)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = HistoryPageState()
    )

    fun onIntent(intent: HistoryPageIntent) = launch {
        when (intent) {
            is HistoryPageIntent.DeleteAddress -> onDeleteAddress(intent.address)

            HistoryPageIntent.ClearDateFilter -> dateRange.emit(null)
            is HistoryPageIntent.FilterByDate -> dateRange.emit(
                DateRange(
                    start = intent.start,
                    end = intent.end
                )
            )

            is HistoryPageIntent.FilterByNetworkType -> networkTypeFilters.emit(
                intent.networkTypes
            )

            is HistoryPageIntent.FilterByProtocols -> protocolFilters.emit(intent.protocols)

            HistoryPageIntent.HideSearch -> showSearch.emit(false)
            HistoryPageIntent.ShowSearch -> showSearch.emit(true)

            is HistoryPageIntent.Search -> searchQuery.emit(intent.query)
            HistoryPageIntent.ClearAll -> {
                searchQuery.emit("")
                protocolFilters.emit(emptyList())
                networkTypeFilters.emit(emptyList())
                dateRange.emit(null)
            }
        }
    }

    private fun onDeleteAddress(address: Address) = launch {
        addressRepository.deleteAddress(address.id)
    }
}
