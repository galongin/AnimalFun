package com.animalfun.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_progress",
    foreignKeys = [
        ForeignKey(
            entity = Animal::class,
            parentColumns = ["id"],
            childColumns = ["animal_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["animal_id"], unique = true)]
)
data class UserProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "animal_id")
    val animalId: Int,

    @ColumnInfo(name = "explored")
    val explored: Boolean = false,

    @ColumnInfo(name = "quiz_correct")
    val quizCorrect: Int = 0,

    @ColumnInfo(name = "quiz_total")
    val quizTotal: Int = 0,

    @ColumnInfo(name = "best_memory_time")
    val bestMemoryTime: Long? = null,

    @ColumnInfo(name = "stars_earned")
    val starsEarned: Int = 0,

    @ColumnInfo(name = "last_interaction")
    val lastInteraction: Long = System.currentTimeMillis()
)
