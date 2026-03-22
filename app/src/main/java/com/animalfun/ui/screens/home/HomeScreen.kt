package com.animalfun.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.animalfun.R
import com.animalfun.data.repository.CategoryProgress
import com.animalfun.ui.theme.CoralRed
import com.animalfun.ui.theme.GrassGreen
import com.animalfun.ui.theme.LavenderPurple
import com.animalfun.ui.theme.OceanBlue
import com.animalfun.ui.theme.StarGold
import com.animalfun.util.LocaleHelper
import org.koin.androidx.compose.koinViewModel

/**
 * Represents one activity card on the home screen.
 */
private data class ActivityCard(
    val titleResId: Int,
    val descResId: Int,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

private val activityCards = listOf(
    ActivityCard(
        titleResId = R.string.explore_animals,
        descResId = R.string.explore_desc,
        icon = Icons.Filled.Explore,
        color = OceanBlue,
        route = "explore"
    ),
    ActivityCard(
        titleResId = R.string.take_quiz,
        descResId = R.string.quiz_desc,
        icon = Icons.Filled.Quiz,
        color = GrassGreen,
        route = "quiz"
    ),
    ActivityCard(
        titleResId = R.string.animal_sounds,
        descResId = R.string.sounds_desc,
        icon = Icons.Filled.MusicNote,
        color = CoralRed,
        route = "sounds"
    ),
    ActivityCard(
        titleResId = R.string.memory_game,
        descResId = R.string.memory_desc,
        icon = Icons.Filled.Psychology,
        color = LavenderPurple,
        route = "memory"
    )
)

/** Maps category name to a string resource id. */
private fun categoryToStringRes(category: String): Int = when (category) {
    "Farm" -> R.string.category_farm
    "Jungle" -> R.string.category_jungle
    "Ocean" -> R.string.category_ocean
    "Birds" -> R.string.category_birds
    "Pets" -> R.string.category_pets
    "Insects" -> R.string.category_insects
    else -> R.string.all_categories
}

/** Maps category name to a theme color. */
private fun categoryToColor(category: String): Color = when (category) {
    "Farm" -> Color(0xFFFF9800)
    "Jungle" -> GrassGreen
    "Ocean" -> OceanBlue
    "Birds" -> Color(0xFF03A9F4)
    "Pets" -> CoralRed
    "Insects" -> LavenderPurple
    else -> Color.Gray
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    windowWidthSizeClass: WindowWidthSizeClass,
    onNavigate: (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val totalStars by viewModel.totalStars.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val exploredCount by viewModel.exploredCount.collectAsStateWithLifecycle()
    val quizAccuracy by viewModel.quizAccuracy.collectAsStateWithLifecycle()
    val totalQuizAnswers by viewModel.totalQuizAnswers.collectAsStateWithLifecycle()
    val completionPercentage by viewModel.completionPercentage.collectAsStateWithLifecycle()
    val categoryProgress by viewModel.categoryProgress.collectAsStateWithLifecycle()
    val hasRecentActivity by viewModel.hasRecentActivity.collectAsStateWithLifecycle()

    val columns = when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        WindowWidthSizeClass.Medium -> 2
        else -> 3 // Expanded
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.welcome_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Language flag toggle
                    IconButton(
                        onClick = { viewModel.toggleLanguage() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Text(
                            text = if (currentLanguage == LocaleHelper.HEBREW) "\uD83C\uDDEC\uD83C\uDDE7" else "\uD83C\uDDEE\uD83C\uDDF1",
                            fontSize = 28.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ── Progress summary card (full width) ──
            item(span = { GridItemSpan(maxLineSpan) }) {
                ProgressSummaryCard(
                    totalStars = totalStars,
                    exploredCount = exploredCount,
                    quizAccuracyPercent = (quizAccuracy * 100).toInt(),
                    totalQuizAnswers = totalQuizAnswers,
                    completionPercentage = completionPercentage,
                    hasRecentActivity = hasRecentActivity
                )
            }

            // ── Category progress section (full width) ──
            if (categoryProgress.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    CategoryProgressSection(categoryProgress = categoryProgress)
                }
            }

            // ── Activity cards ──
            items(activityCards) { card ->
                HomeActivityCard(
                    card = card,
                    onClick = { onNavigate(card.route) }
                )
            }
        }
    }
}

@Composable
private fun ProgressSummaryCard(
    totalStars: Int,
    exploredCount: Int,
    quizAccuracyPercent: Int,
    totalQuizAnswers: Int,
    completionPercentage: Float,
    hasRecentActivity: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Title row with star
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = StarGold,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.your_progress),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Overall completion bar
            Text(
                text = stringResource(
                    R.string.progress_completion,
                    completionPercentage.toInt()
                ),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = (completionPercentage / 100f).coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = GrassGreen,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = stringResource(R.string.total_stars, totalStars),
                    icon = Icons.Filled.Star,
                    iconTint = StarGold
                )
                StatItem(
                    label = stringResource(R.string.progress_explored, exploredCount),
                    icon = Icons.Filled.Pets,
                    iconTint = OceanBlue
                )
                if (totalQuizAnswers > 0) {
                    StatItem(
                        label = stringResource(R.string.progress_accuracy, quizAccuracyPercent),
                        icon = Icons.Filled.CheckCircle,
                        iconTint = GrassGreen
                    )
                }
            }

            // Recent activity indicator
            if (hasRecentActivity) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.progress_recent_activity),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    icon: ImageVector,
    iconTint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CategoryProgressSection(categoryProgress: List<CategoryProgress>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.progress_by_category),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            categoryProgress.forEach { cp ->
                if (cp.total > 0) {
                    CategoryProgressRow(cp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun CategoryProgressRow(cp: CategoryProgress) {
    val fraction = if (cp.total > 0) cp.explored.toFloat() / cp.total else 0f
    val color = categoryToColor(cp.category)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(categoryToStringRes(cp.category)),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.progress_fraction, cp.explored, cp.total),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = fraction,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun HomeActivityCard(
    card: ActivityCard,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick)
            .animateContentSize(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = card.color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = card.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(card.titleResId),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(card.descResId),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
