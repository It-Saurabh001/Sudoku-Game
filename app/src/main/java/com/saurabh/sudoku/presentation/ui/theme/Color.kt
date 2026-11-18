package com.saurabh.sudoku.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// Sudoku Modern colour system (docs/ui_rules.md Part 2).
// Raw colours live ONLY in this file. Screens use MaterialTheme.colorScheme.*
// or LocalSudokuColors (SudokuStatusColors).
// ─────────────────────────────────────────────────────────────────────────────

// Brand Blue — primary & user input
val Blue50 = Color(0xFFEFF5FF)
val Blue100 = Color(0xFFDCE9FF)
val Blue200 = Color(0xFFBBD3FF)
val Blue300 = Color(0xFF8FB5FF)
val Blue400 = Color(0xFF5E91F5)
val Blue500 = Color(0xFF3975ED)
val Blue600 = Color(0xFF2563EB)
val Blue700 = Color(0xFF1D4FC2)
val Blue800 = Color(0xFF173F9E)
val Blue900 = Color(0xFF123477)

// Navy — board grid, clues & structure
val Navy50 = Color(0xFFF1F4FA)
val Navy100 = Color(0xFFDDE4F1)
val Navy200 = Color(0xFFB8C6DF)
val Navy300 = Color(0xFF879BBC)
val Navy400 = Color(0xFF536F9D)
val Navy500 = Color(0xFF294B83)
val Navy600 = Color(0xFF0B2F7A)
val Navy700 = Color(0xFF082762)
val Navy800 = Color(0xFF061E4D)
val Navy900 = Color(0xFF041735)

// Cyan — cell selection & focus
val Cyan50 = Color(0xFFF0FBFF)
val Cyan100 = Color(0xFFDDF6FF)
val Cyan200 = Color(0xFFBDEBFF)
val Cyan300 = Color(0xFF8EDCFF)
val Cyan400 = Color(0xFF5BC9F5)
val Cyan500 = Color(0xFF28B5E8)
val Cyan600 = Color(0xFF1498CC)
val Cyan700 = Color(0xFF087AA8)
val Cyan800 = Color(0xFF075F83)
val Cyan900 = Color(0xFF06465F)

// Semantic families (game feedback only): base / light container / strong text
val Green = Color(0xFF16A36A)
val GreenLight = Color(0xFFDDF7EA)
val GreenStrong = Color(0xFF08613D)

val Amber = Color(0xFFE59A18)
val AmberLight = Color(0xFFFFF2D6)
val AmberStrong = Color(0xFF805000)
val AmberLightPressed = Color(0xFFFFE6B3)   // Amber family press step
val AmberLightHeld = Color(0xFFFFD98C)      // Amber family hold step

val Red = Color(0xFFD94343)
val RedLight = Color(0xFFFDE7E7)
val RedStrong = Color(0xFF982B2B)
val RedLightHeld = Color(0xFFF9CFCF)        // Red family hold step

val Violet = Color(0xFF7657D6)
val VioletLight = Color(0xFFEEE9FF)
val VioletStrong = Color(0xFF49339A)

// Neutral family
val White = Color(0xFFFFFFFF)
val OffWhite = Color(0xFFF8FAFF)
val SurfaceSoft = Color(0xFFF1F5FC)
val FieldFill = Color(0xFFEAF0FA)
val Outline = Color(0xFFD6DFEF)
val OutlineStrong = Color(0xFFAAB9D2)
val Ink = Color(0xFF14213D)
val Muted = Color(0xFF66758F)
val Disabled = Color(0xFF9AA7BA)
val Scrim = Color(0xFF101A2B)

// ── Deliberate dark-mode tokens (no pure black) ──────────────────────────────
val DarkSurface = Color(0xFF0B2147)          // cards, cells, bars
val DarkSurfaceSoft = Color(0xFF122C59)      // tool tiles, secondary cards
val DarkFieldFill = Color(0xFF183566)        // container high
val DarkOutline = Color(0xFF2B4A7E)          // card / chip borders
val DarkMuted = Color(0xFF9AACC9)            // secondary text, notes
val DarkDisabled = Color(0xFF5F7398)         // disabled content
val DarkCyanSurface = Color(0xFF0E3F5C)      // selected cell (cyan-tinted)
val DarkCyanPressed = Color(0xFF135273)
val DarkPeer = Color(0xFF102A55)             // row/col/box peers
val DarkSameNumber = Color(0xFF1A3D78)       // same digit
val DarkBlueHeld = Color(0xFF23477F)
val DarkGreenContainer = Color(0xFF0F3B2B)
val DarkGreenText = Color(0xFF8EE6BC)
val DarkAmberContainer = Color(0xFF4A3510)
val DarkAmberText = Color(0xFFFFD48A)
val DarkRedContainer = Color(0xFF4F1C22)
val DarkRedText = Color(0xFFFFB3B3)
val DarkRedPressed = Color(0xFFFF8F8F)
val DarkVioletContainer = Color(0xFF2F2463)
val DarkVioletText = Color(0xFFCFC2FF)
