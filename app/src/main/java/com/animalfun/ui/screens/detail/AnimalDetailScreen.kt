package com.animalfun.ui.screens.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.animalfun.R
import com.animalfun.data.model.Animal
import com.animalfun.ui.theme.GrassGreen
import com.animalfun.ui.theme.OceanBlue
import com.animalfun.ui.theme.PeachOrange
import com.animalfun.ui.theme.SunshineYellow
import com.animalfun.util.LocaleHelper
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailScreen(
    onBack: () -> Unit,
    viewModel: AnimalDetailViewModel = koinViewModel()
) {
    val animal by viewModel.animal.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            animal == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.loading),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                AnimalDetailContent(
                    animal = animal!!,
                    onPlaySound = viewModel::playSound,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AnimalDetailContent(
    animal: Animal,
    onPlaySound: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHebrew = LocaleHelper.isHebrew()
    val displayName = if (isHebrew) animal.nameHe else animal.nameEn
    val facts = if (isHebrew) animal.factsHe else animal.factsEn
    val diet = if (isHebrew) animal.dietHe else animal.dietEn
    val habitat = if (isHebrew) animal.habitatHe else animal.habitatEn

    val imageResId = context.resources.getIdentifier(
        animal.imageRes, "drawable", context.packageName
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large illustration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = stringResource(R.string.animal_image_desc, displayName),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                // Fallback: large initial letter
                Text(
                    text = displayName.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 96.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Animal name
        Text(
            text = displayName,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tap to hear button
        Button(
            onClick = onPlaySound,
            modifier = Modifier
                .height(56.dp)
                .padding(horizontal = 32.dp)
                .fillMaxWidth(0.7f),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = OceanBlue
            )
        ) {
            Icon(
                imageVector = Icons.Filled.VolumeUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.tap_to_hear),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Fun facts title
        Text(
            text = stringResource(R.string.fun_facts),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontally swipeable fact cards
        if (facts.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = { facts.size })

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 32.dp
                ),
                pageSpacing = 12.dp
            ) { page ->
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = facts[page],
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Page indicators
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(facts.size) { index ->
                    val color = if (pagerState.currentPage == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            // Fact page counter
            Text(
                text = stringResource(
                    R.string.fact_page,
                    pagerState.currentPage + 1,
                    facts.size
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info row: Habitat, Diet, Size
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (habitat.isNotBlank()) {
                InfoRow(
                    icon = Icons.Filled.Forest,
                    label = stringResource(R.string.habitat),
                    value = habitat,
                    iconTint = GrassGreen
                )
            }
            if (diet.isNotBlank()) {
                InfoRow(
                    icon = Icons.Filled.Restaurant,
                    label = stringResource(R.string.diet),
                    value = diet,
                    iconTint = PeachOrange
                )
            }
            if (animal.sizeInfo.isNotBlank()) {
                InfoRow(
                    icon = Icons.Filled.Height,
                    label = stringResource(R.string.size),
                    value = animal.sizeInfo,
                    iconTint = SunshineYellow
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
