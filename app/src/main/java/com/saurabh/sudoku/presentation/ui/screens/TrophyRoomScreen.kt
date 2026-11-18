package com.saurabh.sudoku.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import com.saurabh.sudoku.presentation.ui.components.LoadingState
import com.saurabh.sudoku.presentation.ui.components.SudokuCard
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saurabh.sudoku.presentation.ui.components.AchievementRow
import com.saurabh.sudoku.presentation.viewmodel.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrophyRoomScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val TAG = "TrophyRoomScreen"
    Log.d(TAG, "TrophyRoomScreen composable called")
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Trophy Room") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val totalTrophies = achievements.size
            val unlockedTrophies = achievements.count { it.isUnlocked }
            val progressPercent = if (totalTrophies > 0) (unlockedTrophies * 100) / totalTrophies else 0

            // 1. Premium Summary Card with Circular Progress
            SudokuCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Trophy Status",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        val encouragementText = when {
                            progressPercent == 100 -> "Absolute Legend! You've conquered all challenges!"
                            progressPercent >= 75 -> "You're a Sudoku Master! Almost there!"
                            progressPercent >= 50 -> "Outstanding progress! Keep it up!"
                            progressPercent >= 25 -> "Great start! Trophies are piling up!"
                            else -> "Start solving games to unlock premium badges!"
                        }
                        
                        Text(
                            text = encouragementText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = "$unlockedTrophies of $totalTrophies Trophies Unlocked",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Circular Progress Layout
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(80.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { unlockedTrophies.toFloat() / maxOf(totalTrophies, 1).toFloat() },
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            strokeWidth = 8.dp,
                            color = LocalSudokuColors.current.trophyUnlocked,
                            trackColor = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "$progressPercent%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 2. Achievements List Section Header
            Text(
                text = "Trophies List",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // 3. Grid of Achievements
            if (achievements.isEmpty()) {
                LoadingState(
                    message = "Loading trophies…",
                    modifier = Modifier.heightIn(min = 160.dp)
                )
            } else {
                achievements.forEach { achievement ->
                    AchievementRow(achievement = achievement)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
