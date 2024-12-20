package com.maksimowiczm.findmyip.currentaddress

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object CurrentAddress

fun NavGraphBuilder.currentAddressGraph() {
    composable<CurrentAddress>(
        enterTransition = { slideIntoContainer(SlideDirection.Up) },
        exitTransition = { slideOutOfContainer(SlideDirection.Up) }
    ) {
        CurrentAddressScreen(Modifier.fillMaxSize())
    }
}
