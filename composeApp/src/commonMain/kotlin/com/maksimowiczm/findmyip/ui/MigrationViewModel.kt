package com.maksimowiczm.findmyip.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maksimowiczm.findmyip.data.IsMigratedFrom1
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.infrastructure.di.observe
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MigrationViewModel(
    private val isMigratedFrom1: IsMigratedFrom1,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val internalHideDialog = MutableStateFlow(false)

    val showMigrationDialog =
        combine(
            internalHideDialog,
            dataStore.observe(PreferenceKeys.hideMigrationDialog).map { it ?: false }
        ) { internalHideDialog, hideMigrationDialog ->
            internalHideDialog || hideMigrationDialog
        }.map { hide ->
            if (hide) {
                false
            } else {
                isMigratedFrom1()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = false
        )

    fun hideMigrationDialog() {
        viewModelScope.launch {
            internalHideDialog.value = true
        }
    }

    fun dontShowMigrationDialogAgain() {
        viewModelScope.launch {
            dataStore.set(PreferenceKeys.hideMigrationDialog to true)
        }
    }
}
