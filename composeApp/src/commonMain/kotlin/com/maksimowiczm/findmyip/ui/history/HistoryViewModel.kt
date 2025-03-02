package com.maksimowiczm.findmyip.ui.history

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.data.StringFormatRepository
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime

class HistoryViewModel(
    private val dataStore: DataStore<Preferences>,
    addressRepository: AddressRepository,
    private val stringFormatRepository: StringFormatRepository
) : ViewModel() {
    val enabled = dataStore
        .observe(PreferenceKeys.historyEnabled)
        .map { it ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = runBlocking { dataStore.get(PreferenceKeys.historyEnabled) ?: false }
        )

    val ipv4data = addressRepository
        .observeAddressesPaged(InternetProtocolVersion.IPv4)
        .cachedIn(viewModelScope)

    val ipv6data = addressRepository
        .observeAddressesPaged(InternetProtocolVersion.IPv6)
        .cachedIn(viewModelScope)

    fun onPermissionGranted() {
        viewModelScope.launch {
            dataStore.set(PreferenceKeys.historyEnabled to true)
        }
    }

    fun formatDate(date: LocalDateTime): String = stringFormatRepository.formatDateTime(date)
}
