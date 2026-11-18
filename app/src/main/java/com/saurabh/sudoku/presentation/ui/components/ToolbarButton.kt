package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors
import com.saurabh.sudoku.presentation.ui.theme.animatePressColor
import com.saurabh.sudoku.presentation.ui.theme.rememberPressPhase

/**
 * Action-toolbar tile (Part 4 §2): 48dp icon tile + labelSmall caption + optional badge.
 * Defaults to SurfaceSoft fill + Navy600 icon; callers pass family shades for Notes/Hint.
 * Shade-swap only (ripple off, no scale).
 */
@Composable
fun ToolbarButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    badge: String? = null,
    container: Color = LocalSudokuColors.current.toolTile,
    containerPressed: Color = LocalSudokuColors.current.toolTilePressed,
    containerHeld: Color = LocalSudokuColors.current.toolTileHeld,
    contentColor: Color = LocalSudokuColors.current.onToolTile,
) {
    val status = LocalSudokuColors.current
    val interaction = remember { MutableInteractionSource() }
    val phase = rememberPressPhase(interaction, enabled)
    val bg by animatePressColor(
        phase,
        if (enabled) container else status.disabledFill,
        containerPressed,
        containerHeld,
        "toolTile"
    )
    val tint = if (enabled) contentColor else status.disabledContent

    Column(
        modifier = modifier
            .widthIn(min = 48.dp)
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .semantics { this.contentDescription = contentDescription },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(bg)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint)
            if (badge != null) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp),
                    shape = MaterialTheme.shapes.extraSmall,
                    color = tint,
                    contentColor = if (enabled) container else status.disabledFill
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 3.dp)
                    )
                }
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
