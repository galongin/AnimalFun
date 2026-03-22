package com.animalfun.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quiz_questions",
    foreignKeys = [
        ForeignKey(
            entity = Animal::class,
            parentColumns = ["id"],
            childColumns = ["animal_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["animal_id"])]
)
data class QuizQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "animal_id")
    val animalId: Int,

    @ColumnInfo(name = "question_type")
    val questionType: String, // PICTURE, SOUND, RIDDLE, DIET, HABITAT, REVERSE

    @ColumnInfo(name = "question_text_en")
    val questionTextEn: String,

    @ColumnInfo(name = "question_text_he")
    val questionTextHe: String,

    @ColumnInfo(name = "correct_answer_en")
    val correctAnswerEn: String,

    @ColumnInfo(name = "correct_answer_he")
    val correctAnswerHe: String,

    @ColumnInfo(name = "wrong_answers_en")
    val wrongAnswersEn: List<String>, // stored as JSON via TypeConverter

    @ColumnInfo(name = "wrong_answers_he")
    val wrongAnswersHe: List<String>  // stored as JSON via TypeConverter
)
