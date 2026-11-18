package com.saurabh.sudoku.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.domain.model.Notes
import com.saurabh.sudoku.domain.model.SudokuBoard
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors

/**
 * 9×9 board (U15 / Part 4 §1): thin Navy200 cell lines (0.75dp), thick Navy600 3×3 + outer
 * borders (2dp). Digits scale with the cell width so the board survives 1.3x–1.5x font scale.
 */
@Composable
fun SudokuBoard(
    board: SudokuBoard,
    notes: Notes,
    selectedCell: Pair<Int, Int>?,
    highlightedCells: Set<Pair<Int, Int>> = emptySet(),
    sameNumberCells: Set<Pair<Int, Int>> = emptySet(),
    conflictCells: Set<Pair<Int, Int>> = emptySet(),
    highlightErrors: Boolean = true,
    isNotesMode: Boolean = false,
    lastPlacedCell: Pair<Int, Int>? = null,
    lastMistakeCell: Pair<Int, Int>? = null,
    onCellClick: (row: Int, col: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val TAG = "SudokuBoard"
    Log.d(TAG, "SudokuBoard composed with selectedCell: $selectedCell, highlightedCells: ${highlightedCells.size}, sameNumberCells: ${sameNumberCells.size}, conflictCells: ${conflictCells.size}")
    val status = LocalSudokuColors.current
    val density = LocalDensity.current
    val thin = with(density) { 0.75.dp.toPx() }
    val thick = with(density) { 2.dp.toPx() }

    BoxWithConstraints(
        modifier = modifier
            .testTag("sudoku_board")
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
    ) {
        // px → sp keeps the digit a fixed fraction of the cell regardless of font scale
        val cellPx = constraints.maxWidth / 9f
        val digitSize = with(density) { (cellPx * 0.56f).toSp() }
        val noteSize = with(density) { (cellPx * 0.24f).toSp() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(status.cellNormal)
                .drawWithContent {
                    drawContent()
                    val step = size.width / 9f
                    for (i in 1..8) {
                        if (i % 3 == 0) continue
                        val p = i * step
                        drawLine(status.gridThin, Offset(p, 0f), Offset(p, size.height), thin)
                        drawLine(status.gridThin, Offset(0f, p), Offset(size.width, p), thin)
                    }
                    for (i in 0..3) {
                        val p = (i * 3 * step).coerceIn(thick / 2, size.width - thick / 2)
                        drawLine(status.gridThick, Offset(p, 0f), Offset(p, size.height), thick)
                        drawLine(status.gridThick, Offset(0f, p), Offset(size.width, p), thick)
                    }
                }
        ) {
            for (boxRow in 0..2) {
                Row(modifier = Modifier.weight(1f)) {
                    for (boxCol in 0..2) {
                        Box3x3(
                            board            = board,
                            notes            = notes,
                            boxRow           = boxRow,
                            boxCol           = boxCol,
                            selectedCell     = selectedCell,
                            highlightedCells = highlightedCells,
                            sameNumberCells  = sameNumberCells,
                            conflictCells    = conflictCells,
                            highlightErrors  = highlightErrors,
                            isNotesMode      = isNotesMode,
                            lastPlacedCell   = lastPlacedCell,
                            lastMistakeCell  = lastMistakeCell,
                            digitSize        = digitSize,
                            noteSize         = noteSize,
                            onCellClick      = onCellClick,
                            modifier         = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
