package com.saurabh.sudoku.presentation.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.saurabh.sudoku.domain.model.Difficulty

/**
 * App-specific semantic tokens (docs/ui_rules.md U5, U14, U15).
 * Board cues (Cyan / Red / Green / Amber) are fixed and never come from dynamic colour.
 */
@Immutable
data class SudokuStatusColors(
    // ── Board cells ──────────────────────────────────────────────────────────
    val cellNormal: Color,
    val cellPressed: Color,
    val cellPeer: Color,
    val cellSameNumber: Color,
    val cellSelected: Color,
    val cellSelectedPressed: Color,
    val cellSelectedBorder: Color,
    val cellConflict: Color,
    val onCellConflict: Color,
    val cellCorrect: Color,
    val onCellCorrect: Color,
    val cellHint: Color,
    val onCellHint: Color,
    val gridThin: Color,
    val gridThick: Color,
    val digitGiven: Color,
    val digitUser: Color,
    val digitHighlighted: Color,
    val note: Color,
    val noteActive: Color,

    // ── Touch shades (Golden Rule: same family, darker) ──────────────────────
    val primaryPressed: Color,
    val primaryHeld: Color,
    val secondaryPressed: Color,
    val secondaryHeld: Color,

    // ── Chips ────────────────────────────────────────────────────────────────
    val chipSelected: Color,
    val onChipSelected: Color,
    val chipSelectedPressed: Color,
    val chipSelectedHeld: Color,
    val chipUnselected: Color,
    val onChipUnselected: Color,

    // ── Action toolbar / NumberPad ───────────────────────────────────────────
    val toolTile: Color,
    val toolTilePressed: Color,
    val toolTileHeld: Color,
    val onToolTile: Color,
    val notesOn: Color,
    val onNotesOn: Color,
    val hintTile: Color,
    val hintTilePressed: Color,
    val hintTileHeld: Color,
    val onHintTile: Color,
    val key: Color,
    val keyBorder: Color,
    val onKey: Color,
    val keyBadge: Color,
    val disabledFill: Color,
    val disabledContent: Color,

    // ── Semantic feedback ────────────────────────────────────────────────────
    val success: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val danger: Color,
    val dangerContainer: Color,
    val onDangerContainer: Color,
    val dangerPressed: Color,
    val dangerHeld: Color,
    val special: Color,
    val specialContainer: Color,
    val onSpecialContainer: Color,

    // ── Trophy room ──────────────────────────────────────────────────────────
    val trophyUnlocked: Color,
    val trophyLocked: Color,
    val onTrophyLocked: Color,

    // ── Difficulty chips (U14) ───────────────────────────────────────────────
    val easy: Color,
    val onEasy: Color,
    val medium: Color,
    val onMedium: Color,
    val hard: Color,
    val onHard: Color,
    val expert: Color,
    val onExpert: Color,

    val scrim: Color,
) {
    /** Container + content colour pair for a difficulty badge. */
    fun difficulty(difficulty: Difficulty): Pair<Color, Color> = when (difficulty) {
        Difficulty.EASY -> easy to onEasy
        Difficulty.MEDIUM -> medium to onMedium
        Difficulty.HARD -> hard to onHard
        Difficulty.EXPERT -> expert to onExpert
    }
}

val LightSudokuStatusColors = SudokuStatusColors(
    cellNormal = White,
    cellPressed = Cyan50,
    cellPeer = Blue50,
    cellSameNumber = Blue100,
    cellSelected = Cyan100,
    cellSelectedPressed = Cyan200,
    cellSelectedBorder = Cyan500,
    cellConflict = RedLight,
    onCellConflict = RedStrong,
    cellCorrect = GreenLight,
    onCellCorrect = GreenStrong,
    cellHint = AmberLight,
    onCellHint = AmberStrong,
    gridThin = Navy200,
    gridThick = Navy600,
    digitGiven = Navy600,
    digitUser = Blue700,
    digitHighlighted = Blue900,
    note = Muted,
    noteActive = Blue600,

    primaryPressed = Blue700,
    primaryHeld = Blue800,
    secondaryPressed = Blue50,
    secondaryHeld = Blue100,

    chipSelected = Blue100,
    onChipSelected = Blue900,
    chipSelectedPressed = Blue200,
    chipSelectedHeld = Blue300,
    chipUnselected = SurfaceSoft,
    onChipUnselected = Muted,

    toolTile = SurfaceSoft,
    toolTilePressed = FieldFill,
    toolTileHeld = Navy100,
    onToolTile = Navy600,
    notesOn = Blue100,
    onNotesOn = Blue600,
    hintTile = AmberLight,
    hintTilePressed = AmberLightPressed,
    hintTileHeld = AmberLightHeld,
    onHintTile = AmberStrong,
    key = White,
    keyBorder = Outline,
    onKey = Blue700,
    keyBadge = Muted,
    disabledFill = SurfaceSoft,
    disabledContent = Disabled,

    success = Green,
    successContainer = GreenLight,
    onSuccessContainer = GreenStrong,
    warning = Amber,
    warningContainer = AmberLight,
    onWarningContainer = AmberStrong,
    danger = Red,
    dangerContainer = RedLight,
    onDangerContainer = RedStrong,
    dangerPressed = RedLight,
    dangerHeld = RedLightHeld,
    special = Violet,
    specialContainer = VioletLight,
    onSpecialContainer = VioletStrong,

    trophyUnlocked = Blue600,
    trophyLocked = SurfaceSoft,
    onTrophyLocked = Muted,

    easy = GreenLight,
    onEasy = GreenStrong,
    medium = Blue100,
    onMedium = Blue900,
    hard = AmberLight,
    onHard = AmberStrong,
    expert = VioletLight,
    onExpert = VioletStrong,

    scrim = Scrim,
)

val DarkSudokuStatusColors = SudokuStatusColors(
    cellNormal = DarkSurface,
    cellPressed = DarkPeer,
    cellPeer = DarkPeer,
    cellSameNumber = DarkSameNumber,
    cellSelected = DarkCyanSurface,
    cellSelectedPressed = DarkCyanPressed,
    cellSelectedBorder = Cyan400,
    cellConflict = DarkRedContainer,
    onCellConflict = DarkRedText,
    cellCorrect = DarkGreenContainer,
    onCellCorrect = DarkGreenText,
    cellHint = DarkAmberContainer,
    onCellHint = DarkAmberText,
    gridThin = DarkOutline,
    gridThick = Navy200,
    digitGiven = Navy50,
    digitUser = Blue300,
    digitHighlighted = Cyan100,
    note = DarkMuted,
    noteActive = Blue300,

    primaryPressed = Blue400,
    primaryHeld = Blue500,
    secondaryPressed = DarkSurfaceSoft,
    secondaryHeld = DarkFieldFill,

    chipSelected = DarkSameNumber,
    onChipSelected = Blue100,
    chipSelectedPressed = DarkBlueHeld,
    chipSelectedHeld = Blue800,
    chipUnselected = DarkSurfaceSoft,
    onChipUnselected = DarkMuted,

    toolTile = DarkSurfaceSoft,
    toolTilePressed = DarkFieldFill,
    toolTileHeld = DarkOutline,
    onToolTile = Navy100,
    notesOn = DarkSameNumber,
    onNotesOn = Blue300,
    hintTile = DarkAmberContainer,
    hintTilePressed = AmberStrong,
    hintTileHeld = Amber,
    onHintTile = DarkAmberText,
    key = DarkSurface,
    keyBorder = DarkOutline,
    onKey = Blue300,
    keyBadge = DarkMuted,
    disabledFill = DarkSurfaceSoft,
    disabledContent = DarkDisabled,

    success = Green,
    successContainer = DarkGreenContainer,
    onSuccessContainer = DarkGreenText,
    warning = Amber,
    warningContainer = DarkAmberContainer,
    onWarningContainer = DarkAmberText,
    danger = DarkRedText,
    dangerContainer = DarkRedContainer,
    onDangerContainer = DarkRedText,
    dangerPressed = DarkRedContainer,
    dangerHeld = RedStrong,
    special = VioletLight,
    specialContainer = DarkVioletContainer,
    onSpecialContainer = DarkVioletText,

    trophyUnlocked = Blue300,
    trophyLocked = DarkSurfaceSoft,
    onTrophyLocked = DarkMuted,

    easy = DarkGreenContainer,
    onEasy = DarkGreenText,
    medium = DarkSameNumber,
    onMedium = Blue100,
    hard = DarkAmberContainer,
    onHard = DarkAmberText,
    expert = DarkVioletContainer,
    onExpert = DarkVioletText,

    scrim = Scrim,
)

val LocalSudokuColors = staticCompositionLocalOf { LightSudokuStatusColors }
