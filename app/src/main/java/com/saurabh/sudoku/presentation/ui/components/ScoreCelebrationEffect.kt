package com.saurabh.sudoku.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.saurabh.sudoku.R
import com.saurabh.sudoku.presentation.ui.theme.LocalReduceMotion
import com.saurabh.sudoku.presentation.ui.theme.LocalSudokuColors
import com.saurabh.sudoku.presentation.ui.theme.SudokuTheme

/**
 * Plays the trophy-confetti Lottie animation.
 * With Reduce Motion ON (in-app toggle or system animator scale 0) a static trophy badge is shown.
 *
 * @param modifier   Size/placement — caller decides the exact bounds.
 * @param iterations How many times to play. [LottieConstants.IterateForever] loops indefinitely.
 *                   Pass `1` for a single celebratory burst.
 */
@Composable
fun ScoreCelebrationEffect(
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever
) {
    if (LocalReduceMotion.current) {
        StaticTrophyBadge(modifier)
        return
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.trophy_confetti)
    )

    LottieAnimation(
        composition = composition,
        iterations   = iterations,
        modifier     = modifier
    )
}

@Composable
private fun StaticTrophyBadge(modifier: Modifier = Modifier) {
    val status = LocalSudokuColors.current
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
            contentColor = status.success,
            modifier = Modifier.size(104.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = "Trophy",
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 200)
@Composable
private fun ScoreCelebrationEffectPreview() {
    SudokuTheme { ScoreCelebrationEffect() }
}
