package com.animalfun.ui.screens.memory

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.animalfun.R
import com.animalfun.ui.components.CelebrationOverlay
import com.animalfun.ui.theme.GrassGreen
import com.animalfun.ui.theme.LavenderPurple
import com.animalfun.ui.theme.OceanBlue
import com.animalfun.ui.theme.PeachOrange
import com.animalfun.ui.theme.StarGold
import com.animalfun.ui.theme.SunshineYellow
import org.koin.androidx.compose.koinViewModel

/**
 * Memory game screen — entry point.
 */
@Composable
fun MemoryScreen(
    windowWidthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    onNavigateHome: () -> Unit,
    viewModel: MemoryViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    when (state.screen) {
        MemoryScreenState.SETUP -> {
            MemorySetupScreen(
                state = state,
                onDifficultySelected = viewModel::setDifficulty,
                onMatchTypeSelected = viewModel::setMatchType,
                onStartGame = viewModel::startGame,
                onNavigateHome = onNavigateHome
            )
        }
        MemoryScreenState.PLAYING, MemoryScreenState.COMPLETE -> {
            MemoryPlayScreen(
                state = state,
                windowWidthSizeClass = windowWidthSizeClass,
                onCardFlip = viewModel::flipCard,
                onPlayAgain = viewModel::playAgain,
                onNavigateHome = onNavigateHome,
                onDismissCelebration = viewModel::dismissCelebration,
                formatTime = viewModel::formatTime
            )
        }
    }
}

// ────────────────────────────────────────────────────────────────────────────
// Setup Screen
// ────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemorySetupScreen(
    state: MemoryUiState,
    onDifficultySelected: (MemoryDifficulty) -> Unit,
    onMatchTypeSelected: (MatchType) -> Unit,
    onStartGame: () -> Unit,
    onNavigateHome: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.memory_game),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Difficulty selection
            Text(
                text = stringResource(R.string.memory_choose_difficulty),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MemoryDifficulty.entries.forEach { difficulty ->
                    val label = when (difficulty) {
                        MemoryDifficulty.EASY -> stringResource(R.string.difficulty_easy)
                        MemoryDifficulty.MEDIUM -> stringResource(R.string.difficulty_medium)
                        MemoryDifficulty.HARD -> stringResource(R.string.difficulty_hard)
                    }
                    val pairsText = stringResource(
                        R.string.memory_pairs_count,
                        difficulty.rows * difficulty.columns / 2
                    )
                    FilterChip(
                        selected = state.difficulty == difficulty,
                        onClick = { onDifficultySelected(difficulty) },
                        label = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = label, fontWeight = FontWeight.Bold)
                                Text(
                                    text = pairsText,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanBlue.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.height(56.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Match type selection
            Text(
                text = stringResource(R.string.memory_match_type),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = state.matchType == MatchType.IMAGE_IMAGE,
                    onClick = { onMatchTypeSelected(MatchType.IMAGE_IMAGE) },
                    label = {
                        Text(
                            text = stringResource(R.string.memory_image_image),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LavenderPurple.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.height(48.dp)
                )
                FilterChip(
                    selected = state.matchType == MatchType.IMAGE_NAME,
                    onClick = { onMatchTypeSelected(MatchType.IMAGE_NAME) },
                    label = {
                        Text(
                            text = stringResource(R.string.memory_image_name),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LavenderPurple.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.height(48.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Start button
            Button(
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GrassGreen
                )
            ) {
                Text(
                    text = stringResource(R.string.memory_start),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ────────────────────────────────────────────────────────────────────────────
// Play Screen
// ────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoryPlayScreen(
    state: MemoryUiState,
    windowWidthSizeClass: WindowWidthSizeClass,
    onCardFlip: (Int) -> Unit,
    onPlayAgain: () -> Unit,
    onNavigateHome: () -> Unit,
    onDismissCelebration: () -> Unit,
    formatTime: (Long) -> String
) {
    val columns = when (windowWidthSizeClass) {
        WindowWidthSizeClass.Expanded -> state.difficulty.columns + 1
        WindowWidthSizeClass.Medium -> state.difficulty.columns
        else -> state.difficulty.columns
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Timer
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = null,
                            tint = StarGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.time_elapsed,
                                formatTime(state.timer)
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Moves
                        Text(
                            text = stringResource(R.string.moves, state.moves),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(
                    items = state.cards,
                    key = { _, card -> card.id }
                ) { index, card ->
                    MemoryCardItem(
                        card = card,
                        onClick = { onCardFlip(index) }
                    )
                }
            }

            // Completion overlay
            if (state.isComplete) {
                CompletionOverlay(
                    state = state,
                    formatTime = formatTime,
                    onPlayAgain = onPlayAgain,
                    onNavigateHome = onNavigateHome
                )
            }

            // Celebration animation
            if (state.showCelebration) {
                CelebrationOverlay(onDismiss = onDismissCelebration)
            }
        }
    }
}

// ────────────────────────────────────────────────────────────────────────────
// Memory Card with flip animation
// ────────────────────────────────────────────────────────────────────────────

@Composable
private fun MemoryCardItem(
    card: MemoryCard,
    onClick: () -> Unit
) {
    val isFaceUp = card.isFlipped || card.isMatched

    // Animate rotation around Y axis: 0 = back, 180 = front
    val rotation by animateFloatAsState(
        targetValue = if (isFaceUp) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "cardFlip"
    )

    // Determine which side is showing (past 90 degrees = show front)
    val showFront = rotation > 90f

    Card(
        modifier = Modifier
            .aspectRatio(0.75f)
            .graphicsLayer {
                rotationY = rotation
                // Prevent mirrored text/image on front side
                cameraDistance = 12f * density
            }
            .clickable(enabled = !card.isFlipped && !card.isMatched) { onClick() }
            .then(
                if (card.isMatched) Modifier.alpha(0.7f) else Modifier
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = if (card.isMatched) {
            BorderStroke(2.dp, GrassGreen)
        } else {
            null
        },
        colors = CardDefaults.cardColors(
            containerColor = if (showFront) {
                MaterialTheme.colorScheme.surface
            } else {
                OceanBlue
            }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (showFront) {
                // Front side — counter-rotate so content is not mirrored
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { rotationY = 180f },
                    contentAlignment = Alignment.Center
                ) {
                    CardFrontContent(card = card)
                }
            } else {
                // Back side — question mark pattern
                CardBackContent()
            }
        }
    }
}

@Composable
private fun CardFrontContent(card: MemoryCard) {
    when (card.content) {
        MemoryCardContent.IMAGE -> {
            val context = LocalContext.current
            val imageResId = card.imageRes?.let {
                context.resources.getIdentifier(it, "drawable", context.packageName)
            } ?: 0

            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = card.displayText ?: stringResource(R.string.memory_card_desc),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    text = "?",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        MemoryCardContent.NAME -> {
            Text(
                text = card.displayText ?: "",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun CardBackContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "?",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

// ────────────────────────────────────────────────────────────────────────────
// Completion Overlay
// ────────────────────────────────────────────────────────────────────────────

@Composable
private fun CompletionOverlay(
    state: MemoryUiState,
    formatTime: (Long) -> String,
    onPlayAgain: () -> Unit,
    onNavigateHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.congratulations),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = StarGold
                )

                // Stats
                Text(
                    text = stringResource(
                        R.string.memory_completion_time,
                        formatTime(state.timer)
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(
                        R.string.memory_completion_moves,
                        state.moves
                    ),
                    style = MaterialTheme.typography.titleMedium
                )

                state.bestTime?.let { best ->
                    Text(
                        text = stringResource(
                            R.string.memory_best_time,
                            formatTime(best)
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrassGreen
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Play Again button
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.play_again),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                // Home button
                Button(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PeachOrange
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.memory_home),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
