package com.maksimowiczm.findmyip.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.maksimowiczm.findmyip.shared.motion.materialSharedAxisXIn
import com.maksimowiczm.findmyip.shared.motion.materialSharedAxisXOut
import kotlin.jvm.JvmSuppressWildcards

fun NavGraphBuilder.forwardBackwardComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
            EnterTransition?)? =
        {
            ForwardBackwardComposableDefaults.enterTransition()
        },
    exitTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
            ExitTransition?)? =
        {
            ForwardBackwardComposableDefaults.exitTransition()
        },
    popEnterTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
            EnterTransition?)? =
        {
            ForwardBackwardComposableDefaults.popEnterTransition()
        },
    popExitTransition:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
            ExitTransition?)? =
        {
            ForwardBackwardComposableDefaults.popExitTransition()
        },
    sizeTransform:
        (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards
            SizeTransform?)? =
        null,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        sizeTransform = sizeTransform,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
object ForwardBackwardComposableDefaults {
    const val INITIAL_OFFSET_FACTOR = 0.15f

    fun enterTransition(offset: Float = INITIAL_OFFSET_FACTOR) =
        materialSharedAxisXIn(initialOffsetX = { (it * offset).toInt() })

    fun exitTransition(offset: Float = INITIAL_OFFSET_FACTOR) =
        materialSharedAxisXOut(targetOffsetX = { -(it * offset).toInt() })

    fun popEnterTransition(offset: Float = INITIAL_OFFSET_FACTOR) =
        scaleIn(
            animationSpec = MotionScheme.expressive().defaultSpatialSpec(),
            initialScale = 0.9f,
        ) + materialSharedAxisXIn(initialOffsetX = { -(it * offset).toInt() })

    fun popExitTransition(offset: Float = INITIAL_OFFSET_FACTOR) =
        scaleOut(
            animationSpec = MotionScheme.expressive().defaultSpatialSpec(),
            targetScale = 0.9f,
        ) + materialSharedAxisXOut(targetOffsetX = { (it * offset).toInt() })
}
