package com.animalfun.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.animalfun.data.model.Animal
import com.animalfun.ui.theme.BirdsColor
import com.animalfun.ui.theme.FarmColor
import com.animalfun.ui.theme.InsectsColor
import com.animalfun.ui.theme.JungleColor
import com.animalfun.ui.theme.OceanColor
import com.animalfun.ui.theme.PetsColor
import com.animalfun.util.LocaleHelper

/**
 * Returns the category color for the given category name.
 */
fun categoryColor(category: String): Color = when (category) {
    "Farm" -> FarmColor
    "Jungle" -> JungleColor
    "Ocean" -> OceanColor
    "Birds" -> BirdsColor
    "Pets" -> PetsColor
    "Insects" -> InsectsColor
    else -> FarmColor
}

/**
 * A reusable animal card composable used across Explore, Sounds, and Quiz screens.
 *
 * @param animal The animal data to display.
 * @param onClick Callback when the card is tapped.
 * @param modifier Modifier for the card root.
 * @param isSelected Whether the card is in a selected state (shows border highlight).
 * @param overlayContent Optional composable content rendered as an overlay on the image area
 *                       (useful for quiz modes — icons, text labels, etc.).
 */
@Composable
fun AnimalCard(
    animal: Animal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    overlayContent: (@Composable () -> Unit)? = null
) {
    val context = LocalContext.current
    val isHebrew = LocaleHelper.isHebrew()
    val displayName = if (isHebrew) animal.nameHe else animal.nameEn
    val imageResId = context.resources.getIdentifier(
        animal.imageRes, "drawable", context.packageName
    )
    val catColor = categoryColor(animal.category)

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "selectedBorder"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 3.dp,
                        color = borderColor,
                        shape = MaterialTheme.shapes.large
                    )
                } else {
                    Modifier
                }
            )
            .clip(MaterialTheme.shapes.large)
            .clickable(
                role = Role.Button,
                onClick = onClick
            ),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Category color indicator strip at top
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = catColor
            ) {}

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                // Animal image with optional overlay
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
                        // Fallback placeholder — first letter of the name
                        Text(
                            text = displayName.firstOrNull()?.uppercase() ?: "?",
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Overlay content (quiz mode icons/text)
                    if (overlayContent != null) {
                        overlayContent()
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
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
