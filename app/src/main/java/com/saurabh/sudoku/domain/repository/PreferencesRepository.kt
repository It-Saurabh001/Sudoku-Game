package com.saurabh.sudoku.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val themeMode: Flow<String>
    val soundEnabled: Flow<Boolean>
    val autoNotes: Flow<Boolean>
    val showHints: Flow<Boolean>
    val vibrationEnabled: Flow<Boolean>
    val highlightErrors: Flow<Boolean>
    val autoSave: Flow<Boolean>
    val notesModeEnabled: Flow<Boolean>
    /** In-app Reduce Motion toggle (UI animations only). Default keeps existing fakes compiling. */
    val reduceMotion: Flow<Boolean> get() = kotlinx.coroutines.flow.flowOf(false)

    suspend fun updateThemeMode(mode: String)
    suspend fun updateSoundEnabled(enabled: Boolean)
    suspend fun updateAutoNotes(enabled: Boolean)
    suspend fun updateShowHints(enabled: Boolean)
    suspend fun updateVibrationEnabled(enabled: Boolean)
    suspend fun updateHighlightErrors(enabled: Boolean)
    suspend fun updateAutoSave(enabled: Boolean)
    suspend fun updateNotesModeEnabled(enabled: Boolean)
    suspend fun updateReduceMotion(enabled: Boolean) {}
}