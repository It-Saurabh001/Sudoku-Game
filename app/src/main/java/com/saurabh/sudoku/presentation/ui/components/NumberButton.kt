package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors
import com.saurabh.sudoku.presentation.ui.theme.animatePressColor
import com.saurabh.sudoku.presentation.ui.theme.rememberPressPhase
import com.saurabh.sudoku.presentation.ui.theme.standaloneButtonPressScale

/**
 * Tactile digit key (≥48×52dp). Surface + Outline → Blue50 pressed → Blue100 held.
 * Corner badge = remaining count. 0 left → SurfaceSoft + Disabled, no press state.
 */
@Composable
fun NumberButton(
    number: Int,
    remaining: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val status = LocalSudokuColors.current
    val isEnabled = remaining > 0
    val interaction = remember { MutableInteractionSource() }
    val phase = rememberPressPhase(interaction, isEnabled)
    val container by animatePressColor(
        phase,
        if (isEnabled) status.key else status.disabledFill,
        status.secondaryPressed,
        status.secondaryHeld,
        "numberKey"
    )
    val content = if (isEnabled) status.onKey else status.disabledContent
    val shape = MaterialTheme.shapes.medium

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .standaloneButtonPressScale(phase)
            .widthIn(min = 48.dp)
            .heightIn(min = 52.dp)
            .clip(shape)
            .background(container)
            .border(1.dp, if (isEnabled) status.keyBorder else status.disabledFill, shape)
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = isEnabled,
                role = Role.Button,
                onClick = { if (isEnabled) onClick(number) }
            )
            .semantics { contentDescription = "Number $number, $remaining remaining" }
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = content,
            maxLines = 1
        )
        Text(
            text = remaining.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = if (isEnabled) status.keyBadge else status.disabledContent,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 2.dp, end = 4.dp)
        )
    }
}
