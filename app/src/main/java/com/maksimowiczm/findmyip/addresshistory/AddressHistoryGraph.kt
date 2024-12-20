package com.maksimowiczm.findmyip.addresshistory

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object AddressHistory

fun NavGraphBuilder.addressHistoryGraph(onGrantPermission: () -> Unit) {
    composable<AddressHistory>(
        enterTransition = { slideIntoContainer(SlideDirection.Up) },
        exitTransition = { slideOutOfContainer(SlideDirection.Up) }
    ) {
        AddressHistoryScreen(
            modifier = Modifier.fillMaxSize(),
            onGrantPermission = onGrantPermission
        )
    }
}
