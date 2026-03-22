package com.animalfun.data.local

import androidx.room.Dao
import androidx.room.Query
import com.animalfun.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

/**
 * DAO for progress-tracking queries that complement the basic CRUD in [AnimalDao].
 *
 * All queries return [Flow] so the UI recomposes automatically when the
 * underlying data changes.
 */
@Dao
interface ProgressDao {

    /** Number of distinct animals the user has explored. */
    @Query("SELECT COUNT(*) FROM user_progress WHERE explored = 1")
    fun getExploredAnimalsCount(): Flow<Int>

    /**
     * Overall quiz accuracy as a ratio (0.0 .. 1.0).
     * Returns 0 when no quiz answers have been recorded yet.
     */
    @Query(
        "SELECT CASE WHEN SUM(quiz_total) > 0 " +
        "THEN CAST(SUM(quiz_correct) AS REAL) / SUM(quiz_total) " +
        "ELSE 0.0 END FROM user_progress"
    )
    fun getQuizAccuracy(): Flow<Float>

    /** Total number of quiz answers given across all animals. */
    @Query("SELECT COALESCE(SUM(quiz_total), 0) FROM user_progress")
    fun getTotalQuizAnswers(): Flow<Int>

    /**
     * Returns the count of explored animals for a single category.
     *
     * Join with animals table to resolve the category from the foreign key.
     */
    @Query(
        "SELECT COUNT(*) FROM user_progress " +
        "INNER JOIN animals ON user_progress.animal_id = animals.id " +
        "WHERE user_progress.explored = 1 AND animals.category = :category"
    )
    fun getExploredCountByCategory(category: String): Flow<Int>

    /** Total number of animals in a given category. */
    @Query("SELECT COUNT(*) FROM animals WHERE category = :category")
    fun getTotalCountByCategory(category: String): Flow<Int>

    /**
     * Most recently interacted animals, ordered newest first.
     */
    @Query("SELECT * FROM user_progress ORDER BY last_interaction DESC LIMIT :limit")
    fun getRecentActivity(limit: Int): Flow<List<UserProgress>>

    /** Sum of stars earned for animals in a given category. */
    @Query(
        "SELECT COALESCE(SUM(up.stars_earned), 0) FROM user_progress up " +
        "INNER JOIN animals ON up.animal_id = animals.id " +
        "WHERE animals.category = :category"
    )
    fun getStarsByCategory(category: String): Flow<Int>
}
