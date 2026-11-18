package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.domain.model.Statistics

@Composable
fun AchievementStatsCard(
    statistics: Statistics,
    modifier: Modifier = Modifier
) {
    SudokuCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Achievements",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard(
                    title = "Current Streak",
                    value = "${statistics.currentStreak}",  // Sirf number, 🔥 nahi
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Longest Streak",
                    value = "${statistics.longestStreak}",  // Sirf number, 🏆 nahi
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}