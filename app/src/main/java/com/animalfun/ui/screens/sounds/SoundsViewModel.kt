package com.animalfun.ui.screens.sounds

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.animalfun.data.model.Animal
import com.animalfun.data.repository.AnimalRepository
import com.animalfun.util.AudioPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * The two modes available in the Sounds game.
 */
enum class SoundsMode {
    /** Free exploration: tap any animal to hear its sound. */
    EXPLORE,
    /** Challenge: a random animal sound plays, user must identify the animal. */
    MATCH
}

/**
 * UI state for the animal sounds screen.
 */
data class SoundsUiState(
    /** All animals available in the grid. */
    val animals: List<Animal> = emptyList(),
    /** Current game mode. */
    val currentMode: SoundsMode = SoundsMode.EXPLORE,
    /** The animal whose sound the user must match (MATCH mode only). */
    val matchTargetAnimal: Animal? = null,
    /** Consecutive correct matches in MATCH mode. */
    val streak: Int = 0,
    /** Best streak achieved in this session. */
    val bestStreak: Int = 0,
    /** Whether a sound is currently being played. */
    val isPlaying: Boolean = false,
    /** ID of the animal whose sound is currently playing (EXPLORE mode visual feedback). */
    val selectedAnimalId: Int? = null,
    /** Whether the celebration overlay should be shown. */
    val showCelebration: Boolean = false,
    /** ID of the animal card that should shake (wrong guess). */
    val shakeAnimalId: Int? = null,
    /** Whether animal data is still loading. */
    val isLoading: Boolean = true
)

class SoundsViewModel(
    application: Application,
    private val repository: AnimalRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SoundsUiState())
    val uiState: StateFlow<SoundsUiState> = _uiState.asStateFlow()

    private val audioPlayer = AudioPlayer(application)

    init {
        loadAnimals()
    }

    // ── Public actions ──

    /**
     * Switches between EXPLORE and MATCH modes.
     */
    fun setMode(mode: SoundsMode) {
        audioPlayer.stop()
        _uiState.update {
            it.copy(
                currentMode = mode,
                isPlaying = false,
                selectedAnimalId = null,
                showCelebration = false,
                shakeAnimalId = null,
                matchTargetAnimal = null,
                streak = if (mode == SoundsMode.MATCH) 0 else it.streak
            )
        }
        if (mode == SoundsMode.MATCH) {
            pickNewTarget()
        }
    }

    /**
     * Called when an animal card is tapped.
     * In EXPLORE mode: plays the animal's sound.
     * In MATCH mode: checks if this is the correct animal.
     */
    fun onAnimalTapped(animal: Animal) {
        when (_uiState.value.currentMode) {
            SoundsMode.EXPLORE -> playAnimalSound(animal)
            SoundsMode.MATCH -> checkMatch(animal)
        }
    }

    /**
     * Plays the target animal's sound again (MATCH mode).
     */
    fun playTargetSound() {
        val target = _uiState.value.matchTargetAnimal ?: return
        if (_uiState.value.isPlaying) return
        _uiState.update { it.copy(isPlaying = true) }
        audioPlayer.play(target.soundRes) {
            _uiState.update { it.copy(isPlaying = false) }
        }
    }

    /**
     * Dismisses the celebration overlay.
     */
    fun dismissCelebration() {
        _uiState.update { it.copy(showCelebration = false) }
    }

    /**
     * Clears the shake animation state after the animation completes.
     */
    fun clearShake() {
        _uiState.update { it.copy(shakeAnimalId = null) }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }

    // ── Private helpers ──

    private fun loadAnimals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val animals = repository.getAllAnimals().first()
            _uiState.update { it.copy(animals = animals, isLoading = false) }
        }
    }

    private fun playAnimalSound(animal: Animal) {
        audioPlayer.stop()
        _uiState.update {
            it.copy(
                selectedAnimalId = animal.id,
                isPlaying = true
            )
        }
        audioPlayer.play(animal.soundRes) {
            _uiState.update {
                it.copy(
                    selectedAnimalId = null,
                    isPlaying = false
                )
            }
        }
    }

    private fun checkMatch(animal: Animal) {
        val state = _uiState.value
        val target = state.matchTargetAnimal ?: return
        // Don't allow guessing while sound is playing or celebration is showing
        if (state.showCelebration) return

        if (animal.id == target.id) {
            // Correct guess
            val newStreak = state.streak + 1
            val newBest = maxOf(newStreak, state.bestStreak)
            _uiState.update {
                it.copy(
                    streak = newStreak,
                    bestStreak = newBest,
                    showCelebration = true,
                    shakeAnimalId = null,
                    selectedAnimalId = animal.id
                )
            }
        } else {
            // Wrong guess — trigger shake, reset streak
            _uiState.update {
                it.copy(
                    streak = 0,
                    shakeAnimalId = animal.id,
                    showCelebration = false
                )
            }
        }
    }

    /**
     * Picks a new random target animal and auto-plays its sound.
     * Called when entering MATCH mode or after a correct guess.
     */
    fun pickNewTarget() {
        val animals = _uiState.value.animals
        if (animals.isEmpty()) return

        val currentTargetId = _uiState.value.matchTargetAnimal?.id
        // Pick a different animal if possible
        val candidates = if (animals.size > 1) {
            animals.filter { it.id != currentTargetId }
        } else {
            animals
        }
        val newTarget = candidates.random()

        _uiState.update {
            it.copy(
                matchTargetAnimal = newTarget,
                selectedAnimalId = null,
                isPlaying = true
            )
        }

        audioPlayer.play(newTarget.soundRes) {
            _uiState.update { it.copy(isPlaying = false) }
        }
    }
}
