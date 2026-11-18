package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.presentation.ui.theme.LocalReduceMotion
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors
import com.saurabh.sudoku.presentation.ui.theme.SudokuMotion

/**
 * Single cell. Stable primitive params only (Part 3 §3).
 * State priority (U15): Conflict > Selected > Same-number > Peer > Normal.
 * Press = Cyan family shade-swap, no ripple, NO press scale.
 * Pop / shake run in graphicsLayer and are disabled with Reduce Motion.
 */
@Composable
fun SudokuCell(
    row: Int,
    col: Int,
    value: Int,
    isGiven: Boolean,
    isSelected: Boolean,
    isPeer: Boolean,
    isSameNumber: Boolean,
    isConflict: Boolean,
    notesMask: Int,
    notesActive: Boolean,
    justPlaced: Boolean,
    justMistake: Boolean,
    digitSize: TextUnit,
    noteSize: TextUnit,
    onClick: (row: Int, col: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val status = LocalSudokuColors.current
    val reduceMotion = LocalReduceMotion.current
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()

    // ── Background (strict priority) ────────────────────────────────────────
    val targetBg = when {
        isConflict   -> status.cellConflict
        justPlaced   -> status.cellCorrect
        isSelected   -> if (isPressed) status.cellSelectedPressed else status.cellSelected
        isSameNumber -> status.cellSameNumber
        isPeer       -> status.cellPeer
        isPressed    -> status.cellPressed
        else         -> status.cellNormal
    }
    val animatedBg by animateColorAsState(targetBg, tween(SudokuMotion.PRESS_MS), label = "cellBg")

    val textColor = when {
        isConflict                 -> status.onCellConflict
        justPlaced                 -> status.onCellCorrect
        isSelected || isSameNumber -> status.digitHighlighted
        isGiven                    -> status.digitGiven
        else                       -> status.digitUser
    }

    // ── Placement pop: 1.0 → 1.12 → 1.0 ─────────────────────────────────────
    val pop = remember { Animatable(1f) }
    LaunchedEffect(justPlaced) {
        if (justPlaced && !reduceMotion) {
            val bouncy = spring<Float>(Spring.DampingRatioMediumBouncy, Spring.StiffnessHigh)
            pop.animateTo(1.12f, bouncy)
            pop.animateTo(1f, bouncy)
        }
    }

    // ── Mistake shake: 0 → 6 → -6 → 4 → -4 → 0 (300ms) ─────────────────────
    val shake = remember { Animatable(0f) }
    LaunchedEffect(justMistake) {
        if (justMistake && !reduceMotion) {
            shake.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 300
                    6f at 60
                    -6f at 120
                    4f at 180
                    -4f at 240
                    0f at 300
                }
            )
        }
    }

    val description = buildString {
        append("Row ${row + 1}, column ${col + 1}, ")
        append(if (value != 0) "$value" else "empty")
        if (isGiven) append(", given")
        if (isConflict) append(", conflict")
        if (isSelected) append(", selected")
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .background(animatedBg)
            .then(
                if (isSelected) Modifier.border(2.dp, status.cellSelectedBorder) else Modifier
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = { onClick(row, col) }
            )
            .semantics { contentDescription = description }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = shake.value * density
                    scaleX = pop.value
                    scaleY = pop.value
                }
        ) {
            when {
                value != 0 -> Text(
                    text = value.toString(),
                    style = TextStyle(
                        fontSize = digitSize,
                        lineHeight = digitSize,
                        fontWeight = if (isGiven) FontWeight.Bold else FontWeight.SemiBold,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )
                )
                notesMask != 0 -> NoteGrid(
                    notesMask = notesMask,
                    noteSize = noteSize,
                    color = if (notesActive) status.noteActive else status.note
                )
            }
        }
    }
}

@Composable
private fun NoteGrid(notesMask: Int, noteSize: TextUnit, color: Color) {
    Column(
        modifier = Modifier.fillMaxSize().padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (noteRow in 0..2) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (noteCol in 0..2) {
                    val n = noteRow * 3 + noteCol + 1
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (notesMask and (1 shl n) != 0) {
                            Text(
                                text = n.toString(),
                                style = TextStyle(
                                    fontSize = noteSize,
                                    lineHeight = noteSize,
                                    fontWeight = FontWeight.Medium,
                                    color = color,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
