package com.saurabh.sudoku.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.automirrored.outlined.Redo
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors
import com.saurabh.sudoku.presentation.utils.formatTime

/**
 * Action toolbar (Part 4 §2): Undo, Redo, Erase, Notes, Hint.
 * Pause / Reset live in the top app bar; timer is [GameTimer].
 */
@Composable
fun GameToolbar(
    hintsRemaining: Int,
    isNotesMode: Boolean,
    notesEnabled: Boolean,
    onNotesModeToggle: () -> Unit,
    onHintClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onEraseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val TAG = "GameToolbar"
    Log.d(TAG, "GameToolbar composed with hintsRemaining=$hintsRemaining, isNotesMode=$isNotesMode")
    val status = LocalSudokuColors.current

    Row(
        modifier = modifier
            .testTag("game_toolbar")
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ToolbarButton(
            icon = Icons.AutoMirrored.Outlined.Undo,
            label = "Undo",
            onClick = onUndoClick,
            contentDescription = "Undo last move"
        )
        ToolbarButton(
            icon = Icons.AutoMirrored.Outlined.Redo,
            label = "Redo",
            onClick = onRedoClick,
            contentDescription = "Redo move"
        )
        ToolbarButton(
            icon = Icons.AutoMirrored.Outlined.Backspace,
            label = "Erase",
            onClick = onEraseClick,
            contentDescription = "Erase selected cell"
        )
        if (!notesEnabled) {
            ToolbarButton(
                icon = Icons.Outlined.EditNote,
                label = "Notes",
                onClick = onNotesModeToggle,
                enabled = false,
                contentDescription = "Notes disabled in Settings"
            )
        } else if (isNotesMode) {
            ToolbarButton(
                icon = Icons.Filled.EditNote,
                label = "Notes",
                onClick = onNotesModeToggle,
                contentDescription = "Notes on. Switch to number mode",
                badge = "ON",
                container = status.notesOn,
                containerPressed = status.chipSelectedPressed,
                containerHeld = status.chipSelectedHeld,
                contentColor = status.onNotesOn
            )
        } else {
            ToolbarButton(
                icon = Icons.Outlined.EditNote,
                label = "Notes",
                onClick = onNotesModeToggle,
                contentDescription = "Notes off. Switch to notes mode"
            )
        }
        ToolbarButton(
            icon = Icons.Outlined.Lightbulb,
            label = "Hint",
            onClick = onHintClick,
            enabled = hintsRemaining > 0,
            contentDescription = "Hint, $hintsRemaining left",
            badge = hintsRemaining.toString(),
            container = status.hintTile,
            containerPressed = status.hintTilePressed,
            containerHeld = status.hintTileHeld,
            contentColor = status.onHintTile
        )
    }
}

/** Isolated timer (Part 3 §3): only this recomposes on the 1-second tick. */
@Composable
fun GameTimer(
    elapsedTime: Long,
    modifier: Modifier = Modifier
) {
    val text = elapsedTime.formatTime()
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        modifier = modifier.semantics { contentDescription = "Time $text" }
    )
}
