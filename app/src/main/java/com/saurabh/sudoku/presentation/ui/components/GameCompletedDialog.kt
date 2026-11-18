package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.saurabh.sudoku.domain.model.Difficulty
import com.saurabh.sudoku.presentation.ui.theme.LocalReduceMotion
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors
import com.saurabh.sudoku.presentation.ui.theme.SudokuTheme
import com.saurabh.sudoku.presentation.ui.theme.dialogEnterAnimation

/**
 * Win dialog: Green-family accent banner (ScoreCelebrationEffect / static trophy with
 * Reduce Motion) + Blue600 primary action. No gradients/glow (U7).
 *
 *  ┌───────────────────────────────────────────────┐
 *  │  [ ScoreCelebrationEffect — successContainer ] │
 *  │  Puzzle Solved!                                │
 *  │  [Hard]  ✓ No hints used                       │
 *  │   Time        Difficulty      Hints            │
 *  │  [  Home  ]           [  New Game  ]           │
 *  └───────────────────────────────────────────────┘
 */
@Composable
fun GameCompletedDialog(
    difficulty: Difficulty,
    completionTime: Long,
    hintsUsed: Int,
    onNewGame: () -> Unit,
    onMainMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val status = LocalSudokuColors.current
    val reduceMotion = LocalReduceMotion.current

    // Animate the stats section sliding in after the Lottie starts
    var statsVisible by remember { mutableStateOf(reduceMotion) }
    LaunchedEffect(Unit) {
        if (!reduceMotion) kotlinx.coroutines.delay(400)   // slight delay after dialog appears
        statsVisible = true
    }

    Dialog(
        onDismissRequest = onMainMenu,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,    // we control the width ourselves
            dismissOnClickOutside   = false     // player must explicitly choose
        )
    ) {
        Card(
            modifier = modifier
                .testTag("completion_dialog")
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .dialogEnterAnimation(),
            shape  = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier            = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Celebration banner (Green family) ─────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp)
                        .background(status.successContainer),
                    contentAlignment = Alignment.Center
                ) {
                    ScoreCelebrationEffect(
                        modifier   = Modifier.fillMaxWidth().height(200.dp),
                        iterations = 3              // 3 loops then stops naturally
                    )
                }

                // ── Title + stats ─────────────────────────────────────────
                AnimatedVisibility(
                    visible = statsVisible,
                    enter   = fadeIn(tween(400)) + slideInVertically(
                        tween(400),
                        initialOffsetY = { it / 3 }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text       = "Puzzle Solved!",
                            style      = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color      = status.onSuccessContainer,
                            textAlign  = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DifficultyBadge(difficulty)
                            if (hintsUsed == 0) {
                                Icon(
                                    Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = status.success,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "No hints used",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // ── Stats row ─────────────────────────────────────
                        Spacer(Modifier.height(24.dp))

                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatChip(label = "Time",       value = formatTime(completionTime), modifier = Modifier.weight(1f))
                            StatChip(label = "Difficulty", value = difficulty.displayName,     modifier = Modifier.weight(1f))
                            StatChip(label = "Hints",      value = hintsUsed.toString(),       modifier = Modifier.weight(1f))
                        }

                        // ── Action buttons ────────────────────────────────
                        Spacer(Modifier.height(24.dp))

                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SecondaryButton(
                                onClick  = onMainMenu,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Home", style = MaterialTheme.typography.labelLarge, maxLines = 1)
                            }

                            PrimaryButton(
                                onClick  = onNewGame,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("New Game", style = MaterialTheme.typography.labelLarge, maxLines = 1)
                            }
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

/** Small tonal chip used in the stats row. */
@Composable
private fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text       = value,
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onSurface,
                textAlign  = TextAlign.Center,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis,
                modifier   = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

/** Formats milliseconds → "mm:ss" string. */
private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes      = totalSeconds / 60
    val seconds      = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 400, heightDp = 700)
@Composable
private fun GameCompletedDialogPreview() {
    SudokuTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(LocalSudokuColors.current.scrim.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            GameCompletedDialog(
                difficulty     = Difficulty.HARD,
                completionTime = 312_000L,   // 5:12
                hintsUsed      = 0,
                onNewGame      = {},
                onMainMenu     = {}
            )
        }
    }
}
