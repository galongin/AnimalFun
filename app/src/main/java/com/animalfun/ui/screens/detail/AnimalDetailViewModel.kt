package com.animalfun.ui.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.animalfun.data.model.Animal
import com.animalfun.data.model.UserProgress
import com.animalfun.data.repository.AnimalRepository
import com.animalfun.util.AudioPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AnimalDetailViewModel(
    application: Application,
    private val repository: AnimalRepository,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val animalId: Int = savedStateHandle["animalId"] ?: 0

    private val _animal = MutableStateFlow<Animal?>(null)
    val animal: StateFlow<Animal?> = _animal.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val audioPlayer = AudioPlayer(application)

    init {
        loadAnimal()
    }

    private fun loadAnimal() {
        viewModelScope.launch {
            _isLoading.value = true
            val loaded = repository.getAnimalById(animalId)
            _animal.value = loaded
            _isLoading.value = false

            // Mark this animal as explored in user progress
            if (loaded != null) {
                markExplored(loaded.id)
            }
        }
    }

    private suspend fun markExplored(id: Int) {
        val existing = repository.getUserProgress(id).first()
        if (existing == null) {
            repository.upsertProgress(
                UserProgress(
                    animalId = id,
                    explored = true,
                    starsEarned = 1, // earn a star for exploring
                    lastInteraction = System.currentTimeMillis()
                )
            )
        } else if (!existing.explored) {
            repository.upsertProgress(
                existing.copy(
                    explored = true,
                    starsEarned = existing.starsEarned + 1,
                    lastInteraction = System.currentTimeMillis()
                )
            )
        }
    }

    fun playSound() {
        _animal.value?.soundRes?.let { audioPlayer.play(it) }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
