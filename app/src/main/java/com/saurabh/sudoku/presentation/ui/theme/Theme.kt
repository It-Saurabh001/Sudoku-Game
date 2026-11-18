package com.saurabh.sudoku.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

// docs/ui_rules.md Part 2 §7 — exact light mapping.
private val LightColorScheme = lightColorScheme(
    primary = Blue600,
    onPrimary = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue900,
    inversePrimary = Blue300,
    secondary = Navy600,
    onSecondary = White,
    secondaryContainer = Navy100,
    onSecondaryContainer = Navy900,
    tertiary = Cyan600,
    onTertiary = White,
    tertiaryContainer = Cyan100,
    onTertiaryContainer = Cyan900,
    error = Red,
    onError = White,
    errorContainer = RedLight,
    onErrorContainer = RedStrong,
    background = OffWhite,
    onBackground = Ink,
    surface = White,
    onSurface = Ink,
    surfaceVariant = SurfaceSoft,
    onSurfaceVariant = Muted,
    surfaceTint = Blue600,
    inverseSurface = Navy900,
    inverseOnSurface = White,
    outline = Outline,
    outlineVariant = Navy100,
    scrim = Scrim,
    surfaceBright = White,
    surfaceDim = FieldFill,
    surfaceContainerLowest = White,
    surfaceContainerLow = OffWhite,
    surfaceContainer = SurfaceSoft,
    surfaceContainerHigh = FieldFill,
    surfaceContainerHighest = Navy100,
)

// Deliberate dark mapping: Navy900 anchor, never pure black.
private val DarkColorScheme = darkColorScheme(
    primary = Blue300,
    onPrimary = Navy900,
    primaryContainer = Blue800,
    onPrimaryContainer = Blue100,
    inversePrimary = Blue600,
    secondary = Navy200,
    onSecondary = Navy900,
    secondaryContainer = Navy500,
    onSecondaryContainer = Navy50,
    tertiary = Cyan300,
    onTertiary = Cyan900,
    tertiaryContainer = Cyan800,
    onTertiaryContainer = Cyan100,
    error = DarkRedText,
    onError = Navy900,
    errorContainer = DarkRedContainer,
    onErrorContainer = DarkRedText,
    background = Navy900,
    onBackground = White,
    surface = DarkSurface,
    onSurface = White,
    surfaceVariant = DarkSurfaceSoft,
    onSurfaceVariant = DarkMuted,
    surfaceTint = Blue300,
    inverseSurface = Navy50,
    inverseOnSurface = Navy900,
    outline = DarkOutline,
    outlineVariant = Navy700,
    scrim = Scrim,
    surfaceBright = DarkFieldFill,
    surfaceDim = Navy900,
    surfaceContainerLowest = Navy900,
    surfaceContainerLow = Navy800,
    surfaceContainer = DarkSurface,
    surfaceContainerHigh = DarkSurfaceSoft,
    surfaceContainerHighest = DarkFieldFill,
)

/**
 * @param themeMode "light" | "dark" | "system"
 * @param reduceMotion in-app Reduce Motion toggle (system animator scale is also honoured)
 * @param hapticsEnabled Settings vibration toggle — gates long-press haptics
 * @param dynamicColor optional Material You for shell surfaces only (board cues stay fixed)
 */
@Composable
fun SudokuTheme(
    themeMode: String = "system",
    reduceMotion: Boolean = false,
    hapticsEnabled: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (themeMode) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val statusColors = if (useDarkTheme) DarkSudokuStatusColors else LightSudokuStatusColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Allow content to draw behind status bars
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val windowInsetsController = WindowInsetsControllerCompat(window, view)
            windowInsetsController.isAppearanceLightStatusBars = !useDarkTheme
            windowInsetsController.isAppearanceLightNavigationBars = !useDarkTheme
        }
    }

    val systemReduceMotion = rememberSystemReduceMotion()

    CompositionLocalProvider(
        LocalSudokuColors provides statusColors,
        LocalReduceMotion provides (reduceMotion || systemReduceMotion),
        LocalHapticsEnabled provides hapticsEnabled,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
