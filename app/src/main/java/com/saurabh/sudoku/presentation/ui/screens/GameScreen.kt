package com.saurabh.sudoku.presentation.ui.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saurabh.sudoku.domain.model.Game
import com.saurabh.sudoku.domain.model.GameState
import com.saurabh.sudoku.presentation.ui.components.*
import com.saurabh.sudoku.presentation.ui.theme.SudokuMotion
import com.saurabh.sudoku.presentation.viewmodel.GameViewModel
import com.saurabh.sudoku.presentation.viewmodel.SettingsViewModel
import com.saurabh.sudoku.presentation.utils.Constants
import com.saurabh.sudoku.presentation.utils.OnLifecycleEvent
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val TAG = "GameScreen"
    Log.d(TAG, "GameScreen composable called with gameId: $gameId")
    val uiState by gameViewModel.uiState.collectAsStateWithLifecycle()
    val highlightErrors by settingsViewModel.highlightErrors.collectAsStateWithLifecycle()
    val notesModeSetting by settingsViewModel.notesModeEnabled.collectAsStateWithLifecycle()
    val soundEnabled by settingsViewModel.soundEnabled.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showResetConfirm by rememberSaveable { mutableStateOf(false) }

    // Sync sound enabled setting into the ViewModel
    LaunchedEffect(soundEnabled) {
        gameViewModel.updateSoundEnabled(soundEnabled)
    }

    // Notes Mode switched OFF in Settings → make sure the ViewModel is not left in notes mode
    LaunchedEffect(notesModeSetting, uiState.isNotesMode) {
        if (!notesModeSetting && uiState.isNotesMode) gameViewModel.toggleNotesMode()
    }

    // Load game on first composition
    LaunchedEffect(gameId) {
        gameViewModel.loadGame(gameId)
    }

    // Hint / error messages → Amber snackbar
    LaunchedEffect(uiState.hintMessage) {
        uiState.hintMessage?.let { msg ->
            Log.d(TAG, "Showing hint/error snackbar: $msg")
            scope.launch {
                snackbarHostState.showSnackbar(SudokuSnackbarVisuals(msg, SudokuSnackbarKind.Warning))
            }
            gameViewModel.dismissHint()
        }
    }

    // Mistake message → Red snackbar
    LaunchedEffect(uiState.showMistakeToast, uiState.mistakeMessage) {
        if (uiState.showMistakeToast && uiState.mistakeMessage != null) {
            val msg = uiState.mistakeMessage!!
            Log.d(TAG, "Showing mistake snackbar: $msg")
            scope.launch {
                snackbarHostState.showSnackbar(SudokuSnackbarVisuals(msg, SudokuSnackbarKind.Error))
            }
            gameViewModel.dismissMistakeToast()
        }
    }

    // Clear animation states after one frame
    LaunchedEffect(uiState.lastPlacedCell) {
        if (uiState.lastPlacedCell != null) {
            kotlinx.coroutines.delay(350.milliseconds)
            gameViewModel.clearLastPlacedCell()
        }
    }
    LaunchedEffect(uiState.lastMistakeCell) {
        if (uiState.lastMistakeCell != null) {
            kotlinx.coroutines.delay(400.milliseconds)
            gameViewModel.clearLastMistakeCell()
        }
    }

    // Auto-pause on background
    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_PAUSE) {
            if (uiState.game?.state == GameState.PLAYING) {
                gameViewModel.onPauseToggle()
            }
        }
    }

    // Loading state
    if (uiState.isLoading) {
        Surface(color = MaterialTheme.colorScheme.background, modifier = modifier.fillMaxSize()) {
            LoadingState(message = "Loading puzzle…")
        }
        return
    }

    // Error handling (unchanged flow: return to previous screen)
    uiState.error?.let {
        LaunchedEffect(it) { onNavigateBack(); gameViewModel.clearError() }
        return
    }

    val game = uiState.game ?: return

    // New game generating
    if (uiState.isGeneratingNewGame) {
        LoadingDialog(message = "Generating new puzzle…")
    }

    val isNotesMode = if (notesModeSetting) uiState.isNotesMode else false
    val isFinished = game.state == GameState.COMPLETED || game.state == GameState.LOST
    val isPaused = game.state == GameState.PAUSED

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.testTag("game_screen").fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SudokuSnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        if (isExpanded) {
                            Text(
                                text = "Sudoku",
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                DifficultyBadge(game.difficulty)
                                GameTimer(elapsedTime = game.elapsedTime)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = gameViewModel::onPauseToggle) {
                            Icon(
                                imageVector = if (isPaused) Icons.Outlined.PlayArrow else Icons.Outlined.Pause,
                                contentDescription = if (isPaused) "Resume game" else "Pause game"
                            )
                        }
                        IconButton(onClick = { showResetConfirm = true }) {
                            Icon(Icons.Outlined.RestartAlt, contentDescription = "Reset board")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                        actionIconContentColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        ) { innerPadding ->
            val board: @Composable (Modifier) -> Unit = { boardModifier ->
                SudokuBoard(
                    board            = game.board,
                    notes            = uiState.notes,
                    selectedCell     = uiState.selectedCell,
                    highlightedCells = uiState.highlightedCells,
                    sameNumberCells  = uiState.sameNumberCells,
                    conflictCells    = uiState.conflictCells,
                    highlightErrors  = highlightErrors,
                    isNotesMode      = isNotesMode,
                    lastPlacedCell   = uiState.lastPlacedCell,
                    lastMistakeCell  = uiState.lastMistakeCell,
                    onCellClick      = gameViewModel::onCellSelected,
                    modifier         = boardModifier
                )
            }
            val toolbar: @Composable () -> Unit = {
                GameToolbar(
                    hintsRemaining    = Constants.MAX_HINTS - game.hintsUsed,
                    isNotesMode       = isNotesMode,
                    notesEnabled      = notesModeSetting,
                    onNotesModeToggle = gameViewModel::toggleNotesMode,
                    onHintClick       = gameViewModel::onHintRequested,
                    onUndoClick       = gameViewModel::onUndoMove,
                    onRedoClick       = gameViewModel::onRedoMove,
                    onEraseClick      = gameViewModel::onEraseSelected,
                    modifier          = Modifier.fillMaxWidth()
                )
            }
            val numberPad: @Composable (Int?) -> Unit = { columns ->
                AnimatedVisibility(
                    visible = !isFinished,
                    enter   = fadeIn(tween(SudokuMotion.NUMBER_PAD_MS)) + expandVertically(tween(SudokuMotion.NUMBER_PAD_MS)),
                    exit    = fadeOut(tween(SudokuMotion.NUMBER_PAD_MS)) + shrinkVertically(tween(SudokuMotion.NUMBER_PAD_MS))
                ) {
                    NumberPad(
                        numberCounts     = uiState.numberCounts,
                        onNumberSelected = gameViewModel::onNumberSelected,
                        columns          = columns,
                        modifier         = Modifier.fillMaxWidth()
                    )
                }
            }

            if (isExpanded) {
                // ── ≥600dp: 2-pane (board 1.2f | control dock 0.8f) ──────────
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Box(
                        modifier = Modifier.weight(1.2f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        board(Modifier.fillMaxHeight())
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.8f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GameHeaderCard(game = game)
                        toolbar()
                        numberPad(3)
                    }
                }
            } else {
                // ── Compact: single pane ─────────────────────────────────────
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MistakeCounter(currentMistakes = game.mistakes, maxMistakes = game.maxMistakes)
                    board(Modifier.fillMaxWidth())
                    toolbar()
                    numberPad(null)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // ── Reset confirmation (U11) ──────────────────────────────────────────
        if (showResetConfirm) {
            DestructiveConfirmDialog(
                title = "Reset board?",
                message = "All your entries and notes will be cleared. Mistakes already made stay counted.",
                confirmLabel = "Reset",
                onConfirm = { showResetConfirm = false; gameViewModel.onResetBoard() },
                onDismiss = { showResetConfirm = false }
            )
        }

        // ── Game Over dialog ──────────────────────────────────────────────────
        if (uiState.showGameOverDialog) {
            Log.d(TAG, "Showing game over dialog for game ${game.id.take(8)}, mistakes: ${game.mistakes}/${game.maxMistakes}")
            GameOverDialog(
                difficulty  = game.difficulty,
                mistakes    = game.mistakes,
                maxMistakes = game.maxMistakes,
                onTryAgain  = { gameViewModel.dismissGameOverDialog(); gameViewModel.onNewGame() },
                onMainMenu  = { gameViewModel.dismissGameOverDialog(); onNavigateBack() }
            )
        }

        // ── Win celebration overlay ───────────────────────────────────────────
        if (uiState.showCompletionDialog && uiState.completionTime != null) {
            Log.d(TAG, "Showing completion dialog — game ${game.id.take(8)}, time: ${uiState.completionTime}, hints: ${game.hintsUsed}")
            GameCompletedDialog(
                difficulty     = game.difficulty,
                completionTime = uiState.completionTime!!,
                hintsUsed      = game.hintsUsed,
                onNewGame      = { gameViewModel.dismissCompletionDialog(); gameViewModel.onNewGame() },
                onMainMenu     = { gameViewModel.dismissCompletionDialog(); onNavigateBack() }
            )
        }
    }
}

/** Right-pane header card (≥600dp): difficulty, timer, mistakes. */
@Composable
private fun GameHeaderCard(game: Game) {
    SudokuCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DifficultyBadge(game.difficulty)
                GameTimer(elapsedTime = game.elapsedTime)
            }
            MistakeCounter(currentMistakes = game.mistakes, maxMistakes = game.maxMistakes)
        }
    }
}
