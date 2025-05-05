package com.maksimowiczm.findmyip.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughIn
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughOut
import com.maksimowiczm.findmyip.ui.motion.materialSharedAxisXIn
import com.maksimowiczm.findmyip.ui.motion.materialSharedAxisXOut

// Composable tightly coupled with settings routes. Adjusts the animation to the settings/app
// navigation.
inline fun <reified T : Any> NavGraphBuilder.settingsComposable(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        enterTransition = {
            if (initialState.destination.hasRoute<SettingsHome>()) {
                ForwardBackwardComposableDefaults.enterTransition()
            } else {
                materialFadeThroughIn()
            }
        },
        exitTransition = {
            if (targetState.destination.hasRoute<SettingsHome>()) {
                ForwardBackwardComposableDefaults.exitTransition()
            } else {
                materialFadeThroughOut()
            }
        },
        popEnterTransition = {
            ForwardBackwardComposableDefaults.popEnterTransition()
        },
        popExitTransition = {
            if (targetState.destination.hasRoute<SettingsHome>()) {
                ForwardBackwardComposableDefaults.popExitTransition()
            } else {
                materialFadeThroughOut()
            }
        },
        content = content
    )
}

object ForwardBackwardComposableDefaults {
    const val INITIAL_OFFSET_FACTOR = 0.10f

    fun enterTransition(offset: Float = INITIAL_OFFSET_FACTOR) =
        materialSharedAxisXIn(initialOffsetX = { (it * offset).toInt() })

    fun exitTransition(offset: Float = INITIAL_OFFSET_FACTOR) =
        materialSharedAxisXOut(targetOffsetX = { -(it * offset).toInt() })

    fun popEnterTransition(offset: Float = INITIAL_OFFSET_FACTOR) =
        materialSharedAxisXIn(initialOffsetX = { -(it * offset).toInt() })

    fun popExitTransition(offset: Float = INITIAL_OFFSET_FACTOR) =
        materialSharedAxisXOut(targetOffsetX = { (it * offset).toInt() })
}
