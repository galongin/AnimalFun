package com.animalfun.ui.screens.memory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.animalfun.data.model.Animal
import com.animalfun.data.repository.AnimalRepository
import com.animalfun.util.LocaleHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Difficulty levels with corresponding grid sizes.
 */
enum class MemoryDifficulty(val rows: Int, val columns: Int) {
    EASY(2, 3),     // 3 pairs
    MEDIUM(3, 4),   // 6 pairs
    HARD(4, 5)      // 10 pairs
}

/**
 * Sub-screen state for the memory game flow.
 */
enum class MemoryScreenState {
    /** Difficulty and match-type selection. */
    SETUP,
    /** Active gameplay. */
    PLAYING,
    /** Game complete — show results. */
    COMPLETE
}

/**
 * UI state for the memory game.
 */
data class MemoryUiState(
    val screen: MemoryScreenState = MemoryScreenState.SETUP,
    val cards: List<MemoryCard> = emptyList(),
    val moves: Int = 0,
    val timer: Long = 0L,
    val firstFlippedIndex: Int? = null,
    val secondFlippedIndex: Int? = null,
    val matchesFound: Int = 0,
    val totalPairs: Int = 0,
    val isComplete: Boolean = false,
    val showCelebration: Boolean = false,
    val bestTime: Long? = null,
    val difficulty: MemoryDifficulty = MemoryDifficulty.EASY,
    val matchType: MatchType = MatchType.IMAGE_IMAGE,
    val isProcessing: Boolean = false,
    val isLoading: Boolean = false
)

/**
 * ViewModel for the memory card game.
 *
 * Manages card layout, flip logic, match detection, timer, and move counting.
 */
class MemoryViewModel(
    application: Application,
    private val repository: AnimalRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MemoryUiState())
    val uiState: StateFlow<MemoryUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var timerStarted = false

    /**
     * Updates the selected difficulty on the setup screen.
     */
    fun setDifficulty(difficulty: MemoryDifficulty) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    /**
     * Updates the match type on the setup screen.
     */
    fun setMatchType(matchType: MatchType) {
        _uiState.update { it.copy(matchType = matchType) }
    }

    /**
     * Starts a new game with the current difficulty and match type settings.
     */
    fun startGame() {
        val state = _uiState.value
        val config = MemoryGameConfig(
            rows = state.difficulty.rows,
            columns = state.difficulty.columns,
            matchType = state.matchType
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val allAnimals = repository.getAllAnimals().first()
            if (allAnimals.isEmpty()) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val pairCount = config.totalPairs
            val selectedAnimals = allAnimals.shuffled().take(pairCount)
            val cards = createCards(selectedAnimals, config)

            timerStarted = false
            timerJob?.cancel()

            _uiState.update {
                MemoryUiState(
                    screen = MemoryScreenState.PLAYING,
                    cards = cards,
                    totalPairs = pairCount,
                    difficulty = state.difficulty,
                    matchType = state.matchType
                )
            }
        }
    }

    /**
     * Creates paired cards from the selected animals, then shuffles.
     */
    private fun createCards(
        animals: List<Animal>,
        config: MemoryGameConfig
    ): List<MemoryCard> {
        val isHebrew = LocaleHelper.isHebrew()
        val cards = mutableListOf<MemoryCard>()
        var cardId = 0

        for (animal in animals) {
            // First card is always an image
            cards.add(
                MemoryCard(
                    id = cardId++,
                    animalId = animal.id,
                    content = MemoryCardContent.IMAGE,
                    imageRes = animal.imageRes
                )
            )

            // Second card depends on match type
            when (config.matchType) {
                MatchType.IMAGE_IMAGE -> {
                    cards.add(
                        MemoryCard(
                            id = cardId++,
                            animalId = animal.id,
                            content = MemoryCardContent.IMAGE,
                            imageRes = animal.imageRes
                        )
                    )
                }
                MatchType.IMAGE_NAME -> {
                    val name = if (isHebrew) animal.nameHe else animal.nameEn
                    cards.add(
                        MemoryCard(
                            id = cardId++,
                            animalId = animal.id,
                            content = MemoryCardContent.NAME,
                            displayText = name
                        )
                    )
                }
            }
        }

        return cards.shuffled()
    }

    /**
     * Handles a card flip at the given index.
     */
    fun flipCard(index: Int) {
        val state = _uiState.value
        val card = state.cards.getOrNull(index) ?: return

        // Ignore if already processing, card is already flipped/matched, or same card tapped
        if (state.isProcessing || card.isFlipped || card.isMatched) return

        // Start timer on first flip
        if (!timerStarted) {
            timerStarted = true
            startTimer()
        }

        if (state.firstFlippedIndex == null) {
            // First card of pair
            val updatedCards = state.cards.toMutableList()
            updatedCards[index] = card.copy(isFlipped = true)
            _uiState.update {
                it.copy(
                    cards = updatedCards,
                    firstFlippedIndex = index
                )
            }
        } else {
            // Second card of pair
            val updatedCards = state.cards.toMutableList()
            updatedCards[index] = card.copy(isFlipped = true)
            val newMoves = state.moves + 1

            _uiState.update {
                it.copy(
                    cards = updatedCards,
                    secondFlippedIndex = index,
                    moves = newMoves,
                    isProcessing = true
                )
            }

            // Check for match
            val firstCard = state.cards[state.firstFlippedIndex]
            val secondCard = card

            viewModelScope.launch {
                delay(800L) // Let the player see the second card

                if (firstCard.animalId == secondCard.animalId) {
                    // Match found
                    handleMatch(state.firstFlippedIndex, index)
                } else {
                    // No match — flip both back
                    handleMismatch(state.firstFlippedIndex, index)
                }
            }
        }
    }

    /**
     * Marks both cards as matched and checks for game completion.
     */
    private fun handleMatch(firstIndex: Int, secondIndex: Int) {
        _uiState.update { state ->
            val updatedCards = state.cards.toMutableList()
            updatedCards[firstIndex] = updatedCards[firstIndex].copy(isMatched = true)
            updatedCards[secondIndex] = updatedCards[secondIndex].copy(isMatched = true)
            val newMatchesFound = state.matchesFound + 1
            val isComplete = newMatchesFound == state.totalPairs

            if (isComplete) {
                timerJob?.cancel()
            }

            state.copy(
                cards = updatedCards,
                matchesFound = newMatchesFound,
                firstFlippedIndex = null,
                secondFlippedIndex = null,
                isProcessing = false,
                isComplete = isComplete,
                showCelebration = isComplete,
                screen = if (isComplete) MemoryScreenState.COMPLETE else state.screen
            )
        }

        // Save best time on completion
        if (_uiState.value.isComplete) {
            saveBestTime()
        }
    }

    /**
     * Flips both unmatched cards back to face-down.
     */
    private fun handleMismatch(firstIndex: Int, secondIndex: Int) {
        _uiState.update { state ->
            val updatedCards = state.cards.toMutableList()
            updatedCards[firstIndex] = updatedCards[firstIndex].copy(isFlipped = false)
            updatedCards[secondIndex] = updatedCards[secondIndex].copy(isFlipped = false)
            state.copy(
                cards = updatedCards,
                firstFlippedIndex = null,
                secondFlippedIndex = null,
                isProcessing = false
            )
        }
    }

    /**
     * Starts the game timer that counts up every second.
     */
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _uiState.update { it.copy(timer = it.timer + 1) }
            }
        }
    }

    /**
     * Saves progress for all animals used in the memory game.
     * Awards 1 star per completion and tracks the best time on the first animal.
     */
    private fun saveBestTime() {
        viewModelScope.launch {
            val state = _uiState.value
            val time = state.timer

            // Collect distinct animal IDs used in this game
            val animalIds = state.cards.map { it.animalId }.distinct()

            // Award a star to each animal that participated in this memory game
            for (animalId in animalIds) {
                val existing = repository.getUserProgress(animalId).first()
                val updated = existing?.copy(
                    starsEarned = existing.starsEarned + 1,
                    lastInteraction = System.currentTimeMillis()
                ) ?: com.animalfun.data.model.UserProgress(
                    animalId = animalId,
                    starsEarned = 1,
                    lastInteraction = System.currentTimeMillis()
                )
                repository.upsertProgress(updated)
            }

            // Track best time on the first animal
            val firstAnimalId = animalIds.firstOrNull() ?: return@launch
            val progress = repository.getUserProgress(firstAnimalId).first()
            val current = progress?.bestMemoryTime
            if (current == null || time < current) {
                val updated = progress?.copy(bestMemoryTime = time)
                    ?: com.animalfun.data.model.UserProgress(
                        animalId = firstAnimalId,
                        bestMemoryTime = time
                    )
                repository.upsertProgress(updated)
                _uiState.update { it.copy(bestTime = time) }
            } else {
                _uiState.update { it.copy(bestTime = current) }
            }
        }
    }

    /**
     * Dismisses the celebration overlay.
     */
    fun dismissCelebration() {
        _uiState.update { it.copy(showCelebration = false) }
    }

    /**
     * Returns to the setup screen for a new game.
     */
    fun returnToSetup() {
        timerJob?.cancel()
        timerStarted = false
        _uiState.update {
            MemoryUiState(
                difficulty = it.difficulty,
                matchType = it.matchType
            )
        }
    }

    /**
     * Restarts the game with the same settings.
     */
    fun playAgain() {
        startGame()
    }

    /**
     * Formats seconds into MM:SS string.
     */
    fun formatTime(seconds: Long): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
