package com.saurabh.sudoku.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

private val KeyGap = 8.dp
private val MinKeyWidth = 48.dp

/**
 * NumberPad (Part 4 §2).
 *  - [columns] = 3 → 3×3 calculator layout (2-pane / ≥600dp).
 *  - otherwise: single row of 9 when every key can stay ≥48dp wide,
 *    else a balanced 2-row grid (1–5 / 6–9).
 * Each key shows its remaining count; keys with 0 left are disabled.
 */
@Composable
fun NumberPad(
    numberCounts: Map<Int, Int>,
    onNumberSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    columns: Int? = null
) {
    val TAG = "NumberPad"
    Log.d(TAG, "NumberPad composed with numberCounts: $numberCounts")

    BoxWithConstraints(modifier = modifier.testTag("number_pad").fillMaxWidth()) {
        val singleRowKey = (maxWidth - KeyGap * 8) / 9
        val rows: List<List<Int>> = when {
            columns == 3 -> listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))
            singleRowKey >= MinKeyWidth -> listOf((1..9).toList())
            else -> listOf(listOf(1, 2, 3, 4, 5), listOf(6, 7, 8, 9))
        }
        val perRow = rows.maxOf { it.size }
        val keyWidth = (maxWidth - KeyGap * (perRow - 1)) / perRow

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(KeyGap),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            rows.forEach { numbers ->
                Row(horizontalArrangement = Arrangement.spacedBy(KeyGap)) {
                    numbers.forEach { number ->
                        NumberButton(
                            number = number,
                            remaining = numberCounts.getOrDefault(number, 0),
                            onClick = onNumberSelected,
                            modifier = Modifier.width(keyWidth)
                        )
                    }
                }
            }
        }
    }
}
