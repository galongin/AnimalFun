package com.animalfun.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.animalfun.data.repository.AnimalRepository
import com.animalfun.data.repository.CategoryProgress
import com.animalfun.data.repository.ProgressRepository
import com.animalfun.util.LocaleHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val repository: AnimalRepository,
    private val progressRepository: ProgressRepository
) : AndroidViewModel(application) {

    /** Total stars earned across all progress entries. */
    val totalStars: StateFlow<Int> = progressRepository.getTotalStars()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** Number of animals the user has explored. */
    val exploredCount: StateFlow<Int> = progressRepository.getExploredAnimalsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** Overall quiz accuracy (0.0 .. 1.0). */
    val quizAccuracy: StateFlow<Float> = progressRepository.getQuizAccuracy()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0f)

    /** Total number of quiz answers the user has given. */
    val totalQuizAnswers: StateFlow<Int> = progressRepository.getTotalQuizAnswers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** Overall completion percentage (0 .. 100). */
    val completionPercentage: StateFlow<Float> = progressRepository.getCompletionPercentage()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0f)

    /** Per-category progress (explored/total + stars). */
    val categoryProgress: StateFlow<List<CategoryProgress>> =
        progressRepository.getAllCategoryProgress()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Whether the user has any recent activity to show. */
    val hasRecentActivity: StateFlow<Boolean> = MutableStateFlow(false)

    private val _currentLanguage = MutableStateFlow(LocaleHelper.getCurrentLanguage())
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    init {
        // Sync initial language from DataStore
        viewModelScope.launch {
            LocaleHelper.getLanguageFlow(getApplication()).collect { lang ->
                _currentLanguage.value = lang
            }
        }
        // Track whether there is recent activity
        viewModelScope.launch {
            progressRepository.getRecentActivity(1).collect { list ->
                (hasRecentActivity as MutableStateFlow).value = list.isNotEmpty()
            }
        }
    }

    /**
     * Toggles between English and Hebrew, persists the choice,
     * and applies the new locale immediately.
     */
    fun toggleLanguage() {
        viewModelScope.launch {
            val newLang = if (_currentLanguage.value == LocaleHelper.ENGLISH) {
                LocaleHelper.HEBREW
            } else {
                LocaleHelper.ENGLISH
            }
            LocaleHelper.setLanguage(getApplication(), newLang)
            _currentLanguage.value = newLang
        }
    }
}
