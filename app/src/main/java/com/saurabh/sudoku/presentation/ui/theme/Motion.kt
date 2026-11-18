package com.saurabh.sudoku.presentation.ui.theme

import android.provider.Settings
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalViewConfiguration
import kotlinx.coroutines.delay

/** docs/ui_rules.md U13 + Part 3 timing tokens. */
object SudokuMotion {
    const val PRESS_MS = 110
    const val FORWARD_MS = 300
    const val BACK_MS = 280
    const val TAB_MS = 200
    const val DIALOG_MS = 180
    const val NUMBER_PAD_MS = 200
    const val PRESSED_SCALE = 0.97f
}

/** true when the in-app Reduce Motion toggle is ON or the system animator scale is 0. */
val LocalReduceMotion = compositionLocalOf { false }

/** Mirrors the Settings vibration toggle; gates long-press haptics. */
val LocalHapticsEnabled = compositionLocalOf { true }

@Composable
fun rememberSystemReduceMotion(): Boolean {
    val context = LocalContext.current
    return remember(context) {
        runCatching {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f
            ) == 0f
        }.getOrDefault(false)
    }
}

// ── Touch: Golden Rule press / hold shade-swap ──────────────────────────────

enum class PressPhase { Idle, Pressed, Held }

/**
 * Tracks press → hold for a component using its own [interactionSource].
 * Hold (long-press timeout) fires a LongPress haptic when Settings haptics are on.
 */
@Composable
fun rememberPressPhase(
    interactionSource: InteractionSource,
    enabled: Boolean = true,
    hapticOnHold: Boolean = true,
): PressPhase {
    val isPressed by interactionSource.collectIsPressedAsState()
    var held by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val hapticsEnabled = LocalHapticsEnabled.current
    val holdTimeout = LocalViewConfiguration.current.longPressTimeoutMillis
    LaunchedEffect(isPressed, enabled) {
        held = false
        if (isPressed && enabled) {
            delay(holdTimeout)
            held = true
            if (hapticOnHold && hapticsEnabled) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }
    return when {
        !enabled || !isPressed -> PressPhase.Idle
        held -> PressPhase.Held
        else -> PressPhase.Pressed
    }
}

/** Same-family colour for the current [phase], animated with tween(110). */
@Composable
fun animatePressColor(
    phase: PressPhase,
    normal: Color,
    pressed: Color,
    held: Color,
    label: String = "pressColor",
): State<Color> = animateColorAsState(
    targetValue = when (phase) {
        PressPhase.Idle -> normal
        PressPhase.Pressed -> pressed
        PressPhase.Held -> held
    },
    animationSpec = tween(SudokuMotion.PRESS_MS),
    label = label
)

/** 0.97f press scale — ONLY for standalone buttons (never board, cells, rows or cards). */
@Composable
fun Modifier.standaloneButtonPressScale(phase: PressPhase): Modifier {
    val reduceMotion = LocalReduceMotion.current
    val scale by animateFloatAsState(
        targetValue = if (phase != PressPhase.Idle && !reduceMotion) SudokuMotion.PRESSED_SCALE else 1f,
        animationSpec = tween(SudokuMotion.PRESS_MS),
        label = "pressScale"
    )
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

/** Dialog enter: centre scale 0.92 → 1.0 + fade, 180 ms (skipped with Reduce Motion). */
@Composable
fun Modifier.dialogEnterAnimation(): Modifier {
    val reduceMotion = LocalReduceMotion.current
    val progress = remember { Animatable(if (reduceMotion) 1f else 0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(SudokuMotion.DIALOG_MS, easing = FastOutSlowInEasing))
    }
    return this.graphicsLayer {
        val p = progress.value
        val s = 0.92f + 0.08f * p
        scaleX = s
        scaleY = s
        alpha = p
    }
}

// ── Screen transitions (Part 3 §2) ──────────────────────────────────────────

fun sudokuForwardEnter(): EnterTransition =
    slideInHorizontally(tween(SudokuMotion.FORWARD_MS, easing = FastOutSlowInEasing)) { it } +
        fadeIn(tween(SudokuMotion.FORWARD_MS, easing = FastOutSlowInEasing))

fun sudokuForwardExit(): ExitTransition =
    slideOutHorizontally(tween(SudokuMotion.FORWARD_MS, easing = FastOutSlowInEasing)) { -it / 4 } +
        fadeOut(tween(SudokuMotion.FORWARD_MS, easing = FastOutSlowInEasing), targetAlpha = 0.85f)

fun sudokuPopEnter(): EnterTransition =
    slideInHorizontally(tween(SudokuMotion.BACK_MS, easing = FastOutSlowInEasing)) { -it / 4 } +
        fadeIn(tween(SudokuMotion.BACK_MS, easing = FastOutSlowInEasing), initialAlpha = 0.85f)

fun sudokuPopExit(): ExitTransition =
    slideOutHorizontally(tween(SudokuMotion.BACK_MS, easing = FastOutSlowInEasing)) { it }
