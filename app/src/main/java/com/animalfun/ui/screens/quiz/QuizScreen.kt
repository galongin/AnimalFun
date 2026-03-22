package com.animalfun.ui.screens.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.animalfun.R
import com.animalfun.ui.components.AnimalCard
import com.animalfun.ui.components.CelebrationOverlay
import com.animalfun.ui.components.QuizOptionButton
import com.animalfun.ui.components.QuizOptionState
import com.animalfun.ui.theme.StarGold
import org.koin.androidx.compose.koinViewModel

/**
 * Root quiz screen composable that delegates to the appropriate sub-screen
 * based on [QuizUiState.screen].
 */
@Composable
fun QuizScreen(
    onNavigateHome: () -> Unit = {},
    viewModel: QuizViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (state.screen) {
            QuizScreenState.TYPE_SELECTION -> {
                QuizTypeSelectionScreen(
                    difficulty = state.difficulty,
                    onDifficultyChange = viewModel::setDifficulty,
                    onQuizTypeSelected = viewModel::startQuiz
                )
            }

            QuizScreenState.GAME_PLAY -> {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    QuizGamePlayScreen(
                        state = state,
                        onAnswerSelected = viewModel::selectAnswer,
                        onPlaySound = viewModel::playCurrentAnimalSound,
                        onBack = viewModel::backToTypeSelection
                    )
                }
            }

            QuizScreenState.COMPLETE -> {
                QuizCompleteScreen(
                    score = state.score,
                    totalQuestions = state.totalQuestions,
                    starsEarned = state.starsEarned,
                    onPlayAgain = viewModel::playAgain,
                    onHome = {
                        viewModel.backToTypeSelection()
                        onNavigateHome()
                    },
                    onBackToTypes = viewModel::backToTypeSelection
                )
            }
        }

        // Celebration overlay
        AnimatedVisibility(
            visible = state.showCelebration,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CelebrationOverlay(
                onDismiss = { /* auto-advances via ViewModel delay */ }
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Sub-screen: Quiz Type Selection
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun QuizTypeSelectionScreen(
    difficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    onQuizTypeSelected: (QuizType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.quiz_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.quiz_choose_type),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Difficulty selector
        DifficultySelector(
            selected = difficulty,
            onSelect = onDifficultyChange,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Quiz type grid (2 columns)
        QuizTypeGrid(onQuizTypeSelected = onQuizTypeSelected)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DifficultySelector(
    selected: Difficulty,
    onSelect: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.quiz_difficulty_label),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Difficulty.entries.forEach { difficulty ->
                val label = when (difficulty) {
                    Difficulty.EASY -> stringResource(R.string.difficulty_easy)
                    Difficulty.MEDIUM -> stringResource(R.string.difficulty_medium)
                    Difficulty.HARD -> stringResource(R.string.difficulty_hard)
                }

                FilterChip(
                    selected = selected == difficulty,
                    onClick = { onSelect(difficulty) },
                    label = {
                        Text(
                            text = label,
                            fontWeight = if (selected == difficulty) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun QuizTypeGrid(
    onQuizTypeSelected: (QuizType) -> Unit
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Display in rows of 2
        QuizType.entries.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { quizType ->
                    QuizTypeCard(
                        quizType = quizType,
                        onClick = { onQuizTypeSelected(quizType) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remaining space if odd number
                if (row.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizTypeCard(
    quizType: QuizType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val iconResId = quizType.iconResId
            if (iconResId != 0) {
                val iconExists = try {
                    context.resources.getResourceName(iconResId)
                    true
                } catch (_: Exception) {
                    false
                }
                if (iconExists) {
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // Fallback icon placeholder
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(quizType.displayNameResId),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Sub-screen: Quiz Game Play
// ──────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizGamePlayScreen(
    state: QuizUiState,
    onAnswerSelected: (Int) -> Unit,
    onPlaySound: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar with back and score
        TopAppBar(
            title = {
                Text(
                    text = stringResource(
                        R.string.quiz_progress,
                        state.currentQuestion + 1,
                        state.totalQuestions
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            },
            actions = {
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = stringResource(R.string.quiz_score, state.score),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = StarGold,
                            modifier = Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                )
            }
        )

        // Progress bar
        LinearProgressIndicator(
            progress = if (state.totalQuestions > 0) {
                (state.currentQuestion + 1).toFloat() / state.totalQuestions
            } else 0f,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question area + options
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Question content (varies by quiz type)
            QuizQuestionContent(
                state = state,
                onPlaySound = onPlaySound
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Answer options
            if (state.quizType == QuizType.REVERSE) {
                ReverseQuizOptions(
                    options = state.options,
                    selectedIndex = state.selectedAnswerIndex,
                    onSelect = onAnswerSelected
                )
            } else {
                TextQuizOptions(
                    options = state.options,
                    selectedIndex = state.selectedAnswerIndex,
                    onSelect = onAnswerSelected
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuizQuestionContent(
    state: QuizUiState,
    onPlaySound: () -> Unit
) {
    val context = LocalContext.current

    when (state.quizType) {
        QuizType.PICTURE -> {
            // Show animal image
            val animal = state.currentAnimal
            if (animal != null) {
                val imageResId = context.resources.getIdentifier(
                    animal.imageRes, "drawable", context.packageName
                )
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = stringResource(R.string.quiz_question_picture_desc),
                        modifier = Modifier
                            .size(180.dp)
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = state.questionText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        QuizType.SOUND -> {
            // Show play button
            IconButton(
                onClick = onPlaySound,
                modifier = Modifier.size(96.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = stringResource(R.string.quiz_play_sound),
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = state.questionText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        QuizType.RIDDLE -> {
            // Show riddle text
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = state.questionText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        QuizType.DIET, QuizType.HABITAT -> {
            // Show question text
            Text(
                text = state.questionText,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        QuizType.REVERSE -> {
            // Show question text (riddle or description)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = state.questionText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        null -> { /* Should not happen during game play */ }
    }
}

@Composable
private fun TextQuizOptions(
    options: List<QuizOption>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, option ->
            val optionState = when {
                selectedIndex < 0 -> QuizOptionState.Default
                index == selectedIndex && option.isCorrect -> QuizOptionState.Correct
                index == selectedIndex && !option.isCorrect -> QuizOptionState.Incorrect
                option.isCorrect && selectedIndex >= 0 -> QuizOptionState.Correct
                else -> QuizOptionState.Disabled
            }

            QuizOptionButton(
                text = option.text,
                state = optionState,
                onClick = { onSelect(index) }
            )
        }
    }
}

@Composable
private fun ReverseQuizOptions(
    options: List<QuizOption>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    val columns = if (options.size <= 2) 2 else 2

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(0.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp) // Fixed height to avoid nested scroll issues
    ) {
        items(options.size) { index ->
            val option = options[index]
            val animal = option.animal

            if (animal != null) {
                val isSelected = index == selectedIndex
                val borderModifier = when {
                    selectedIndex < 0 -> false
                    index == selectedIndex && option.isCorrect -> true
                    option.isCorrect && selectedIndex >= 0 -> true
                    else -> false
                }

                AnimalCard(
                    animal = animal,
                    onClick = {
                        if (selectedIndex < 0) onSelect(index)
                    },
                    isSelected = borderModifier
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Sub-screen: Quiz Complete
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun QuizCompleteScreen(
    score: Int,
    totalQuestions: Int,
    starsEarned: Int,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
    onBackToTypes: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.quiz_complete),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Score display
        Text(
            text = stringResource(R.string.your_score, score, totalQuestions),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Stars
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                Icon(
                    imageVector = if (index < starsEarned) Icons.Filled.Star else Icons.Filled.StarOutline,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = if (index < starsEarned) StarGold else MaterialTheme.colorScheme.outlineVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Completion message
        val message = when {
            score == totalQuestions -> stringResource(R.string.quiz_perfect)
            score >= totalQuestions * 0.7 -> stringResource(R.string.quiz_great)
            score >= totalQuestions * 0.4 -> stringResource(R.string.quiz_good)
            else -> stringResource(R.string.quiz_try_harder)
        }
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Action buttons
        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.play_again),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackToTypes,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = stringResource(R.string.quiz_choose_another),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.nav_home),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
