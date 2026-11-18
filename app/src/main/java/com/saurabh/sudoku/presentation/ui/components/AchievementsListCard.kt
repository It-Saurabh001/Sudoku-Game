package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saurabh.sudoku.domain.model.Achievement
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors

@Composable
fun AchievementsListCard(
    achievements: List<Achievement>,
    modifier: Modifier = Modifier
) {
    SudokuCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Trophy Room",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.weight(1f))
                val unlockedCount = achievements.count { it.isUnlocked }
                Text(
                    text = "$unlockedCount / ${achievements.size} Unlocked",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                achievements.forEach { achievement ->
                    AchievementRow(achievement = achievement)
                }
            }
        }
    }
}

@Composable
fun AchievementRow(
    achievement: Achievement,
    modifier: Modifier = Modifier
) {
    val status = LocalSudokuColors.current
    // Unlocked: surface card + Blue accent. Locked: SurfaceSoft + Muted (color guide §4).
    val backgroundColor = if (achievement.isUnlocked) MaterialTheme.colorScheme.surface else status.trophyLocked
    val borderColor = MaterialTheme.colorScheme.outline
    val accent = if (achievement.isUnlocked) status.trophyUnlocked else status.onTrophyLocked

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.medium
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Badge: filled trophy when unlocked, lock when locked (never colour alone)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (achievement.isUnlocked)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
        ) {
            Icon(
                imageVector = if (achievement.isUnlocked) Icons.Filled.EmojiEvents else Icons.Default.Lock,
                contentDescription = if (achievement.isUnlocked) "Unlocked" else "Locked",
                tint = accent,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Info and Progress
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = achievement.title,
                style = MaterialTheme.typography.titleMedium,
                color = if (achievement.isUnlocked)
                    MaterialTheme.colorScheme.onSurface
                else
                    status.onTrophyLocked,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = achievement.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Progress bar if needed
            if (achievement.displayProgress && !achievement.isUnlocked && achievement.maxProgress > 0) {
                val progress = achievement.currentProgress.toFloat() / achievement.maxProgress.toFloat()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outline
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${achievement.currentProgress} / ${achievement.maxProgress}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
