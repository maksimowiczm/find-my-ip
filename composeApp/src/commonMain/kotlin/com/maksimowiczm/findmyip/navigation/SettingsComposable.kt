package com.maksimowiczm.findmyip.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughIn
import com.maksimowiczm.findmyip.ui.motion.materialFadeThroughOut
import kotlin.reflect.KType

inline fun <reified T : Any> NavGraphBuilder.settingsComposable(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline sizeTransform: (
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards SizeTransform?
    )? = null,
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    forwardBackwardComposable<T>(
        typeMap = typeMap,
        deepLinks = deepLinks,
        enterTransition = {
            if (initialState.destination.hasRoute<TopRoute.Home>() ||
                initialState.destination.hasRoute<TopRoute.History>()
            ) {
                materialFadeThroughIn()
            } else {
                ForwardBackwardComposableDefaults.enterTransition()
            }
        },
        exitTransition = {
            if (
                targetState.destination.hasRoute<TopRoute.Home>() ||
                targetState.destination.hasRoute<TopRoute.History>()
            ) {
                materialFadeThroughOut()
            } else {
                ForwardBackwardComposableDefaults.exitTransition()
            }
        },
        popEnterTransition = {
            if (initialState.destination.hasRoute<TopRoute.Home>() ||
                initialState.destination.hasRoute<TopRoute.History>()
            ) {
                materialFadeThroughIn()
            } else {
                ForwardBackwardComposableDefaults.popEnterTransition()
            }
        },
        popExitTransition = {
            if (
                targetState.destination.hasRoute<TopRoute.Home>() ||
                targetState.destination.hasRoute<TopRoute.History>()
            ) {
                materialFadeThroughOut()
            } else {
                ForwardBackwardComposableDefaults.popExitTransition()
            }
        },
        sizeTransform = sizeTransform,
        content = content
    )
}
