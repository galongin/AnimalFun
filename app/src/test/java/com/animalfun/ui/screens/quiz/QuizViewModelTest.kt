package com.animalfun.ui.screens.quiz

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Unit tests for quiz logic - tests pure data/enum behavior
 * without needing Android framework mocks.
 */
class QuizViewModelTest {

    // ── QuizType ──

    @Test
    fun `QuizType has 6 types`() {
        assertEquals(6, QuizType.entries.size)
    }

    @Test
    fun `QuizType contains all expected types`() {
        val names = QuizType.entries.map { it.name }.toSet()
        assert("PICTURE" in names)
        assert("SOUND" in names)
        assert("RIDDLE" in names)
        assert("DIET" in names)
        assert("HABITAT" in names)
        assert("REVERSE" in names)
    }

    // ── Difficulty ──

    @Test
    fun `EASY difficulty provides 2 options`() {
        assertEquals(2, Difficulty.EASY.optionCount)
    }

    @Test
    fun `MEDIUM difficulty provides 3 options`() {
        assertEquals(3, Difficulty.MEDIUM.optionCount)
    }

    @Test
    fun `HARD difficulty provides 4 options`() {
        assertEquals(4, Difficulty.HARD.optionCount)
    }

    @Test
    fun `Difficulty has 3 levels`() {
        assertEquals(3, Difficulty.entries.size)
    }

    // ── QuizScreenState ──

    @Test
    fun `QuizScreenState has TYPE_SELECTION, GAME_PLAY, COMPLETE`() {
        val states = QuizScreenState.entries.map { it.name }.toSet()
        assert("TYPE_SELECTION" in states)
        assert("GAME_PLAY" in states)
        assert("COMPLETE" in states)
    }

    // ── QuizUiState ──

    @Test
    fun `default QuizUiState has zero score`() {
        val state = QuizUiState()
        assertEquals(0, state.score)
        assertEquals(0, state.currentQuestion)
        assertEquals(QuizScreenState.TYPE_SELECTION, state.screen)
    }

    @Test
    fun `QuizUiState copy updates correctly`() {
        val state = QuizUiState()
        val updated = state.copy(score = 5, currentQuestion = 2)
        assertEquals(5, updated.score)
        assertEquals(2, updated.currentQuestion)
    }

    @Test
    fun `default difficulty is MEDIUM`() {
        val state = QuizUiState()
        assertEquals(Difficulty.MEDIUM, state.difficulty)
    }

    @Test
    fun `stars calculation - perfect score gets 3 stars`() {
        // 5/5 = 100% -> 3 stars
        val state = QuizUiState(score = 5, totalQuestions = 5)
        val percentage = state.score.toFloat() / state.totalQuestions.coerceAtLeast(1)
        val stars = when {
            percentage >= 0.8f -> 3
            percentage >= 0.5f -> 2
            percentage > 0f -> 1
            else -> 0
        }
        assertEquals(3, stars)
    }

    @Test
    fun `stars calculation - 60 percent gets 2 stars`() {
        val state = QuizUiState(score = 3, totalQuestions = 5)
        val percentage = state.score.toFloat() / state.totalQuestions.coerceAtLeast(1)
        val stars = when {
            percentage >= 0.8f -> 3
            percentage >= 0.5f -> 2
            percentage > 0f -> 1
            else -> 0
        }
        assertEquals(2, stars)
    }

    @Test
    fun `stars calculation - 20 percent gets 1 star`() {
        val state = QuizUiState(score = 1, totalQuestions = 5)
        val percentage = state.score.toFloat() / state.totalQuestions.coerceAtLeast(1)
        val stars = when {
            percentage >= 0.8f -> 3
            percentage >= 0.5f -> 2
            percentage > 0f -> 1
            else -> 0
        }
        assertEquals(1, stars)
    }

    @Test
    fun `stars calculation - zero score gets 0 stars`() {
        val state = QuizUiState(score = 0, totalQuestions = 5)
        val percentage = state.score.toFloat() / state.totalQuestions.coerceAtLeast(1)
        val stars = when {
            percentage >= 0.8f -> 3
            percentage >= 0.5f -> 2
            percentage > 0f -> 1
            else -> 0
        }
        assertEquals(0, stars)
    }

    @Test
    fun `each quiz type has a display name resource`() {
        for (type in QuizType.entries) {
            assertNotEquals(0, type.displayNameResId)
        }
    }

    @Test
    fun `each quiz type has an icon resource`() {
        for (type in QuizType.entries) {
            assertNotEquals(0, type.iconResId)
        }
    }
}
