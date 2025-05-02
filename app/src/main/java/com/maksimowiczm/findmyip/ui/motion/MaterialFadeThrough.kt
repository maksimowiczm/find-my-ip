package com.maksimowiczm.findmyip.ui.motion

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn

/**
 * Creates a MaterialFadeThrough enter transition
 */
fun materialFadeThroughIn(durationMillis: Int = 200, initialScale: Float = 0.92f): EnterTransition {
    val fadeInDuration = (durationMillis * 0.75f).toInt()
    val scaleDuration = (durationMillis * 0.80f).toInt()
    val delayScale = durationMillis - scaleDuration

    return fadeIn(
        animationSpec = tween(
            durationMillis = fadeInDuration,
            easing = LinearOutSlowInEasing
        )
    ) + scaleIn(
        animationSpec = tween(
            durationMillis = scaleDuration,
            delayMillis = delayScale,
            easing = FastOutSlowInEasing
        ),
        initialScale = initialScale
    )
}

/**
 * Creates a MaterialFadeThrough exit transition
 */
fun materialFadeThroughOut(durationMillis: Int = 200): ExitTransition {
    val fadeOutDuration = (durationMillis * 0.15f).toInt()

    return fadeOut(
        animationSpec = tween(
            durationMillis = fadeOutDuration,
            easing = FastOutSlowInEasing
        )
    )
}
