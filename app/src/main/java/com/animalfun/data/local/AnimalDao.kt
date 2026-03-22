package com.animalfun.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.animalfun.data.model.Animal
import com.animalfun.data.model.QuizQuestion
import com.animalfun.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {

    // ── Animal queries ──

    @Query("SELECT * FROM animals ORDER BY name_en ASC")
    fun getAllAnimals(): Flow<List<Animal>>

    @Query("SELECT * FROM animals WHERE category = :category ORDER BY name_en ASC")
    fun getAnimalsByCategory(category: String): Flow<List<Animal>>

    @Query("SELECT * FROM animals WHERE id = :animalId")
    suspend fun getAnimalById(animalId: Int): Animal?

    @Query(
        "SELECT * FROM animals WHERE name_en LIKE '%' || :query || '%' " +
        "OR name_he LIKE '%' || :query || '%' ORDER BY name_en ASC"
    )
    fun searchAnimals(query: String): Flow<List<Animal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimal(animal: Animal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimals(animals: List<Animal>)

    // ── Quiz queries ──

    @Query(
        "SELECT * FROM quiz_questions WHERE question_type = :type " +
        "ORDER BY RANDOM() LIMIT :count"
    )
    suspend fun getQuizQuestions(type: String, count: Int): List<QuizQuestion>

    @Query("SELECT * FROM quiz_questions WHERE animal_id = :animalId")
    suspend fun getQuizQuestionsForAnimal(animalId: Int): List<QuizQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(questions: List<QuizQuestion>)

    // ── UserProgress queries ──

    @Query("SELECT * FROM user_progress WHERE animal_id = :animalId")
    fun getUserProgress(animalId: Int): Flow<UserProgress?>

    @Query("SELECT * FROM user_progress ORDER BY last_interaction DESC")
    fun getAllProgress(): Flow<List<UserProgress>>

    @Upsert
    suspend fun upsertProgress(progress: UserProgress)

    @Query("SELECT COALESCE(SUM(stars_earned), 0) FROM user_progress")
    fun getTotalStars(): Flow<Int>

    @Query("DELETE FROM user_progress")
    suspend fun deleteAllProgress()
}
