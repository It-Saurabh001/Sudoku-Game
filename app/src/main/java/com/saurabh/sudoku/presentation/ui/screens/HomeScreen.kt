package com.saurabh.sudoku.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saurabh.sudoku.R
import com.saurabh.sudoku.domain.model.GameState
import com.saurabh.sudoku.presentation.ui.components.ContinueGameCard
import com.saurabh.sudoku.presentation.ui.components.DestructiveConfirmDialog
import com.saurabh.sudoku.presentation.ui.components.ErrorState
import com.saurabh.sudoku.presentation.ui.components.LoadingDialog
import com.saurabh.sudoku.presentation.ui.components.NewGameSection
import com.saurabh.sudoku.presentation.ui.components.QuickStatsCard
import com.saurabh.sudoku.presentation.ui.components.SecondaryButton
import com.saurabh.sudoku.presentation.ui.components.SudokuCard
import com.saurabh.sudoku.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToGame: (String) -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToTrophyRoom: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val TAG = "HomeScreen"
    Log.d(TAG, "HomeScreen composable called")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentGame by viewModel.currentGame.collectAsStateWithLifecycle()
    val statistics by viewModel.statistics.collectAsStateWithLifecycle()
    var showNewGameConfirm by rememberSaveable { mutableStateOf(false) }

    // Handle navigation
    LaunchedEffect(uiState.navigateToGame, uiState.generatedGameId) {
        if (uiState.navigateToGame && uiState.generatedGameId != null) {
            Log.d(TAG, "Navigating to game: ${uiState.generatedGameId}")
            onNavigateToGame(uiState.generatedGameId!!)
            viewModel.onNavigatedToGame()
        }
    }

    // Show loading dialog
    if (uiState.isGeneratingPuzzle) {
        LoadingDialog(message = "Generating new puzzle...")
    }

    val activeGame = currentGame?.takeIf { it.state != GameState.COMPLETED }
    // An unfinished (playing / paused) game would be replaced → confirm first (U11)
    val hasResumableGame = activeGame != null && activeGame.state != GameState.LOST

    Scaffold(
        modifier = modifier.testTag("home_screen").fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Brand header — logo shown as-is (no glow / gradient, U7)
            Column(
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.sudoku_icon),
                    contentDescription = "Sudoku logo",
                    modifier = Modifier.size(96.dp)
                )
                Text(
                    text = "Sudoku",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1
                )
            }

            // Continue Game Section (owns the primary CTA when present)
            activeGame?.let { game ->
                ContinueGameCard(
                    game = game,
                    onContinueClick = { viewModel.continueCurrentGame() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Generation error → Retry (U10)
            uiState.error?.let { message ->
                SudokuCard(modifier = Modifier.fillMaxWidth()) {
                    ErrorState(
                        message = message,
                        onRetry = { viewModel.clearError(); viewModel.startNewGame() }
                    )
                }
            }

            // New Game Section
            NewGameSection(
                selectedDifficulty = uiState.selectedDifficulty,
                onDifficultySelected = viewModel::onDifficultySelected,
                onStartNewGame = {
                    if (hasResumableGame) showNewGameConfirm = true else viewModel.startNewGame()
                },
                isLoading = uiState.isGeneratingPuzzle,
                isPrimaryAction = activeGame == null,
                modifier = Modifier.fillMaxWidth()
            )

            // Quick Stats
            QuickStatsCard(
                statistics = statistics,
                onViewAllStats = onNavigateToStatistics,
                modifier = Modifier.fillMaxWidth()
            )

            // Trophy Room Button
            SecondaryButton(
                onClick = onNavigateToTrophyRoom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.EmojiEvents, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.width(8.dp))
                Text("Trophy Room", style = MaterialTheme.typography.labelLarge, maxLines = 1)
            }

            // Settings Button
            SecondaryButton(
                onClick = onNavigateToSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.width(8.dp))
                Text("Settings", style = MaterialTheme.typography.labelLarge, maxLines = 1)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showNewGameConfirm) {
        DestructiveConfirmDialog(
            title = "Start a new game?",
            message = "Your current game in progress will be replaced.",
            confirmLabel = "Start New",
            onConfirm = { showNewGameConfirm = false; viewModel.startNewGame() },
            onDismiss = { showNewGameConfirm = false }
        )
    }
}
