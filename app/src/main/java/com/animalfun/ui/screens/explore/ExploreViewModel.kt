package com.animalfun.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animalfun.data.model.Animal
import com.animalfun.data.repository.AnimalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class ExploreViewModel(
    private val repository: AnimalRepository
) : ViewModel() {

    companion object {
        const val ALL_CATEGORY = "All"
        val CATEGORIES = listOf(ALL_CATEGORY, "Farm", "Jungle", "Ocean", "Birds", "Pets", "Insects")
    }

    private val _selectedCategory = MutableStateFlow(ALL_CATEGORY)
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val animals: StateFlow<List<Animal>> = _selectedCategory
        .flatMapLatest { category ->
            if (category == ALL_CATEGORY) {
                repository.getAllAnimals()
            } else {
                repository.getAnimalsByCategory(category)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }
}
