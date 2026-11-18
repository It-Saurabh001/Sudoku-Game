package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors

/**
 * Mistakes pill (U14): Green safe → Amber one-left → Red at limit, always with icon + text.
 * [showPermanenceHint] adds the U15 cue that strikes survive Undo.
 */
@Composable
fun MistakeCounter(
    currentMistakes: Int,
    maxMistakes: Int,
    modifier: Modifier = Modifier,
    showPermanenceHint: Boolean = true
) {
    val status = LocalSudokuColors.current
    val atLimit = currentMistakes >= maxMistakes
    val nearLimit = !atLimit && currentMistakes > 0 && maxMistakes - currentMistakes == 1
    val (container, content, icon) = when {
        atLimit -> Triple(status.dangerContainer, status.onDangerContainer, Icons.Outlined.ErrorOutline)
        nearLimit -> Triple(status.warningContainer, status.onWarningContainer, Icons.Outlined.WarningAmber)
        else -> Triple(status.successContainer, status.onSuccessContainer, Icons.Outlined.CheckCircle)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = container,
            contentColor = content,
            modifier = Modifier.semantics {
                contentDescription = "Mistakes $currentMistakes of $maxMistakes. Mistakes are permanent, Undo does not remove them."
            }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Mistakes $currentMistakes/$maxMistakes",
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (showPermanenceHint) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Strikes stay even after Undo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
