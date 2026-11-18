package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors
import com.saurabh.sudoku.presentation.ui.theme.animatePressColor
import com.saurabh.sudoku.presentation.ui.theme.rememberPressPhase
import com.saurabh.sudoku.presentation.ui.theme.standaloneButtonPressScale

/**
 * Primary CTA (Blue600 → Blue700 pressed → Blue800 held). Ripple off, 0.97f scale.
 * Use ONE per screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val phase = rememberPressPhase(interaction, enabled)
    val status = LocalSudokuColors.current
    val container by animatePressColor(
        phase, MaterialTheme.colorScheme.primary, status.primaryPressed, status.primaryHeld, "primaryBtn"
    )
    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        Button(
            onClick = onClick,
            modifier = modifier.standaloneButtonPressScale(phase).heightIn(min = 48.dp),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = container,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = status.disabledFill,
                disabledContentColor = status.disabledContent
            ),
            interactionSource = interaction,
            content = content
        )
    }
}

/** Secondary button: surface + Outline border + Ink text (Blue50 pressed → Blue100 held). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val phase = rememberPressPhase(interaction, enabled)
    val status = LocalSudokuColors.current
    val container by animatePressColor(
        phase, MaterialTheme.colorScheme.surface, status.secondaryPressed, status.secondaryHeld, "secondaryBtn"
    )
    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.standaloneButtonPressScale(phase).heightIn(min = 48.dp),
            enabled = enabled,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = container,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = status.disabledFill,
                disabledContentColor = status.disabledContent
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            interactionSource = interaction,
            content = content
        )
    }
}

/** Destructive dialog confirm (U11): Red text, Red-family press/hold container. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DangerTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val phase = rememberPressPhase(interaction)
    val status = LocalSudokuColors.current
    val container by animatePressColor(
        phase, MaterialTheme.colorScheme.surface.copy(alpha = 0f), status.dangerPressed, status.dangerHeld, "dangerBtn"
    )
    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        TextButton(
            onClick = onClick,
            modifier = modifier.standaloneButtonPressScale(phase).heightIn(min = 48.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = container,
                contentColor = status.danger
            ),
            interactionSource = interaction,
            content = content
        )
    }
}
