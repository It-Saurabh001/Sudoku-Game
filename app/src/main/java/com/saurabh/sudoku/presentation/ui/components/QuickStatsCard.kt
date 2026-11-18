package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.domain.model.Statistics

@Composable
fun QuickStatsCard(
    statistics: Statistics,
    onViewAllStats: () -> Unit,
    modifier: Modifier = Modifier
) {
    SudokuCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Progress",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                TextButton(onClick = onViewAllStats) {
                    Text("View All", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Games Completed",
                    value = statistics.gamesCompleted.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Current Streak",
                    value = statistics.currentStreak.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Best Streak",
                    value = statistics.longestStreak.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
