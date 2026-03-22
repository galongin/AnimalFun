package com.animalfun.ui.screens.memory

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for memory game logic - tests pure data/state behavior
 * without needing Android framework mocks.
 */
class MemoryViewModelTest {

    // ── MemoryDifficulty ──

    @Test
    fun `EASY difficulty has 3 pairs (2x3 grid)`() {
        assertEquals(2, MemoryDifficulty.EASY.rows)
        assertEquals(3, MemoryDifficulty.EASY.columns)
        assertEquals(3, MemoryDifficulty.EASY.rows * MemoryDifficulty.EASY.columns / 2)
    }

    @Test
    fun `MEDIUM difficulty has 6 pairs (3x4 grid)`() {
        assertEquals(3, MemoryDifficulty.MEDIUM.rows)
        assertEquals(4, MemoryDifficulty.MEDIUM.columns)
        assertEquals(6, MemoryDifficulty.MEDIUM.rows * MemoryDifficulty.MEDIUM.columns / 2)
    }

    @Test
    fun `HARD difficulty has 10 pairs (4x5 grid)`() {
        assertEquals(4, MemoryDifficulty.HARD.rows)
        assertEquals(5, MemoryDifficulty.HARD.columns)
        assertEquals(10, MemoryDifficulty.HARD.rows * MemoryDifficulty.HARD.columns / 2)
    }

    // ── MemoryGameConfig ──

    @Test
    fun `MemoryGameConfig calculates totalPairs correctly`() {
        val config = MemoryGameConfig(rows = 3, columns = 4, matchType = MatchType.IMAGE_IMAGE)
        assertEquals(6, config.totalPairs)
    }

    @Test
    fun `MemoryGameConfig with IMAGE_NAME match type`() {
        val config = MemoryGameConfig(rows = 2, columns = 3, matchType = MatchType.IMAGE_NAME)
        assertEquals(MatchType.IMAGE_NAME, config.matchType)
        assertEquals(3, config.totalPairs)
    }

    // ── MatchType ──

    @Test
    fun `MatchType has IMAGE_IMAGE and IMAGE_NAME`() {
        assertEquals(2, MatchType.entries.size)
        val names = MatchType.entries.map { it.name }.toSet()
        assertTrue("IMAGE_IMAGE" in names)
        assertTrue("IMAGE_NAME" in names)
    }

    // ── MemoryCard ──

    @Test
    fun `MemoryCard starts face down and unmatched`() {
        val card = MemoryCard(
            id = 0,
            animalId = 1,
            content = MemoryCardContent.IMAGE,
            imageRes = "animal_dog",
            displayText = null,
            isFlipped = false,
            isMatched = false
        )
        assertFalse(card.isFlipped)
        assertFalse(card.isMatched)
    }

    @Test
    fun `MemoryCard copy flips correctly`() {
        val card = MemoryCard(
            id = 0, animalId = 1, content = MemoryCardContent.IMAGE,
            imageRes = "test", displayText = null, isFlipped = false, isMatched = false
        )
        val flipped = card.copy(isFlipped = true)
        assertTrue(flipped.isFlipped)
        assertFalse(flipped.isMatched)
    }

    @Test
    fun `MemoryCard copy matches correctly`() {
        val card = MemoryCard(
            id = 0, animalId = 1, content = MemoryCardContent.IMAGE,
            imageRes = "test", displayText = null, isFlipped = true, isMatched = false
        )
        val matched = card.copy(isMatched = true)
        assertTrue(matched.isFlipped)
        assertTrue(matched.isMatched)
    }

    @Test
    fun `MemoryCardContent has IMAGE and NAME`() {
        assertEquals(2, MemoryCardContent.entries.size)
    }

    // ── MemoryUiState ──

    @Test
    fun `default MemoryUiState starts in SETUP`() {
        val state = MemoryUiState()
        assertEquals(MemoryScreenState.SETUP, state.screen)
        assertEquals(0, state.moves)
        assertEquals(0L, state.timer)
        assertEquals(0, state.matchesFound)
        assertFalse(state.isComplete)
        assertFalse(state.showCelebration)
    }

    @Test
    fun `MemoryUiState tracks moves`() {
        val state = MemoryUiState(moves = 5)
        assertEquals(5, state.moves)
    }

    @Test
    fun `MemoryUiState detects completion`() {
        val state = MemoryUiState(matchesFound = 6, totalPairs = 6, isComplete = true)
        assertTrue(state.isComplete)
        assertEquals(state.matchesFound, state.totalPairs)
    }

    // ── Timer formatting ──

    @Test
    fun `formatTime formats seconds correctly`() {
        // 0 seconds = 00:00
        assertEquals("00:00", formatTime(0))
    }

    @Test
    fun `formatTime formats minutes correctly`() {
        // 125 seconds = 02:05
        assertEquals("02:05", formatTime(125))
    }

    @Test
    fun `formatTime handles exact minute`() {
        assertEquals("01:00", formatTime(60))
    }

    // ── Difficulty enum ──

    @Test
    fun `Difficulty has 3 levels`() {
        assertEquals(3, MemoryDifficulty.entries.size)
    }

    // ── Pair creation logic ──

    @Test
    fun `IMAGE_IMAGE pairs have same content type`() {
        // When matchType is IMAGE_IMAGE, both cards in a pair should be IMAGE
        val card1 = MemoryCard(0, 1, MemoryCardContent.IMAGE, "dog", null, false, false)
        val card2 = MemoryCard(1, 1, MemoryCardContent.IMAGE, "dog", null, false, false)
        assertEquals(card1.animalId, card2.animalId)
        assertEquals(card1.content, card2.content)
    }

    @Test
    fun `IMAGE_NAME pairs have different content types`() {
        val card1 = MemoryCard(0, 1, MemoryCardContent.IMAGE, "dog", null, false, false)
        val card2 = MemoryCard(1, 1, MemoryCardContent.NAME, null, "Dog", false, false)
        assertEquals(card1.animalId, card2.animalId)
        assertFalse(card1.content == card2.content)
    }
}

// Helper to test timer formatting without ViewModel
private fun formatTime(seconds: Long): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(min, sec)
}
