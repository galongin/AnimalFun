package com.animalfun.data.repository

import com.animalfun.data.local.AnimalDao
import com.animalfun.data.local.ProgressDao
import com.animalfun.data.model.UserProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Data class holding explored / total counts for a single category.
 */
data class CategoryProgress(
    val category: String,
    val explored: Int,
    val total: Int,
    val stars: Int
)

/**
 * Repository that aggregates progress information from [ProgressDao]
 * and [AnimalDao], adding lightweight business logic on top.
 */
class ProgressRepository(
    private val progressDao: ProgressDao,
    private val animalDao: AnimalDao
) {

    // ── Delegated queries ──

    fun getExploredAnimalsCount(): Flow<Int> =
        progressDao.getExploredAnimalsCount()

    fun getQuizAccuracy(): Flow<Float> =
        progressDao.getQuizAccuracy()

    fun getTotalQuizAnswers(): Flow<Int> =
        progressDao.getTotalQuizAnswers()

    fun getRecentActivity(limit: Int = 5): Flow<List<UserProgress>> =
        progressDao.getRecentActivity(limit)

    fun getTotalStars(): Flow<Int> =
        animalDao.getTotalStars()

    // ── Category progress ──

    /**
     * Returns a [Flow] of [CategoryProgress] for the given category.
     * Combines three underlying queries so the result updates reactively.
     */
    fun getCategoryProgress(category: String): Flow<CategoryProgress> =
        combine(
            progressDao.getExploredCountByCategory(category),
            progressDao.getTotalCountByCategory(category),
            progressDao.getStarsByCategory(category)
        ) { explored, total, stars ->
            CategoryProgress(
                category = category,
                explored = explored,
                total = total,
                stars = stars
            )
        }

    /**
     * Returns progress for every predefined category in a single [Flow].
     */
    fun getAllCategoryProgress(): Flow<List<CategoryProgress>> {
        val categories = listOf("Farm", "Jungle", "Ocean", "Birds", "Pets", "Insects")
        val flows = categories.map { getCategoryProgress(it) }

        // Combine all six flows into a single list emission.
        return combine(flows) { array -> array.toList() }
    }

    // ── Business-logic helpers ──

    /**
     * Overall completion percentage (explored animals / total animals).
     * Returns a value between 0f and 100f.
     */
    suspend fun clearAllProgress() = animalDao.deleteAllProgress()

    fun getCompletionPercentage(): Flow<Float> =
        combine(
            progressDao.getExploredAnimalsCount(),
            // Total animal count: reuse category counts for all categories summed,
            // or simply query all animals.
            animalDao.getAllAnimals()
        ) { explored, allAnimals ->
            if (allAnimals.isEmpty()) 0f
            else (explored.toFloat() / allAnimals.size) * 100f
        }
}
