package com.animalfun.ui.screens.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.animalfun.data.model.Animal
import com.animalfun.data.model.UserProgress
import com.animalfun.data.repository.AnimalRepository
import com.animalfun.util.AudioPlayer
import com.animalfun.util.LocaleHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Difficulty levels determining the number of answer options.
 */
enum class Difficulty(val optionCount: Int) {
    EASY(2),
    MEDIUM(3),
    HARD(4)
}

/**
 * Represents a single option in a quiz question.
 *
 * @param text The text label for this option.
 * @param animal The animal associated with this option (used for image-based options in REVERSE type).
 * @param isCorrect Whether this option is the correct answer.
 */
data class QuizOption(
    val text: String,
    val animal: Animal? = null,
    val isCorrect: Boolean = false
)

/**
 * UI state for the quiz game.
 */
data class QuizUiState(
    /** Current sub-screen: TYPE_SELECTION, GAME_PLAY, or COMPLETE. */
    val screen: QuizScreenState = QuizScreenState.TYPE_SELECTION,
    /** Index of the current question (0-based). */
    val currentQuestion: Int = 0,
    /** Total number of questions in this quiz round. */
    val totalQuestions: Int = 5,
    /** Current score (number of correct answers). */
    val score: Int = 0,
    /** The selected quiz type (null when on type-selection screen). */
    val quizType: QuizType? = null,
    /** The selected difficulty. */
    val difficulty: Difficulty = Difficulty.MEDIUM,
    /** The animal being asked about in the current question. */
    val currentAnimal: Animal? = null,
    /** The question text to display. */
    val questionText: String = "",
    /** The list of answer options. */
    val options: List<QuizOption> = emptyList(),
    /** Index of the selected answer, or -1 if not yet answered. */
    val selectedAnswerIndex: Int = -1,
    /** Whether the celebration overlay should be showing. */
    val showCelebration: Boolean = false,
    /** Whether the quiz round is complete. */
    val isComplete: Boolean = false,
    /** Stars earned for this quiz round (calculated at completion). */
    val starsEarned: Int = 0,
    /** Whether animal data is still loading. */
    val isLoading: Boolean = false
)

enum class QuizScreenState {
    TYPE_SELECTION,
    GAME_PLAY,
    COMPLETE
}

class QuizViewModel(
    application: Application,
    private val repository: AnimalRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val audioPlayer = AudioPlayer(application)

    /** Cached list of all animals from the database. */
    private var allAnimals: List<Animal> = emptyList()

    /** Animals selected for this quiz round, pre-shuffled. */
    private var quizAnimals: List<Animal> = emptyList()

    // ── Public actions ──

    /**
     * Sets the difficulty level before starting a quiz.
     */
    fun setDifficulty(difficulty: Difficulty) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    /**
     * Starts a quiz of the given type. Fetches animals from the repo,
     * generates questions dynamically, and moves to the game-play screen.
     */
    fun startQuiz(quizType: QuizType) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Fetch all animals once
            if (allAnimals.isEmpty()) {
                allAnimals = repository.getAllAnimals().first()
            }

            // Filter animals that have the data needed for this quiz type
            val eligible = allAnimals.filter { animal ->
                when (quizType) {
                    QuizType.RIDDLE -> animal.riddleEn.isNotBlank()
                    QuizType.DIET -> animal.dietEn.isNotBlank()
                    QuizType.HABITAT -> animal.habitatEn.isNotBlank()
                    else -> true
                }
            }

            val difficulty = _uiState.value.difficulty
            val needed = difficulty.optionCount

            // Need at least `optionCount` eligible animals to form a question
            if (eligible.size < needed) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            // Pick up to 5 distinct animals as quiz subjects
            val totalQuestions = minOf(5, eligible.size)
            quizAnimals = eligible.shuffled().take(totalQuestions)

            _uiState.update {
                it.copy(
                    screen = QuizScreenState.GAME_PLAY,
                    quizType = quizType,
                    currentQuestion = 0,
                    totalQuestions = totalQuestions,
                    score = 0,
                    selectedAnswerIndex = -1,
                    showCelebration = false,
                    isComplete = false,
                    starsEarned = 0,
                    isLoading = false
                )
            }

            loadQuestion(0, quizType, difficulty)
        }
    }

    /**
     * Called when the user taps an answer option.
     */
    fun selectAnswer(index: Int) {
        val state = _uiState.value
        // Ignore if already answered
        if (state.selectedAnswerIndex >= 0) return

        val isCorrect = state.options.getOrNull(index)?.isCorrect == true
        val newScore = if (isCorrect) state.score + 1 else state.score

        _uiState.update {
            it.copy(
                selectedAnswerIndex = index,
                score = newScore,
                showCelebration = isCorrect
            )
        }

        // Advance after a delay
        viewModelScope.launch {
            delay(if (isCorrect) 2000L else 1500L)
            advanceQuestion()
        }
    }

    /**
     * Plays the sound for the current animal (used in SOUND quiz type).
     */
    fun playCurrentAnimalSound() {
        val animal = _uiState.value.currentAnimal ?: return
        audioPlayer.play(animal.soundRes)
    }

    /**
     * Resets the quiz back to the type-selection screen.
     */
    fun backToTypeSelection() {
        audioPlayer.stop()
        _uiState.update { QuizUiState() }
    }

    /**
     * Replays the quiz with the same type and difficulty.
     */
    fun playAgain() {
        val quizType = _uiState.value.quizType ?: return
        startQuiz(quizType)
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }

    // ── Private helpers ──

    private fun advanceQuestion() {
        val state = _uiState.value
        val nextIndex = state.currentQuestion + 1

        if (nextIndex >= state.totalQuestions) {
            // Quiz complete
            val stars = calculateStars(state.score + 0, state.totalQuestions)
            _uiState.update {
                it.copy(
                    screen = QuizScreenState.COMPLETE,
                    isComplete = true,
                    showCelebration = false,
                    starsEarned = stars
                )
            }
            // Persist progress
            saveProgress()
        } else {
            _uiState.update {
                it.copy(
                    currentQuestion = nextIndex,
                    selectedAnswerIndex = -1,
                    showCelebration = false
                )
            }
            loadQuestion(nextIndex, state.quizType!!, state.difficulty)
        }
    }

    private fun loadQuestion(index: Int, quizType: QuizType, difficulty: Difficulty) {
        val correctAnimal = quizAnimals[index]
        val isHebrew = LocaleHelper.isHebrew()
        val optionCount = difficulty.optionCount

        // Pick N-1 random wrong animals (different from correct)
        val wrongAnimals = allAnimals
            .filter { it.id != correctAnimal.id }
            .let { pool ->
                // For DIET/HABITAT, pick animals with distinct answers to avoid duplicate options
                when (quizType) {
                    QuizType.DIET -> pool.filter {
                        val answer = if (isHebrew) it.dietHe else it.dietEn
                        answer.isNotBlank() && answer != (if (isHebrew) correctAnimal.dietHe else correctAnimal.dietEn)
                    }
                    QuizType.HABITAT -> pool.filter {
                        val answer = if (isHebrew) it.habitatHe else it.habitatEn
                        answer.isNotBlank() && answer != (if (isHebrew) correctAnimal.habitatHe else correctAnimal.habitatEn)
                    }
                    else -> pool
                }
            }
            .shuffled()
            .take(optionCount - 1)

        // Build options
        val options = buildOptions(quizType, correctAnimal, wrongAnimals, isHebrew)

        // Build question text
        val questionText = buildQuestionText(quizType, correctAnimal, isHebrew)

        _uiState.update {
            it.copy(
                currentAnimal = correctAnimal,
                questionText = questionText,
                options = options
            )
        }
    }

    private fun buildOptions(
        quizType: QuizType,
        correct: Animal,
        wrong: List<Animal>,
        isHebrew: Boolean
    ): List<QuizOption> {
        val options = mutableListOf<QuizOption>()

        when (quizType) {
            QuizType.PICTURE, QuizType.SOUND, QuizType.RIDDLE -> {
                // Options are animal names
                options.add(
                    QuizOption(
                        text = if (isHebrew) correct.nameHe else correct.nameEn,
                        animal = correct,
                        isCorrect = true
                    )
                )
                wrong.forEach { animal ->
                    options.add(
                        QuizOption(
                            text = if (isHebrew) animal.nameHe else animal.nameEn,
                            animal = animal,
                            isCorrect = false
                        )
                    )
                }
            }
            QuizType.DIET -> {
                // Options are diet answers
                options.add(
                    QuizOption(
                        text = if (isHebrew) correct.dietHe else correct.dietEn,
                        animal = correct,
                        isCorrect = true
                    )
                )
                wrong.forEach { animal ->
                    options.add(
                        QuizOption(
                            text = if (isHebrew) animal.dietHe else animal.dietEn,
                            animal = animal,
                            isCorrect = false
                        )
                    )
                }
            }
            QuizType.HABITAT -> {
                // Options are habitat answers
                options.add(
                    QuizOption(
                        text = if (isHebrew) correct.habitatHe else correct.habitatEn,
                        animal = correct,
                        isCorrect = true
                    )
                )
                wrong.forEach { animal ->
                    options.add(
                        QuizOption(
                            text = if (isHebrew) animal.habitatHe else animal.habitatEn,
                            animal = animal,
                            isCorrect = false
                        )
                    )
                }
            }
            QuizType.REVERSE -> {
                // Options are animal images (AnimalCard)
                options.add(
                    QuizOption(
                        text = if (isHebrew) correct.nameHe else correct.nameEn,
                        animal = correct,
                        isCorrect = true
                    )
                )
                wrong.forEach { animal ->
                    options.add(
                        QuizOption(
                            text = if (isHebrew) animal.nameHe else animal.nameEn,
                            animal = animal,
                            isCorrect = false
                        )
                    )
                }
            }
        }

        return options.shuffled()
    }

    private fun buildQuestionText(
        quizType: QuizType,
        animal: Animal,
        isHebrew: Boolean
    ): String {
        val name = if (isHebrew) animal.nameHe else animal.nameEn
        val context = getApplication<Application>()

        return when (quizType) {
            QuizType.PICTURE -> context.getString(
                com.animalfun.R.string.quiz_question_picture, name
            )
            QuizType.SOUND -> context.getString(
                com.animalfun.R.string.quiz_question_sound
            )
            QuizType.RIDDLE -> if (isHebrew) animal.riddleHe else animal.riddleEn
            QuizType.DIET -> context.getString(
                com.animalfun.R.string.quiz_question_diet, name
            )
            QuizType.HABITAT -> context.getString(
                com.animalfun.R.string.quiz_question_habitat, name
            )
            QuizType.REVERSE -> if (isHebrew) animal.riddleHe.ifBlank {
                context.getString(com.animalfun.R.string.quiz_question_reverse, animal.nameHe)
            } else animal.riddleEn.ifBlank {
                context.getString(com.animalfun.R.string.quiz_question_reverse, animal.nameEn)
            }
        }
    }

    private fun calculateStars(score: Int, total: Int): Int {
        if (total == 0) return 0
        val percentage = score.toFloat() / total
        return when {
            percentage >= 1.0f -> 3
            percentage >= 0.7f -> 2
            percentage >= 0.4f -> 1
            else -> 0
        }
    }

    private fun saveProgress() {
        viewModelScope.launch {
            val state = _uiState.value
            // Update progress for each animal used in this quiz
            quizAnimals.forEach { animal ->
                val existing = repository.getUserProgress(animal.id).first()
                val updated = existing?.copy(
                    quizCorrect = existing.quizCorrect + if (quizAnimals.indexOf(animal) < state.score) 1 else 0,
                    quizTotal = existing.quizTotal + 1,
                    starsEarned = existing.starsEarned + state.starsEarned,
                    lastInteraction = System.currentTimeMillis()
                ) ?: UserProgress(
                    animalId = animal.id,
                    quizCorrect = if (quizAnimals.indexOf(animal) < state.score) 1 else 0,
                    quizTotal = 1,
                    starsEarned = state.starsEarned,
                    lastInteraction = System.currentTimeMillis()
                )
                repository.upsertProgress(updated)
            }
        }
    }
}
