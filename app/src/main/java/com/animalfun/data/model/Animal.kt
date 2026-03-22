package com.animalfun.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animals")
data class Animal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name_en")
    val nameEn: String,

    @ColumnInfo(name = "name_he")
    val nameHe: String,

    @ColumnInfo(name = "category")
    val category: String, // Farm, Jungle, Ocean, Birds, Pets, Insects

    @ColumnInfo(name = "image_res")
    val imageRes: String, // drawable resource name

    @ColumnInfo(name = "sound_res")
    val soundRes: String, // raw resource name

    @ColumnInfo(name = "facts_en")
    val factsEn: List<String>, // stored as JSON via TypeConverter

    @ColumnInfo(name = "facts_he")
    val factsHe: List<String>, // stored as JSON via TypeConverter

    @ColumnInfo(name = "riddle_en")
    val riddleEn: String = "",

    @ColumnInfo(name = "riddle_he")
    val riddleHe: String = "",

    @ColumnInfo(name = "diet_en")
    val dietEn: String = "",

    @ColumnInfo(name = "diet_he")
    val dietHe: String = "",

    @ColumnInfo(name = "habitat_en")
    val habitatEn: String = "",

    @ColumnInfo(name = "habitat_he")
    val habitatHe: String = "",

    @ColumnInfo(name = "size_info")
    val sizeInfo: String = ""
)
