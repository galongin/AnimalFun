package com.animalfun.data.repository

import com.animalfun.data.local.AnimalDao
import com.animalfun.data.model.Animal
import com.animalfun.data.model.QuizQuestion
import com.animalfun.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

class AnimalRepository(private val animalDao: AnimalDao) {

    // ── Animal operations ──

    fun getAllAnimals(): Flow<List<Animal>> = animalDao.getAllAnimals()

    fun getAnimalsByCategory(category: String): Flow<List<Animal>> =
        animalDao.getAnimalsByCategory(category)

    suspend fun getAnimalById(animalId: Int): Animal? =
        animalDao.getAnimalById(animalId)

    fun searchAnimals(query: String): Flow<List<Animal>> =
        animalDao.searchAnimals(query)

    suspend fun insertAnimal(animal: Animal) = animalDao.insertAnimal(animal)

    suspend fun insertAnimals(animals: List<Animal>) = animalDao.insertAnimals(animals)

    // ── Quiz operations ──

    suspend fun getQuizQuestions(type: String, count: Int): List<QuizQuestion> =
        animalDao.getQuizQuestions(type, count)

    suspend fun getQuizQuestionsForAnimal(animalId: Int): List<QuizQuestion> =
        animalDao.getQuizQuestionsForAnimal(animalId)

    suspend fun insertQuizQuestions(questions: List<QuizQuestion>) =
        animalDao.insertQuizQuestions(questions)

    // ── UserProgress operations ──

    fun getUserProgress(animalId: Int): Flow<UserProgress?> =
        animalDao.getUserProgress(animalId)

    fun getAllProgress(): Flow<List<UserProgress>> =
        animalDao.getAllProgress()

    suspend fun upsertProgress(progress: UserProgress) =
        animalDao.upsertProgress(progress)

    fun getTotalStars(): Flow<Int> =
        animalDao.getTotalStars()

    suspend fun deleteAllProgress() = animalDao.deleteAllProgress()
}
