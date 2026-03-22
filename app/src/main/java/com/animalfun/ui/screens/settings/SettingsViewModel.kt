package com.animalfun.ui.screens.settings

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.animalfun.data.repository.AnimalRepository
import com.animalfun.util.LocaleHelper
import com.animalfun.util.settingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class SettingsUiState(
    val isParentalGateUnlocked: Boolean = false,
    val currentLanguage: String = LocaleHelper.ENGLISH,
    val isSoundEnabled: Boolean = true,
    val showResetDialog: Boolean = false,
    val mathQuestion: String = "",
    val mathAnswer: Int = 0,
    val userAnswer: String = "",
    val showWrongAnswer: Boolean = false
)

class SettingsViewModel(
    application: Application,
    private val animalRepository: AnimalRepository
) : AndroidViewModel(application) {

    companion object {
        private val SOUND_ENABLED_KEY = booleanPreferencesKey("sound_enabled")
    }

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        generateMathQuestion()

        // Sync language from DataStore
        viewModelScope.launch {
            LocaleHelper.getLanguageFlow(getApplication()).collect { lang ->
                _uiState.update { it.copy(currentLanguage = lang) }
            }
        }

        // Sync sound preference from DataStore
        viewModelScope.launch {
            getApplication<Application>().settingsDataStore.data.collect { prefs ->
                val soundEnabled = prefs[SOUND_ENABLED_KEY] ?: true
                _uiState.update { it.copy(isSoundEnabled = soundEnabled) }
            }
        }
    }

    private fun generateMathQuestion() {
        val isAddition = Random.nextBoolean()
        val answer: Int
        val question: String

        if (isAddition) {
            val a = Random.nextInt(5, 25)
            val b = Random.nextInt(5, 20)
            answer = a + b
            question = "$a + $b = ?"
        } else {
            val answer0 = Random.nextInt(10, 30)
            val b = Random.nextInt(3, answer0 - 5)
            answer = answer0 - b
            question = "$answer0 - $b = ?"
        }

        _uiState.update {
            it.copy(
                mathQuestion = question,
                mathAnswer = answer,
                userAnswer = "",
                showWrongAnswer = false
            )
        }
    }

    fun onUserAnswerChange(value: String) {
        // Only allow digits
        val filtered = value.filter { it.isDigit() }
        _uiState.update { it.copy(userAnswer = filtered, showWrongAnswer = false) }
    }

    fun validateParentalGate(): Boolean {
        val typed = _uiState.value.userAnswer.toIntOrNull()
        return if (typed == _uiState.value.mathAnswer) {
            _uiState.update { it.copy(isParentalGateUnlocked = true, showWrongAnswer = false) }
            true
        } else {
            _uiState.update { it.copy(showWrongAnswer = true, userAnswer = "") }
            generateMathQuestion()
            false
        }
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val newLang = if (_uiState.value.currentLanguage == LocaleHelper.ENGLISH) {
                LocaleHelper.HEBREW
            } else {
                LocaleHelper.ENGLISH
            }
            LocaleHelper.setLanguage(getApplication(), newLang)
            _uiState.update { it.copy(currentLanguage = newLang) }
        }
    }

    fun toggleSound() {
        viewModelScope.launch {
            val newValue = !_uiState.value.isSoundEnabled
            getApplication<Application>().settingsDataStore.edit { prefs ->
                prefs[SOUND_ENABLED_KEY] = newValue
            }
            _uiState.update { it.copy(isSoundEnabled = newValue) }
        }
    }

    fun showResetDialog() {
        _uiState.update { it.copy(showResetDialog = true) }
    }

    fun dismissResetDialog() {
        _uiState.update { it.copy(showResetDialog = false) }
    }

    fun resetProgress() {
        viewModelScope.launch {
            animalRepository.deleteAllProgress()
            _uiState.update { it.copy(showResetDialog = false) }
        }
    }

    fun lockParentalGate() {
        generateMathQuestion()
        _uiState.update {
            it.copy(isParentalGateUnlocked = false)
        }
    }
}
