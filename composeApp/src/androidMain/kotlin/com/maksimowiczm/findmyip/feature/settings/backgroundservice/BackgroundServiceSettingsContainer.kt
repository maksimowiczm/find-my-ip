package com.maksimowiczm.findmyip.feature.settings.backgroundservice

import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.maksimowiczm.findmyip.data.AddressRefreshWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.dsl.updateStateImmediate
import pro.respawn.flowmvi.plugins.reduce
import pro.respawn.flowmvi.plugins.whileSubscribed

private typealias State = BackgroundServiceSettingsState
private typealias Intent = BackgroundServiceSettingsIntent
private typealias Action = MVIAction

internal class BackgroundServiceSettingsContainer(private val workManager: WorkManager) :
    Container<State, Intent, Action> {
    override val store = store(BackgroundServiceSettingsState.Loading) {
        whileSubscribed {
            workManager
                .getWorkInfosByTagFlow(AddressRefreshWorker.TAG)
                .map { infos ->
                    when (infos.firstOrNull()?.state) {
                        null,
                        WorkInfo.State.SUCCEEDED,
                        WorkInfo.State.FAILED,
                        WorkInfo.State.BLOCKED,
                        WorkInfo.State.CANCELLED -> false

                        WorkInfo.State.ENQUEUED,
                        WorkInfo.State.RUNNING -> true
                    }
                }.onEach {
                    updateState {
                        when (it) {
                            true -> BackgroundServiceSettingsState.Enabled
                            false -> BackgroundServiceSettingsState.Disabled
                        }
                    }
                }.launchIn(this)
        }

        reduce {
            when (it) {
                BackgroundServiceSettingsIntent.Disable -> {
                    updateStateImmediate { BackgroundServiceSettingsState.Disabling }

                    delay(1000L)

                    AddressRefreshWorker.cancelPeriodicWorkRequest(workManager)
                }

                BackgroundServiceSettingsIntent.Enable -> {
                    updateStateImmediate { BackgroundServiceSettingsState.Enabling }

                    delay(1000L)

                    AddressRefreshWorker.cancelAndCreatePeriodicWorkRequest(workManager)
                }
            }
        }
    }
}
