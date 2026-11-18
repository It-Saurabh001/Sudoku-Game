package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.domain.model.Difficulty
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors
import com.saurabh.sudoku.presentation.ui.theme.animatePressColor
import com.saurabh.sudoku.presentation.ui.theme.rememberPressPhase

/** Difficulty FilterChips (U9): selected Blue100 + Blue900 + check icon; unselected SurfaceSoft + Muted. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DifficultySelector(
    selectedDifficulty: Difficulty,
    onDifficultySelected: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .testTag("difficulty_selector")
            .fillMaxWidth()
    ) {
        Text(
            text = "Select Difficulty",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Difficulty.entries.forEach { difficulty ->
                DifficultyChip(
                    difficulty = difficulty,
                    selected = selectedDifficulty == difficulty,
                    onClick = { onDifficultySelected(difficulty) }
                )
            }
        }

        Text(
            text = "${selectedDifficulty.clues} clues",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DifficultyChip(
    difficulty: Difficulty,
    selected: Boolean,
    onClick: () -> Unit
) {
    val status = LocalSudokuColors.current
    val interaction = remember { MutableInteractionSource() }
    val phase = rememberPressPhase(interaction)
    val container by animatePressColor(
        phase,
        if (selected) status.chipSelected else status.chipUnselected,
        if (selected) status.chipSelectedPressed else status.secondaryPressed,
        if (selected) status.chipSelectedHeld else status.secondaryHeld,
        "difficultyChip"
    )
    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            label = {
                Text(
                    text = difficulty.displayName,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = if (selected) {
                { Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
            } else null,
            shape = MaterialTheme.shapes.small,
            colors = FilterChipDefaults.filterChipColors(
                containerColor = container,
                labelColor = status.onChipUnselected,
                selectedContainerColor = container,
                selectedLabelColor = status.onChipSelected
            ),
            border = FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = selected,
                borderColor = MaterialTheme.colorScheme.outline,
                selectedBorderColor = MaterialTheme.colorScheme.primary
            ),
            interactionSource = interaction
        )
    }
}
