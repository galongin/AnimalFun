package com.animalfun.ui.screens.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.animalfun.R
import com.animalfun.data.model.Animal
import com.animalfun.ui.theme.BirdsColor
import com.animalfun.ui.theme.FarmColor
import com.animalfun.ui.theme.InsectsColor
import com.animalfun.ui.theme.JungleColor
import com.animalfun.ui.theme.OceanColor
import com.animalfun.ui.theme.PetsColor
import com.animalfun.util.LocaleHelper
import org.koin.androidx.compose.koinViewModel

/**
 * Maps category names to their string resource IDs.
 */
private val categoryStringRes = mapOf(
    "All" to R.string.all_categories,
    "Farm" to R.string.category_farm,
    "Jungle" to R.string.category_jungle,
    "Ocean" to R.string.category_ocean,
    "Birds" to R.string.category_birds,
    "Pets" to R.string.category_pets,
    "Insects" to R.string.category_insects
)

/**
 * Maps category to a tint color for the filter chip.
 */
private val categoryColors = mapOf(
    "Farm" to FarmColor,
    "Jungle" to JungleColor,
    "Ocean" to OceanColor,
    "Birds" to BirdsColor,
    "Pets" to PetsColor,
    "Insects" to InsectsColor
)

@Composable
fun ExploreScreen(
    windowWidthSizeClass: WindowWidthSizeClass,
    onAnimalClick: (Int) -> Unit,
    viewModel: ExploreViewModel = koinViewModel()
) {
    val animals by viewModel.animals.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val columns = when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        WindowWidthSizeClass.Medium -> 3
        else -> 4 // Expanded
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Category filter chips
        CategoryChips(
            categories = ExploreViewModel.CATEGORIES,
            selectedCategory = selectedCategory,
            onCategorySelected = viewModel::selectCategory
        )

        // Animal grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(animals, key = { it.id }) { animal ->
                AnimalGridCard(
                    animal = animal,
                    onClick = { onAnimalClick(animal.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            val chipColor = categoryColors[category]

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        text = stringResource(categoryStringRes[category] ?: R.string.all_categories),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = if (chipColor != null) {
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = chipColor,
                        selectedLabelColor = androidx.compose.ui.graphics.Color.White
                    )
                } else {
                    FilterChipDefaults.filterChipColors()
                },
                modifier = Modifier.height(48.dp)
            )
        }
    }
}

@Composable
private fun AnimalGridCard(
    animal: Animal,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val isHebrew = LocaleHelper.isHebrew()
    val displayName = if (isHebrew) animal.nameHe else animal.nameEn
    val imageResId = context.resources.getIdentifier(
        animal.imageRes, "drawable", context.packageName
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            // Animal image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = displayName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // Fallback placeholder
                    Text(
                        text = displayName.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Animal name
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
