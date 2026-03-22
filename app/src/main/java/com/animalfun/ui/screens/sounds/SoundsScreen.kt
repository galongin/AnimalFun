package com.animalfun.ui.screens.sounds

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.animalfun.R
import com.animalfun.data.model.Animal
import com.animalfun.ui.components.AnimalCard
import com.animalfun.ui.components.CelebrationOverlay
import com.animalfun.ui.components.ResponsiveGrid
import com.animalfun.ui.theme.CoralRed
import com.animalfun.ui.theme.StarGold
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

/**
 * Root composable for the animal sounds game screen.
 * Supports two modes: EXPLORE (free play) and MATCH (sound identification challenge).
 */
@Composable
fun SoundsScreen(
    windowWidthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    viewModel: SoundsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                // -- Mode toggle --
                ModeToggleRow(
                    currentMode = state.currentMode,
                    onModeChange = viewModel::setMode,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // -- Streak display (MATCH mode) --
                if (state.currentMode == SoundsMode.MATCH) {
                    StreakDisplay(
                        streak = state.streak,
                        bestStreak = state.bestStreak,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // -- Play Sound button --
                    PlaySoundButton(
                        isPlaying = state.isPlaying,
                        onClick = viewModel::playTargetSound,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (state.currentMode == SoundsMode.EXPLORE) {
                    Text(
                        text = stringResource(R.string.tap_animal_sound),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                // -- Animal grid --
                ResponsiveGrid(
                    windowWidthSizeClass = windowWidthSizeClass,
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = state.animals,
                        key = { it.id }
                    ) { animal ->
                        SoundsAnimalCard(
                            animal = animal,
                            isPlaying = state.selectedAnimalId == animal.id && state.isPlaying,
                            isPulsingSelected = state.selectedAnimalId == animal.id && state.currentMode == SoundsMode.EXPLORE,
                            shouldShake = state.shakeAnimalId == animal.id,
                            onShakeComplete = viewModel::clearShake,
                            onClick = { viewModel.onAnimalTapped(animal) }
                        )
                    }
                }
            }
        }

        // Celebration overlay
        AnimatedVisibility(
            visible = state.showCelebration,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CelebrationOverlay(
                onDismiss = {
                    viewModel.dismissCelebration()
                    viewModel.pickNewTarget()
                }
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Mode toggle
// ──────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeToggleRow(
    currentMode: SoundsMode,
    onModeChange: (SoundsMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = currentMode == SoundsMode.EXPLORE,
            onClick = { onModeChange(SoundsMode.EXPLORE) },
            label = {
                Text(
                    text = stringResource(R.string.sounds_mode_explore),
                    fontWeight = if (currentMode == SoundsMode.EXPLORE) FontWeight.Bold else FontWeight.Normal
                )
            }
        )

        Spacer(modifier = Modifier.width(12.dp))

        FilterChip(
            selected = currentMode == SoundsMode.MATCH,
            onClick = { onModeChange(SoundsMode.MATCH) },
            label = {
                Text(
                    text = stringResource(R.string.match_the_sound),
                    fontWeight = if (currentMode == SoundsMode.MATCH) FontWeight.Bold else FontWeight.Normal
                )
            }
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Streak display
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun StreakDisplay(
    streak: Int,
    bestStreak: Int,
    modifier: Modifier = Modifier
) {
    val streakScale by animateFloatAsState(
        targetValue = if (streak > 0) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "streakScale"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Current streak
        Text(
            text = stringResource(R.string.streak, streak),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (streak >= 3) CoralRed else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.scale(streakScale)
        )

        // Fire indicator for streaks >= 3
        if (streak >= 3) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = CoralRed,
                modifier = Modifier
                    .size(28.dp)
                    .scale(streakScale)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Best streak
        Text(
            text = stringResource(R.string.sounds_best_streak, bestStreak),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Play Sound button (MATCH mode)
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun PlaySoundButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulsing animation while sound is playing
    val infiniteTransition = rememberInfiniteTransition(label = "speakerPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val scale = if (isPlaying) pulseScale else 1f

    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        enabled = !isPlaying,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = stringResource(R.string.sounds_play_again_btn),
            modifier = Modifier
                .size(32.dp)
                .scale(scale),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.sounds_play_again_btn),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Animal card with pulsing / shake animations
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun SoundsAnimalCard(
    animal: Animal,
    isPlaying: Boolean,
    isPulsingSelected: Boolean,
    shouldShake: Boolean,
    onShakeComplete: () -> Unit,
    onClick: () -> Unit
) {
    // Pulsing scale animation when this card's sound is playing (EXPLORE mode)
    val infiniteTransition = rememberInfiniteTransition(label = "cardPulse_${animal.id}")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cardPulseScale_${animal.id}"
    )
    val cardScale = if (isPulsingSelected && isPlaying) pulseScale else 1f

    // Shake animation for wrong guess (MATCH mode)
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(shouldShake) {
        if (shouldShake) {
            // Quick left-right shake sequence
            repeat(3) {
                shakeOffset.animateTo(
                    targetValue = 12f,
                    animationSpec = tween(durationMillis = 50)
                )
                shakeOffset.animateTo(
                    targetValue = -12f,
                    animationSpec = tween(durationMillis = 50)
                )
            }
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 50)
            )
            onShakeComplete()
        }
    }

    AnimalCard(
        animal = animal,
        onClick = onClick,
        isSelected = isPlaying || isPulsingSelected,
        modifier = Modifier
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
        overlayContent = if (isPlaying) {
            {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    tint = StarGold,
                    modifier = Modifier.size(32.dp)
                )
            }
        } else null
    )
}
