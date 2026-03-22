package com.animalfun.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.animalfun.ui.theme.CorrectGreen
import com.animalfun.ui.theme.IncorrectRed

/**
 * Visual state for a quiz option button.
 */
enum class QuizOptionState {
    /** Default idle state — not yet answered. */
    Default,
    /** Correct answer — green pulse animation. */
    Correct,
    /** Incorrect answer — red shake animation. */
    Incorrect,
    /** Disabled after the question has been answered. */
    Disabled
}

/**
 * A button composable for quiz answer options. Supports text label, optional image,
 * and animated visual feedback for correct/incorrect answers.
 *
 * @param text The answer text label.
 * @param state The visual state of the button.
 * @param onClick Callback when the button is tapped.
 * @param modifier Modifier for the button root.
 * @param imageResName Optional drawable resource name for picture/reverse quiz types.
 */
@Composable
fun QuizOptionButton(
    text: String,
    state: QuizOptionState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageResName: String? = null
) {
    val context = LocalContext.current

    // --- Color animation ---
    val containerColor by animateColorAsState(
        targetValue = when (state) {
            QuizOptionState.Default -> MaterialTheme.colorScheme.primaryContainer
            QuizOptionState.Correct -> CorrectGreen
            QuizOptionState.Incorrect -> IncorrectRed
            QuizOptionState.Disabled -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(durationMillis = 400),
        label = "quizOptionColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when (state) {
            QuizOptionState.Default -> MaterialTheme.colorScheme.onPrimaryContainer
            QuizOptionState.Correct -> Color.White
            QuizOptionState.Incorrect -> Color.White
            QuizOptionState.Disabled -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(durationMillis = 400),
        label = "quizOptionContentColor"
    )

    // --- Pulse animation for Correct ---
    val pulseScale: Float
    if (state == QuizOptionState.Correct) {
        val infiniteTransition = rememberInfiniteTransition(label = "correctPulse")
        val pulse by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.06f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 500),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )
        pulseScale = pulse
    } else {
        pulseScale = 1f
    }

    // --- Shake animation for Incorrect ---
    val shakeOffset: Float
    if (state == QuizOptionState.Incorrect) {
        val shakeAnim by animateFloatAsState(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessHigh
            ),
            label = "shakeOffset"
        )
        // We kick off the shake by starting from a non-zero value.
        // Using infinite transition for a quick back-and-forth shake.
        val infiniteTransition = rememberInfiniteTransition(label = "incorrectShake")
        val shake by infiniteTransition.animateFloat(
            initialValue = -8f,
            targetValue = 8f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 80),
                repeatMode = RepeatMode.Reverse
            ),
            label = "shakeX"
        )
        shakeOffset = shake
    } else {
        shakeOffset = 0f
    }

    val isEnabled = state == QuizOptionState.Default

    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .scale(pulseScale)
            .offset { IntOffset(x = shakeOffset.toInt(), y = 0) },
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp,
            disabledElevation = 2.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Optional image (for picture/reverse quiz types)
            if (imageResName != null) {
                val imageResId = context.resources.getIdentifier(
                    imageResName, "drawable", context.packageName
                )
                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = text,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
