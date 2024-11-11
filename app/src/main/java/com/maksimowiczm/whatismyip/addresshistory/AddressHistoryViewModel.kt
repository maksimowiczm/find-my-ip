package com.maksimowiczm.whatismyip.addresshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.whatismyip.data.Keys
import com.maksimowiczm.whatismyip.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AddressHistoryViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val state = userPreferencesRepository.get(Keys.save_history).map {
        if (it == true) {
            AddressHistoryState.Loading
        } else {
            AddressHistoryState.NoPermission
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = false
    )

    fun onGrantPermission() {
        viewModelScope.launch {
            userPreferencesRepository.set(Keys.save_history, true)
        }
    }
}

sealed interface AddressHistoryState {
    data object NoPermission : AddressHistoryState
    data object Loading : AddressHistoryState
}
