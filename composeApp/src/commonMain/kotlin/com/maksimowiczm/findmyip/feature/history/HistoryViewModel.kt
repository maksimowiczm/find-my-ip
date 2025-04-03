package com.maksimowiczm.findmyip.feature.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.data.StringFormatRepository
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.domain.ObserveHistoryUseCase
import com.maksimowiczm.findmyip.domain.ShouldShowHistoryUseCase
import com.maksimowiczm.findmyip.infrastructure.di.observe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class HistoryViewModel(
    private val shouldShowHistoryUseCase: ShouldShowHistoryUseCase,
    private val observeHistoryUseCase: ObserveHistoryUseCase,
    private val stringFormatRepository: StringFormatRepository,
    dataStore: DataStore<Preferences>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val filter = savedStateHandle.getMutableStateFlow("filter", Filter.All)

    fun setFilter(filter: Filter) {
        this.filter.value = filter
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val history: Flow<PagingData<HistoryItem>> = filter.flatMapLatest { filter ->
        observeHistoryUseCase.observeHistory(filter.protocol).map { data ->
            data.map {
                val date = stringFormatRepository.formatDateTime(it.date)
                HistoryItem(
                    id = it.id,
                    ip = it.ip,
                    date = date,
                    protocol = it.protocol
                )
            }
        }
    }

    private val _historyEnabled =
        dataStore.observe(PreferenceKeys.historyEnabled).map { it ?: false }
    val historyEnabled = _historyEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = runBlocking { _historyEnabled.first() }
    )

    private val _ipv4Enabled = dataStore.observe(PreferenceKeys.ipv4Enabled).map { it ?: false }
    private val _ipv6Enabled = dataStore.observe(PreferenceKeys.ipv6Enabled).map { it ?: false }
    val ipv4Enabled = _ipv4Enabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = runBlocking { _ipv4Enabled.first() }
    )
    val ipv6Enabled = _ipv6Enabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = runBlocking { _ipv6Enabled.first() }
    )

    val showIpv4 = shouldShowHistoryUseCase
        .shouldShowHistory(InternetProtocolVersion.IPv4)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = runBlocking {
                shouldShowHistoryUseCase.shouldShowHistory(InternetProtocolVersion.IPv4).first()
            }
        )

    val showIpv6 = shouldShowHistoryUseCase
        .shouldShowHistory(InternetProtocolVersion.IPv6)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = runBlocking {
                shouldShowHistoryUseCase.shouldShowHistory(InternetProtocolVersion.IPv6).first()
            }
        )
}
