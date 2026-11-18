package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.domain.model.Difficulty

/**
 * New-game card. [isPrimaryAction] = false when a Continue card already owns the screen's
 * single primary CTA, so "Start Game" renders as a secondary button (U8).
 */
@Composable
fun NewGameSection(
    selectedDifficulty: Difficulty,
    onDifficultySelected: (Difficulty) -> Unit,
    onStartNewGame: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    isPrimaryAction: Boolean = true
) {
    SudokuCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            DifficultySelector(
                selectedDifficulty = selectedDifficulty,
                onDifficultySelected = onDifficultySelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            val label: @Composable () -> Unit = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = LocalContentColor.current
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Start Game", style = MaterialTheme.typography.labelLarge, maxLines = 1)
            }
            if (isPrimaryAction) {
                PrimaryButton(
                    onClick = onStartNewGame,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) { label() }
            } else {
                SecondaryButton(
                    onClick = onStartNewGame,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) { label() }
            }
        }
    }
}
