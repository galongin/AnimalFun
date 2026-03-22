package com.animalfun.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.animalfun.R
import com.animalfun.ui.theme.CoralRed
import com.animalfun.ui.theme.GrassGreen
import com.animalfun.ui.theme.LavenderPurple
import com.animalfun.ui.theme.OceanBlue
import com.animalfun.ui.theme.PeachOrange
import com.animalfun.ui.theme.StarGold
import com.animalfun.ui.theme.SunshineYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Data class representing a single confetti / star particle.
 */
private data class Particle(
    val x: Float,         // normalised 0..1 horizontal position
    val speed: Float,     // fall speed factor
    val size: Float,      // radius
    val color: Color,
    val rotation: Float,  // initial rotation degrees
    val rotationSpeed: Float,
    val isStar: Boolean   // true = star shape, false = rectangle confetti
)

private val particleColors = listOf(
    StarGold, SunshineYellow, OceanBlue, GrassGreen,
    CoralRed, LavenderPurple, PeachOrange
)

/**
 * A fullscreen celebration overlay shown when the player answers correctly.
 * Uses Compose-native Canvas animations (falling stars and confetti particles)
 * with a bounce-in congratulatory text.
 *
 * Auto-dismisses after approximately 2 seconds.
 *
 * @param onDismiss Callback invoked when the overlay is dismissed (auto or tap).
 */
@Composable
fun CelebrationOverlay(
    onDismiss: () -> Unit
) {
    // --- Particles ---
    val particles = remember {
        List(40) {
            Particle(
                x = Random.nextFloat(),
                speed = 0.3f + Random.nextFloat() * 0.7f,
                size = 6f + Random.nextFloat() * 10f,
                color = particleColors[Random.nextInt(particleColors.size)],
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = -180f + Random.nextFloat() * 360f,
                isStar = Random.nextBoolean()
            )
        }
    }

    // Animated progress drives particle fall (0 = top, 1 = past bottom)
    val fallProgress = remember { Animatable(0f) }

    // Text scale bounce-in
    val textScale = remember { Animatable(0f) }

    // Overlay alpha
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // Start animations in parallel
        launch {
            textScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
        launch {
            fallProgress.animateTo(
                targetValue = 1.3f,
                animationSpec = tween(durationMillis = 2200, easing = LinearEasing)
            )
        }

        // Auto-dismiss after 2 seconds
        delay(2000L)
        visible = false
        onDismiss()
    }

    if (!visible) return

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                visible = false
                onDismiss()
            },
        contentAlignment = Alignment.Center
    ) {
        // Semi-transparent background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = Color.Black.copy(alpha = 0.4f))
        }

        // Particle canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val progress = fallProgress.value

            particles.forEach { p ->
                val px = p.x * w
                val py = -p.size * 2 + progress * (h + p.size * 4) * p.speed
                val rot = p.rotation + progress * p.rotationSpeed * 3f

                if (py > -p.size * 2 && py < h + p.size * 2) {
                    if (p.isStar) {
                        drawStar(
                            center = Offset(px, py),
                            outerRadius = p.size,
                            color = p.color,
                            rotationDeg = rot
                        )
                    } else {
                        rotate(degrees = rot, pivot = Offset(px, py)) {
                            drawRect(
                                color = p.color,
                                topLeft = Offset(px - p.size / 2, py - p.size / 3),
                                size = androidx.compose.ui.geometry.Size(p.size, p.size * 0.6f)
                            )
                        }
                    }
                }
            }
        }

        // Celebration text with bounce scale
        Text(
            text = stringResource(R.string.celebration_great_job),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = StarGold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .scale(textScale.value)
                .padding(horizontal = 32.dp)
        )
    }
}

/**
 * Draws a 5-pointed star using Canvas.
 */
private fun DrawScope.drawStar(
    center: Offset,
    outerRadius: Float,
    color: Color,
    rotationDeg: Float
) {
    val innerRadius = outerRadius * 0.45f
    val path = Path()
    val points = 5
    val angleStep = (Math.PI * 2 / points).toFloat()
    val startAngle = (-Math.PI / 2).toFloat() // start from top

    rotate(degrees = rotationDeg, pivot = center) {
        for (i in 0 until points) {
            val outerAngle = startAngle + i * angleStep
            val innerAngle = outerAngle + angleStep / 2f

            val ox = center.x + outerRadius * cos(outerAngle)
            val oy = center.y + outerRadius * sin(outerAngle)
            val ix = center.x + innerRadius * cos(innerAngle)
            val iy = center.y + innerRadius * sin(innerAngle)

            if (i == 0) {
                path.moveTo(ox, oy)
            } else {
                path.lineTo(ox, oy)
            }
            path.lineTo(ix, iy)
        }
        path.close()
        drawPath(path = path, color = color)
    }
}
