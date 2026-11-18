package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import com.saurabh.sudoku.domain.model.Notes
import com.saurabh.sudoku.domain.model.SudokuBoard

/** One 3×3 box. Grid lines are drawn by [SudokuBoard]; cells receive primitive params only. */
@Composable
fun Box3x3(
    board: SudokuBoard,
    notes: Notes,
    boxRow: Int,
    boxCol: Int,
    selectedCell: Pair<Int, Int>?,
    highlightedCells: Set<Pair<Int, Int>>,
    sameNumberCells: Set<Pair<Int, Int>>,
    conflictCells: Set<Pair<Int, Int>>,
    highlightErrors: Boolean,
    isNotesMode: Boolean,
    lastPlacedCell: Pair<Int, Int>?,
    lastMistakeCell: Pair<Int, Int>?,
    digitSize: TextUnit,
    noteSize: TextUnit,
    onCellClick: (row: Int, col: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (row in 0..2) {
            Row(modifier = Modifier.weight(1f)) {
                for (col in 0..2) {
                    val r = boxRow * 3 + row
                    val c = boxCol * 3 + col
                    val cellPair = r to c
                    val isSelected = selectedCell == cellPair

                    SudokuCell(
                        row            = r,
                        col            = c,
                        value          = board.getCurrentValue(r, c),
                        isGiven        = board.isInitialCell(r, c),
                        isSelected     = isSelected,
                        isPeer         = cellPair in highlightedCells && !isSelected,
                        isSameNumber   = cellPair in sameNumberCells,
                        isConflict     = cellPair in conflictCells && highlightErrors,
                        notesMask      = notes.getNotes(r, c).fold(0) { mask, n -> mask or (1 shl n) },
                        notesActive    = isNotesMode && isSelected,
                        justPlaced     = lastPlacedCell == cellPair,
                        justMistake    = lastMistakeCell == cellPair,
                        digitSize      = digitSize,
                        noteSize       = noteSize,
                        onClick        = onCellClick,
                        modifier       = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
