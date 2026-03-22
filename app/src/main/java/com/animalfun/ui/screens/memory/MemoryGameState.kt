package com.animalfun.ui.screens.memory

/**
 * Content type displayed on a memory card face.
 */
enum class MemoryCardContent {
    /** Card shows the animal image. */
    IMAGE,
    /** Card shows the animal name in the current language. */
    NAME
}

/**
 * Match type for the memory game.
 */
enum class MatchType {
    /** Both cards in a pair show images (same animal). */
    IMAGE_IMAGE,
    /** One card shows the image, the other shows the animal name. */
    IMAGE_NAME
}

/**
 * Configuration for a memory game round.
 *
 * @param rows Number of rows in the grid.
 * @param columns Number of columns in the grid.
 * @param matchType Whether pairs are image-image or image-name.
 */
data class MemoryGameConfig(
    val rows: Int,
    val columns: Int,
    val matchType: MatchType = MatchType.IMAGE_IMAGE
) {
    /** Total number of pairs in this configuration. */
    val totalPairs: Int get() = (rows * columns) / 2
}

/**
 * Represents a single card in the memory game grid.
 *
 * @param id Unique identifier for this card instance.
 * @param animalId The animal this card belongs to.
 * @param content Whether this card shows an image or a name.
 * @param imageRes Drawable resource name (when content == IMAGE).
 * @param displayText Text to show (when content == NAME).
 * @param isFlipped Whether the card is currently face-up.
 * @param isMatched Whether this card has been matched and should stay face-up.
 */
data class MemoryCard(
    val id: Int,
    val animalId: Int,
    val content: MemoryCardContent,
    val imageRes: String? = null,
    val displayText: String? = null,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)
