package com.animalfun.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.animalfun.data.model.Animal
import com.animalfun.data.model.QuizQuestion
import com.animalfun.data.model.UserProgress
import org.json.JSONArray

@Database(
    entities = [Animal::class, QuizQuestion::class, UserProgress::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AnimalDatabase : RoomDatabase() {

    abstract fun animalDao(): AnimalDao

    abstract fun progressDao(): ProgressDao

    companion object {
        fun buildDatabase(context: Context): AnimalDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AnimalDatabase::class.java,
                "animal_fun.db"
            )
                .addCallback(SeedDatabaseCallback())
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    /**
     * Callback that seeds the database with initial animal data on first creation.
     */
    internal class SeedDatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Seed is performed via raw SQL so it runs inside the Room creation transaction.
            seedAnimals(db)
            seedQuizQuestions(db)
            // Seed remaining 40 animals and their quiz questions
            SeedData.allNewAnimalSql().forEach { db.execSQL(it) }
            SeedData.allNewQuizSql().forEach { db.execSQL(it) }
        }

        private fun toJson(items: List<String>): String {
            val arr = JSONArray()
            items.forEach { arr.put(it) }
            return arr.toString()
        }

        private fun seedAnimals(db: SupportSQLiteDatabase) {
            data class Seed(
                val nameEn: String, val nameHe: String, val category: String,
                val imageRes: String, val soundRes: String,
                val factsEn: List<String>, val factsHe: List<String>,
                val riddleEn: String, val riddleHe: String,
                val dietEn: String, val dietHe: String,
                val habitatEn: String, val habitatHe: String,
                val sizeInfo: String
            )

            val animals = listOf(
                // 1. Cow (Farm)
                Seed(
                    "Cow", "\u05E4\u05E8\u05D4", "Farm",
                    "animal_cow", "silence",
                    listOf(
                        "Cows have best friends and get stressed when separated.",
                        "A cow gives about 200,000 glasses of milk in her lifetime.",
                        "Cows have almost 360-degree vision.",
                        "Cows can smell things up to 10 km away."
                    ),
                    listOf(
                        "\u05DC\u05E4\u05E8\u05D5\u05EA \u05D9\u05E9 \u05D7\u05D1\u05E8\u05D5\u05EA \u05D4\u05DB\u05D9 \u05D8\u05D5\u05D1\u05D5\u05EA \u05D5\u05D4\u05DF \u05E0\u05DC\u05D7\u05E6\u05D5\u05EA \u05DB\u05E9\u05DE\u05E4\u05E8\u05D9\u05D3\u05D9\u05DD \u05D0\u05D5\u05EA\u05DF.",
                        "\u05E4\u05E8\u05D4 \u05E0\u05D5\u05EA\u05E0\u05EA \u05DB-200,000 \u05DB\u05D5\u05E1\u05D5\u05EA \u05D7\u05DC\u05D1 \u05D1\u05D7\u05D9\u05D9\u05D4.",
                        "\u05DC\u05E4\u05E8\u05D5\u05EA \u05D9\u05E9 \u05E9\u05D3\u05D4 \u05E8\u05D0\u05D9\u05D9\u05D4 \u05E9\u05DC \u05DB\u05DE\u05E2\u05D8 360 \u05DE\u05E2\u05DC\u05D5\u05EA.",
                        "\u05E4\u05E8\u05D5\u05EA \u05D9\u05DB\u05D5\u05DC\u05D5\u05EA \u05DC\u05D4\u05E8\u05D9\u05D7 \u05D3\u05D1\u05E8\u05D9\u05DD \u05DE\u05DE\u05E8\u05D7\u05E7 \u05E9\u05DC 10 \u05E7\u05DE."
                    ),
                    "I say moo and give you milk. Who am I?",
                    "\u05D0\u05E0\u05D9 \u05D0\u05D5\u05DE\u05E8\u05EA \u05DE\u05D5\u05D5 \u05D5\u05E0\u05D5\u05EA\u05E0\u05EA \u05D7\u05DC\u05D1. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Grass and hay", "\u05E2\u05E9\u05D1 \u05D5\u05D7\u05E6\u05D9\u05E8",
                    "Farms and green pastures", "\u05D7\u05D5\u05D5\u05EA \u05D5\u05DE\u05E8\u05E2\u05D9\u05DD \u05D9\u05E8\u05D5\u05E7\u05D9\u05DD",
                    "Large \u2013 about 1.5 m tall"
                ),
                // 2. Chicken (Farm)
                Seed(
                    "Chicken", "\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA", "Farm",
                    "animal_chicken", "silence",
                    listOf(
                        "Chickens can remember over 100 different faces.",
                        "There are more chickens on Earth than people!",
                        "A mother hen turns her eggs about 50 times a day.",
                        "Chickens can see more colors than humans."
                    ),
                    listOf(
                        "\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05D5\u05EA \u05D6\u05D5\u05DB\u05E8\u05D5\u05EA \u05D9\u05D5\u05EA\u05E8 \u05DE-100 \u05E4\u05E0\u05D9\u05DD \u05E9\u05D5\u05E0\u05D9\u05DD.",
                        "\u05D9\u05E9 \u05D9\u05D5\u05EA\u05E8 \u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05D5\u05EA \u05DE\u05D0\u05E0\u05E9\u05D9\u05DD \u05D1\u05E2\u05D5\u05DC\u05DD!",
                        "\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA \u05D0\u05DE\u05D0 \u05D4\u05D5\u05E4\u05DB\u05EA \u05D0\u05EA \u05D4\u05D1\u05D9\u05E6\u05D9\u05DD \u05E9\u05DC\u05D4 \u05DB-50 \u05E4\u05E2\u05DD \u05D1\u05D9\u05D5\u05DD.",
                        "\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05D5\u05EA \u05E8\u05D5\u05D0\u05D5\u05EA \u05D9\u05D5\u05EA\u05E8 \u05E6\u05D1\u05E2\u05D9\u05DD \u05DE\u05D1\u05E0\u05D9 \u05D0\u05D3\u05DD."
                    ),
                    "I lay eggs and cluck all day. Who am I?",
                    "\u05D0\u05E0\u05D9 \u05DE\u05D8\u05D9\u05DC\u05D4 \u05D1\u05D9\u05E6\u05D9\u05DD \u05D5\u05DE\u05E7\u05E8\u05E7\u05E8\u05EA \u05DB\u05DC \u05D4\u05D9\u05D5\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Seeds, grains, and insects", "\u05D6\u05E8\u05E2\u05D9\u05DD, \u05D2\u05E8\u05E2\u05D9\u05E0\u05D9\u05DD \u05D5\u05D7\u05E8\u05E7\u05D9\u05DD",
                    "Farms and coops", "\u05D7\u05D5\u05D5\u05EA \u05D5\u05DC\u05D5\u05DC\u05D9\u05DD",
                    "Small \u2013 about 40 cm tall"
                ),
                // 3. Lion (Jungle)
                Seed(
                    "Lion", "\u05D0\u05E8\u05D9\u05D4", "Jungle",
                    "animal_lion", "silence",
                    listOf(
                        "A lion's roar can be heard from 8 km away!",
                        "Lions sleep up to 20 hours a day.",
                        "Lions are the only cats that live in groups called prides.",
                        "A lion's mane makes it look bigger and stronger."
                    ),
                    listOf(
                        "\u05E9\u05D0\u05D2\u05EA \u05D0\u05E8\u05D9\u05D4 \u05E0\u05E9\u05DE\u05E2\u05EA \u05DE\u05DE\u05E8\u05D7\u05E7 \u05E9\u05DC 8 \u05E7\u05DE!",
                        "\u05D0\u05E8\u05D9\u05D5\u05EA \u05D9\u05E9\u05E0\u05D9\u05DD \u05E2\u05D3 20 \u05E9\u05E2\u05D5\u05EA \u05D1\u05D9\u05D5\u05DD.",
                        "\u05D0\u05E8\u05D9\u05D5\u05EA \u05D4\u05DD \u05D4\u05D7\u05EA\u05D5\u05DC\u05D9\u05DD \u05D4\u05D9\u05D7\u05D9\u05D3\u05D9\u05DD \u05E9\u05D7\u05D9\u05D9\u05DD \u05D1\u05E7\u05D1\u05D5\u05E6\u05D5\u05EA \u05E9\u05E0\u05E7\u05E8\u05D0\u05D5\u05EA \u05DC\u05D4\u05E7\u05D4.",
                        "\u05D4\u05E8\u05E2\u05DE\u05D4 \u05E9\u05DC \u05D4\u05D0\u05E8\u05D9\u05D4 \u05D2\u05D5\u05E8\u05DE\u05EA \u05DC\u05D5 \u05DC\u05D4\u05D9\u05E8\u05D0\u05D5\u05EA \u05D2\u05D3\u05D5\u05DC \u05D5\u05D7\u05D6\u05E7 \u05D9\u05D5\u05EA\u05E8."
                    ),
                    "I am called the king of the jungle and I have a big mane. Who am I?",
                    "\u05E7\u05D5\u05E8\u05D0\u05D9\u05DD \u05DC\u05D9 \u05DE\u05DC\u05DA \u05D4\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC \u05D5\u05D9\u05E9 \u05DC\u05D9 \u05E8\u05E2\u05DE\u05D4 \u05D2\u05D3\u05D5\u05DC\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Meat (zebras, antelopes)", "\u05D1\u05E9\u05E8 (\u05D6\u05D1\u05E8\u05D5\u05EA, \u05D0\u05E0\u05D8\u05D9\u05DC\u05D5\u05E4\u05D5\u05EA)",
                    "African savanna and grasslands", "\u05E1\u05D0\u05D5\u05D5\u05E0\u05D4 \u05D5\u05E2\u05E8\u05D1\u05D5\u05EA \u05D1\u05D0\u05E4\u05E8\u05D9\u05E7\u05D4",
                    "Large \u2013 about 1.2 m tall at the shoulder"
                ),
                // 4. Elephant (Jungle)
                Seed(
                    "Elephant", "\u05E4\u05D9\u05DC", "Jungle",
                    "animal_elephant", "silence",
                    listOf(
                        "Elephants have amazing memories and never forget a friend.",
                        "Baby elephants suck their trunks just like babies suck their thumbs.",
                        "An elephant's trunk has over 40,000 muscles!",
                        "Elephants are afraid of bees.",
                        "Elephants can communicate using sounds too low for humans to hear."
                    ),
                    listOf(
                        "\u05DC\u05E4\u05D9\u05DC\u05D9\u05DD \u05D9\u05E9 \u05D6\u05D9\u05DB\u05E8\u05D5\u05DF \u05DE\u05D3\u05D4\u05D9\u05DD \u05D5\u05D4\u05DD \u05D0\u05E3 \u05E4\u05E2\u05DD \u05DC\u05D0 \u05E9\u05D5\u05DB\u05D7\u05D9\u05DD \u05D7\u05D1\u05E8.",
                        "\u05E4\u05D9\u05DC\u05D5\u05E0\u05D9\u05DD \u05DE\u05D5\u05E6\u05E6\u05D9\u05DD \u05D0\u05EA \u05D4\u05D7\u05D3\u05E7 \u05E9\u05DC\u05D4\u05DD \u05D1\u05D3\u05D9\u05D5\u05E7 \u05DB\u05DE\u05D5 \u05E9\u05EA\u05D9\u05E0\u05D5\u05E7\u05D5\u05EA \u05DE\u05D5\u05E6\u05E6\u05D9\u05DD \u05D0\u05E6\u05D1\u05E2.",
                        "\u05D1\u05D7\u05D3\u05E7 \u05E9\u05DC \u05E4\u05D9\u05DC \u05D9\u05E9 \u05D9\u05D5\u05EA\u05E8 \u05DE-40,000 \u05E9\u05E8\u05D9\u05E8\u05D9\u05DD!",
                        "\u05E4\u05D9\u05DC\u05D9\u05DD \u05DE\u05E4\u05D7\u05D3\u05D9\u05DD \u05DE\u05D3\u05D1\u05D5\u05E8\u05D9\u05DD.",
                        "\u05E4\u05D9\u05DC\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05EA\u05E7\u05E9\u05E8 \u05D1\u05E6\u05DC\u05D9\u05DC\u05D9\u05DD \u05E0\u05DE\u05D5\u05DB\u05D9\u05DD \u05DE\u05D3\u05D9 \u05E9\u05D0\u05D5\u05D6\u05DF \u05D4\u05D0\u05D3\u05DD \u05DC\u05D0 \u05E9\u05D5\u05DE\u05E2\u05EA."
                    ),
                    "I have a long trunk, big ears, and I never forget. Who am I?",
                    "\u05D9\u05E9 \u05DC\u05D9 \u05D7\u05D3\u05E7 \u05D0\u05E8\u05D5\u05DA, \u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD \u05D2\u05D3\u05D5\u05DC\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05D0\u05E3 \u05E4\u05E2\u05DD \u05DC\u05D0 \u05E9\u05D5\u05DB\u05D7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Plants, fruits, and tree bark", "\u05E6\u05DE\u05D7\u05D9\u05DD, \u05E4\u05D9\u05E8\u05D5\u05EA \u05D5\u05E7\u05DC\u05D9\u05E4\u05EA \u05E2\u05E6\u05D9\u05DD",
                    "African and Asian forests and savannas", "\u05D9\u05E2\u05E8\u05D5\u05EA \u05D5\u05E1\u05D0\u05D5\u05D5\u05E0\u05D5\u05EA \u05D1\u05D0\u05E4\u05E8\u05D9\u05E7\u05D4 \u05D5\u05D0\u05E1\u05D9\u05D4",
                    "Very large \u2013 about 3 m tall"
                ),
                // 5. Dolphin (Ocean)
                Seed(
                    "Dolphin", "\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF", "Ocean",
                    "animal_dolphin", "silence",
                    listOf(
                        "Dolphins sleep with one eye open to watch for danger.",
                        "Each dolphin has a unique whistle, like a name.",
                        "Dolphins love to play and surf on waves.",
                        "Dolphins are one of the smartest animals in the world."
                    ),
                    listOf(
                        "\u05D3\u05D5\u05DC\u05E4\u05D9\u05E0\u05D9\u05DD \u05D9\u05E9\u05E0\u05D9\u05DD \u05E2\u05DD \u05E2\u05D9\u05DF \u05D0\u05D7\u05EA \u05E4\u05EA\u05D5\u05D7\u05D4 \u05DB\u05D3\u05D9 \u05DC\u05D4\u05D9\u05E9\u05DE\u05E8 \u05DE\u05E1\u05DB\u05E0\u05D5\u05EA.",
                        "\u05DC\u05DB\u05DC \u05D3\u05D5\u05DC\u05E4\u05D9\u05DF \u05D9\u05E9 \u05E9\u05E8\u05D9\u05E7\u05D4 \u05D9\u05D9\u05D7\u05D5\u05D3\u05D9\u05EA, \u05DB\u05DE\u05D5 \u05E9\u05DD.",
                        "\u05D3\u05D5\u05DC\u05E4\u05D9\u05E0\u05D9\u05DD \u05D0\u05D5\u05D4\u05D1\u05D9\u05DD \u05DC\u05E9\u05D7\u05E7 \u05D5\u05DC\u05D2\u05DC\u05D5\u05E9 \u05E2\u05DC \u05D2\u05DC\u05D9\u05DD.",
                        "\u05D3\u05D5\u05DC\u05E4\u05D9\u05E0\u05D9\u05DD \u05D4\u05DD \u05DE\u05D4\u05D7\u05D9\u05D5\u05EA \u05D4\u05D7\u05DB\u05DE\u05D5\u05EA \u05D1\u05D9\u05D5\u05EA\u05E8 \u05D1\u05E2\u05D5\u05DC\u05DD."
                    ),
                    "I jump through waves, I love to play, and I live in the ocean. Who am I?",
                    "\u05D0\u05E0\u05D9 \u05E7\u05D5\u05E4\u05E5 \u05DE\u05D4\u05D2\u05DC\u05D9\u05DD, \u05D0\u05D5\u05D4\u05D1 \u05DC\u05E9\u05D7\u05E7 \u05D5\u05D7\u05D9 \u05D1\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Fish and squid", "\u05D3\u05D2\u05D9\u05DD \u05D5\u05D3\u05D9\u05D5\u05E0\u05D5\u05E0\u05D9\u05DD",
                    "Oceans and seas worldwide", "\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1\u05D9\u05DD \u05D5\u05D9\u05DE\u05D9\u05DD \u05D1\u05E8\u05D7\u05D1\u05D9 \u05D4\u05E2\u05D5\u05DC\u05DD",
                    "Medium \u2013 about 2\u20133 m long"
                ),
                // 6. Shark (Ocean)
                Seed(
                    "Shark", "\u05DB\u05E8\u05D9\u05E9", "Ocean",
                    "animal_shark", "silence",
                    listOf(
                        "Sharks have been around for over 400 million years \u2013 before the dinosaurs!",
                        "Sharks never run out of teeth. They can grow over 30,000 teeth in a lifetime!",
                        "Most sharks are not dangerous to people.",
                        "Whale sharks are the biggest fish in the ocean but eat only tiny plankton."
                    ),
                    listOf(
                        "\u05DB\u05E8\u05D9\u05E9\u05D9\u05DD \u05D7\u05D9\u05D9\u05DD \u05E2\u05DC \u05DB\u05D3\u05D5\u05E8 \u05D4\u05D0\u05E8\u05E5 \u05DB\u05D1\u05E8 \u05D9\u05D5\u05EA\u05E8 \u05DE-400 \u05DE\u05D9\u05DC\u05D9\u05D5\u05DF \u05E9\u05E0\u05D4 \u2013 \u05DC\u05E4\u05E0\u05D9 \u05D4\u05D3\u05D9\u05E0\u05D5\u05D6\u05D0\u05D5\u05E8\u05D9\u05DD!",
                        "\u05DC\u05DB\u05E8\u05D9\u05E9\u05D9\u05DD \u05D0\u05E3 \u05E4\u05E2\u05DD \u05DC\u05D0 \u05E0\u05D2\u05DE\u05E8\u05D5\u05EA \u05D4\u05E9\u05D9\u05E0\u05D9\u05D9\u05DD. \u05D4\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D2\u05D3\u05DC \u05DE\u05E2\u05DC 30,000 \u05E9\u05D9\u05E0\u05D9\u05D9\u05DD \u05D1\u05D7\u05D9\u05D9\u05D4\u05DD!",
                        "\u05E8\u05D5\u05D1 \u05D4\u05DB\u05E8\u05D9\u05E9\u05D9\u05DD \u05D0\u05D9\u05E0\u05DD \u05DE\u05E1\u05D5\u05DB\u05E0\u05D9\u05DD \u05DC\u05D1\u05E0\u05D9 \u05D0\u05D3\u05DD.",
                        "\u05DB\u05E8\u05D9\u05E9 \u05DC\u05D5\u05D5\u05D9\u05EA\u05DF \u05D4\u05D5\u05D0 \u05D4\u05D3\u05D2 \u05D4\u05DB\u05D9 \u05D2\u05D3\u05D5\u05DC \u05D1\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1 \u05D0\u05D1\u05DC \u05D0\u05D5\u05DB\u05DC \u05E8\u05E7 \u05E4\u05DC\u05E0\u05E7\u05D8\u05D5\u05DF \u05D6\u05E2\u05D9\u05E8."
                    ),
                    "I swim in the ocean, I have lots of sharp teeth, and I am very fast. Who am I?",
                    "\u05D0\u05E0\u05D9 \u05E9\u05D5\u05D7\u05D4 \u05D1\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1, \u05D9\u05E9 \u05DC\u05D9 \u05D4\u05E8\u05D1\u05D4 \u05E9\u05D9\u05E0\u05D9\u05D9\u05DD \u05D7\u05D3\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05DE\u05D4\u05D9\u05E8 \u05DE\u05D0\u05D5\u05D3. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Fish, seals, and sea creatures", "\u05D3\u05D2\u05D9\u05DD, \u05DB\u05DC\u05D1\u05D9 \u05D9\u05DD \u05D5\u05D9\u05E6\u05D5\u05E8\u05D9 \u05D9\u05DD",
                    "Oceans worldwide", "\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1\u05D9\u05DD \u05D1\u05E8\u05D7\u05D1\u05D9 \u05D4\u05E2\u05D5\u05DC\u05DD",
                    "Varies \u2013 from 20 cm to 12 m long"
                ),
                // 7. Eagle (Birds)
                Seed(
                    "Eagle", "\u05E0\u05E9\u05E8", "Birds",
                    "animal_eagle", "silence",
                    listOf(
                        "Eagles can spot a rabbit from over 3 km away!",
                        "Eagle nests can be huge \u2013 as heavy as a small car.",
                        "Eagles can fly at speeds over 150 km per hour when diving.",
                        "Some eagles mate for life and stay with the same partner forever."
                    ),
                    listOf(
                        "\u05E0\u05E9\u05E8\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D6\u05D4\u05D5\u05EA \u05D0\u05E8\u05E0\u05D1 \u05DE\u05DE\u05E8\u05D7\u05E7 \u05E9\u05DC \u05D9\u05D5\u05EA\u05E8 \u05DE-3 \u05E7\u05DE!",
                        "\u05E7\u05E0\u05D9 \u05E0\u05E9\u05E8\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D4\u05D9\u05D5\u05EA \u05E2\u05E0\u05E7\u05D9\u05D9\u05DD \u2013 \u05DB\u05D1\u05D3\u05D9\u05DD \u05DB\u05DE\u05D5 \u05DE\u05DB\u05D5\u05E0\u05D9\u05EA \u05E7\u05D8\u05E0\u05D4.",
                        "\u05E0\u05E9\u05E8\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E2\u05D5\u05E3 \u05D1\u05DE\u05D4\u05D9\u05E8\u05D5\u05EA \u05E9\u05DC \u05DE\u05E2\u05DC 150 \u05E7\u05DE\u05F4\u05E9 \u05D1\u05E6\u05DC\u05D9\u05DC\u05D4.",
                        "\u05D9\u05E9 \u05E0\u05E9\u05E8\u05D9\u05DD \u05E9\u05E0\u05E9\u05D0\u05E8\u05D9\u05DD \u05D6\u05D5\u05D2 \u05DC\u05DB\u05DC \u05D4\u05D7\u05D9\u05D9\u05DD."
                    ),
                    "I fly very high in the sky and have the sharpest eyes of any bird. Who am I?",
                    "\u05D0\u05E0\u05D9 \u05E2\u05E3 \u05D2\u05D1\u05D5\u05D4 \u05DE\u05D0\u05D5\u05D3 \u05D1\u05E9\u05DE\u05D9\u05D9\u05DD \u05D5\u05D9\u05E9 \u05DC\u05D9 \u05D0\u05EA \u05D4\u05E2\u05D9\u05E0\u05D9\u05D9\u05DD \u05D4\u05DB\u05D9 \u05D7\u05D3\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Fish, rabbits, and small animals", "\u05D3\u05D2\u05D9\u05DD, \u05D0\u05E8\u05E0\u05D1\u05D5\u05EA \u05D5\u05D7\u05D9\u05D5\u05EA \u05E7\u05D8\u05E0\u05D5\u05EA",
                    "Mountains, forests, and cliffs", "\u05D4\u05E8\u05D9\u05DD, \u05D9\u05E2\u05E8\u05D5\u05EA \u05D5\u05E6\u05D5\u05E7\u05D9\u05DD",
                    "Large \u2013 wingspan up to 2.5 m"
                ),
                // 8. Parrot (Birds)
                Seed(
                    "Parrot", "\u05EA\u05D5\u05DB\u05D9", "Birds",
                    "animal_parrot", "silence",
                    listOf(
                        "Parrots can learn to say hundreds of words!",
                        "Some parrots live over 80 years.",
                        "Parrots can use tools and solve puzzles.",
                        "A parrot's beak is strong enough to crack a walnut."
                    ),
                    listOf(
                        "\u05EA\u05D5\u05DB\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05DC\u05DE\u05D5\u05D3 \u05DC\u05D5\u05DE\u05E8 \u05DE\u05D0\u05D5\u05EA \u05DE\u05D9\u05DC\u05D9\u05DD!",
                        "\u05D9\u05E9 \u05EA\u05D5\u05DB\u05D9\u05DD \u05E9\u05D7\u05D9\u05D9\u05DD \u05DE\u05E2\u05DC 80 \u05E9\u05E0\u05D4.",
                        "\u05EA\u05D5\u05DB\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D4\u05E9\u05EA\u05DE\u05E9 \u05D1\u05DB\u05DC\u05D9\u05DD \u05D5\u05DC\u05E4\u05EA\u05D5\u05E8 \u05D7\u05D9\u05D3\u05D5\u05EA.",
                        "\u05D4\u05DE\u05E7\u05D5\u05E8 \u05E9\u05DC \u05EA\u05D5\u05DB\u05D9 \u05D7\u05D6\u05E7 \u05DE\u05E1\u05E4\u05D9\u05E7 \u05DB\u05D3\u05D9 \u05DC\u05E4\u05E6\u05D7 \u05D0\u05D2\u05D5\u05D6."
                    ),
                    "I can talk like a person and have beautiful colorful feathers. Who am I?",
                    "\u05D0\u05E0\u05D9 \u05D9\u05D5\u05D3\u05E2 \u05DC\u05D3\u05D1\u05E8 \u05DB\u05DE\u05D5 \u05D1\u05DF \u05D0\u05D3\u05DD \u05D5\u05D9\u05E9 \u05DC\u05D9 \u05E0\u05D5\u05E6\u05D5\u05EA \u05E6\u05D1\u05E2\u05D5\u05E0\u05D9\u05D5\u05EA \u05D9\u05E4\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Fruits, seeds, and nuts", "\u05E4\u05D9\u05E8\u05D5\u05EA, \u05D6\u05E8\u05E2\u05D9\u05DD \u05D5\u05D0\u05D2\u05D5\u05D6\u05D9\u05DD",
                    "Tropical rainforests", "\u05D9\u05E2\u05E8\u05D5\u05EA \u05D2\u05E9\u05DD \u05D8\u05E8\u05D5\u05E4\u05D9\u05D9\u05DD",
                    "Medium \u2013 about 30\u201340 cm tall"
                ),
                // 9. Dog (Pets)
                Seed(
                    "Dog", "\u05DB\u05DC\u05D1", "Pets",
                    "animal_dog", "silence",
                    listOf(
                        "Dogs can understand up to 250 words and gestures.",
                        "A dog's nose print is unique, just like a human fingerprint.",
                        "Dogs wag their tails to the right when they are happy.",
                        "Dogs have a sense of smell 10,000 times better than humans."
                    ),
                    listOf(
                        "\u05DB\u05DC\u05D1\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D4\u05D1\u05D9\u05DF \u05E2\u05D3 250 \u05DE\u05D9\u05DC\u05D9\u05DD \u05D5\u05DE\u05D7\u05D5\u05D5\u05EA.",
                        "\u05D8\u05D1\u05D9\u05E2\u05EA \u05D4\u05D0\u05E3 \u05E9\u05DC \u05DB\u05DC\u05D1 \u05D4\u05D9\u05D0 \u05D9\u05D9\u05D7\u05D5\u05D3\u05D9\u05EA, \u05D1\u05D3\u05D9\u05D5\u05E7 \u05DB\u05DE\u05D5 \u05D8\u05D1\u05D9\u05E2\u05EA \u05D0\u05E6\u05D1\u05E2.",
                        "\u05DB\u05DC\u05D1\u05D9\u05DD \u05DE\u05E0\u05D9\u05E4\u05D9\u05DD \u05D0\u05EA \u05D4\u05D6\u05E0\u05D1 \u05DC\u05D9\u05DE\u05D9\u05DF \u05DB\u05E9\u05D4\u05DD \u05E9\u05DE\u05D7\u05D9\u05DD.",
                        "\u05D7\u05D5\u05E9 \u05D4\u05E8\u05D9\u05D7 \u05E9\u05DC \u05DB\u05DC\u05D1\u05D9\u05DD \u05D8\u05D5\u05D1 \u05E4\u05D9 10,000 \u05DE\u05E9\u05DC \u05D1\u05E0\u05D9 \u05D0\u05D3\u05DD."
                    ),
                    "I am your best friend, I wag my tail, and I love to fetch. Who am I?",
                    "\u05D0\u05E0\u05D9 \u05D4\u05D7\u05D1\u05E8 \u05D4\u05DB\u05D9 \u05D8\u05D5\u05D1 \u05E9\u05DC\u05DA, \u05D0\u05E0\u05D9 \u05DE\u05E0\u05D9\u05E3 \u05D6\u05E0\u05D1 \u05D5\u05D0\u05D5\u05D4\u05D1 \u05DC\u05D4\u05D1\u05D9\u05D0. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Dog food, meat, and treats", "\u05D0\u05D5\u05DB\u05DC \u05DC\u05DB\u05DC\u05D1\u05D9\u05DD, \u05D1\u05E9\u05E8 \u05D5\u05D7\u05D8\u05D9\u05E4\u05D9\u05DD",
                    "Homes with people", "\u05D1\u05EA\u05D9\u05DD \u05E2\u05DD \u05D0\u05E0\u05E9\u05D9\u05DD",
                    "Medium \u2013 varies greatly by breed"
                ),
                // 10. Cat (Pets)
                Seed(
                    "Cat", "\u05D7\u05EA\u05D5\u05DC", "Pets",
                    "animal_cat", "silence",
                    listOf(
                        "Cats sleep about 16 hours a day!",
                        "Cats can rotate their ears 180 degrees.",
                        "A group of cats is called a clowder.",
                        "Cats have over 20 different sounds they use to communicate.",
                        "Cats can jump up to six times their own length."
                    ),
                    listOf(
                        "\u05D7\u05EA\u05D5\u05DC\u05D9\u05DD \u05D9\u05E9\u05E0\u05D9\u05DD \u05DB-16 \u05E9\u05E2\u05D5\u05EA \u05D1\u05D9\u05D5\u05DD!",
                        "\u05D7\u05EA\u05D5\u05DC\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E1\u05D5\u05D1\u05D1 \u05D0\u05EA \u05D4\u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD \u05E9\u05DC\u05D4\u05DD \u05D1-180 \u05DE\u05E2\u05DC\u05D5\u05EA.",
                        "\u05E7\u05D1\u05D5\u05E6\u05D4 \u05E9\u05DC \u05D7\u05EA\u05D5\u05DC\u05D9\u05DD \u05E0\u05E7\u05E8\u05D0\u05EA \u05D1\u05D0\u05E0\u05D2\u05DC\u05D9\u05EA clowder.",
                        "\u05DC\u05D7\u05EA\u05D5\u05DC\u05D9\u05DD \u05D9\u05E9 \u05D9\u05D5\u05EA\u05E8 \u05DE-20 \u05E6\u05DC\u05D9\u05DC\u05D9\u05DD \u05E9\u05D5\u05E0\u05D9\u05DD \u05DC\u05EA\u05E7\u05E9\u05D5\u05E8\u05EA.",
                        "\u05D7\u05EA\u05D5\u05DC\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E7\u05E4\u05D5\u05E5 \u05DC\u05D2\u05D5\u05D1\u05D4 \u05E9\u05DC \u05E9\u05E9 \u05E4\u05E2\u05DE\u05D9\u05DD \u05D0\u05D5\u05E8\u05DA \u05D4\u05D2\u05D5\u05E3 \u05E9\u05DC\u05D4\u05DD."
                    ),
                    "I purr when I am happy, I love to chase mice, and I always land on my feet. Who am I?",
                    "\u05D0\u05E0\u05D9 \u05DE\u05E8\u05D8\u05D8 \u05DB\u05E9\u05D0\u05E0\u05D9 \u05E9\u05DE\u05D7, \u05D0\u05D5\u05D4\u05D1 \u05DC\u05E8\u05D3\u05D5\u05E3 \u05D0\u05D7\u05E8\u05D9 \u05E2\u05DB\u05D1\u05E8\u05D9\u05DD \u05D5\u05EA\u05DE\u05D9\u05D3 \u05E0\u05D5\u05D7\u05EA \u05E2\u05DC \u05D4\u05E8\u05D2\u05DC\u05D9\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Cat food, fish, and meat", "\u05D0\u05D5\u05DB\u05DC \u05DC\u05D7\u05EA\u05D5\u05DC\u05D9\u05DD, \u05D3\u05D2\u05D9\u05DD \u05D5\u05D1\u05E9\u05E8",
                    "Homes with people, barns, and streets", "\u05D1\u05EA\u05D9\u05DD \u05E2\u05DD \u05D0\u05E0\u05E9\u05D9\u05DD, \u05D0\u05E1\u05DE\u05D9\u05DD \u05D5\u05E8\u05D7\u05D5\u05D1\u05D5\u05EA",
                    "Small to medium \u2013 about 25 cm tall"
                )
            )

            animals.forEach { a ->
                db.execSQL(
                    """INSERT INTO animals (name_en, name_he, category, image_res, sound_res,
                       facts_en, facts_he, riddle_en, riddle_he, diet_en, diet_he,
                       habitat_en, habitat_he, size_info)
                       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""",
                    arrayOf(
                        a.nameEn, a.nameHe, a.category, a.imageRes, a.soundRes,
                        toJson(a.factsEn), toJson(a.factsHe),
                        a.riddleEn, a.riddleHe, a.dietEn, a.dietHe,
                        a.habitatEn, a.habitatHe, a.sizeInfo
                    )
                )
            }
        }

        private fun seedQuizQuestions(db: SupportSQLiteDatabase) {
            data class QSeed(
                val animalId: Int, val type: String,
                val qEn: String, val qHe: String,
                val aEn: String, val aHe: String,
                val wEn: List<String>, val wHe: List<String>
            )

            val questions = listOf(
                // --- Cow (id=1) ---
                QSeed(1, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Cow", "\u05E4\u05E8\u05D4", listOf("Dog", "Lion", "Parrot"), listOf("\u05DB\u05DC\u05D1", "\u05D0\u05E8\u05D9\u05D4", "\u05EA\u05D5\u05DB\u05D9")),
                QSeed(1, "RIDDLE", "I say moo and give you milk. Who am I?", "\u05D0\u05E0\u05D9 \u05D0\u05D5\u05DE\u05E8\u05EA \u05DE\u05D5\u05D5 \u05D5\u05E0\u05D5\u05EA\u05E0\u05EA \u05D7\u05DC\u05D1. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Cow", "\u05E4\u05E8\u05D4", listOf("Chicken", "Dog", "Dolphin"), listOf("\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA", "\u05DB\u05DC\u05D1", "\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF")),
                QSeed(1, "DIET", "What does the cow eat?", "\u05DE\u05D4 \u05D0\u05D5\u05DB\u05DC\u05EA \u05D4\u05E4\u05E8\u05D4?",
                    "Grass and hay", "\u05E2\u05E9\u05D1 \u05D5\u05D7\u05E6\u05D9\u05E8",
                    listOf("Fish", "Meat", "Nuts"), listOf("\u05D3\u05D2\u05D9\u05DD", "\u05D1\u05E9\u05E8", "\u05D0\u05D2\u05D5\u05D6\u05D9\u05DD")),

                // --- Chicken (id=2) ---
                QSeed(2, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Chicken", "\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA", listOf("Eagle", "Parrot", "Dog"), listOf("\u05E0\u05E9\u05E8", "\u05EA\u05D5\u05DB\u05D9", "\u05DB\u05DC\u05D1")),
                QSeed(2, "RIDDLE", "I lay eggs and cluck all day. Who am I?", "\u05D0\u05E0\u05D9 \u05DE\u05D8\u05D9\u05DC\u05D4 \u05D1\u05D9\u05E6\u05D9\u05DD \u05D5\u05DE\u05E7\u05E8\u05E7\u05E8\u05EA \u05DB\u05DC \u05D4\u05D9\u05D5\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Chicken", "\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA", listOf("Cow", "Cat", "Dolphin"), listOf("\u05E4\u05E8\u05D4", "\u05D7\u05EA\u05D5\u05DC", "\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF")),
                QSeed(2, "HABITAT", "Where does the chicken live?", "\u05D0\u05D9\u05E4\u05D4 \u05D7\u05D9\u05D4 \u05D4\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA?",
                    "Farms and coops", "\u05D7\u05D5\u05D5\u05EA \u05D5\u05DC\u05D5\u05DC\u05D9\u05DD",
                    listOf("Ocean", "Jungle", "Mountains"), listOf("\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1", "\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC", "\u05D4\u05E8\u05D9\u05DD")),

                // --- Lion (id=3) ---
                QSeed(3, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Lion", "\u05D0\u05E8\u05D9\u05D4", listOf("Elephant", "Dog", "Eagle"), listOf("\u05E4\u05D9\u05DC", "\u05DB\u05DC\u05D1", "\u05E0\u05E9\u05E8")),
                QSeed(3, "HABITAT", "Where does the lion live?", "\u05D0\u05D9\u05E4\u05D4 \u05D7\u05D9 \u05D4\u05D0\u05E8\u05D9\u05D4?",
                    "African savanna and grasslands", "\u05E1\u05D0\u05D5\u05D5\u05E0\u05D4 \u05D5\u05E2\u05E8\u05D1\u05D5\u05EA \u05D1\u05D0\u05E4\u05E8\u05D9\u05E7\u05D4",
                    listOf("Ocean", "Farm", "Your home"), listOf("\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1", "\u05D7\u05D5\u05D5\u05D4", "\u05D4\u05D1\u05D9\u05EA \u05E9\u05DC\u05DA")),
                QSeed(3, "RIDDLE", "I am called the king of the jungle and I have a big mane. Who am I?", "\u05E7\u05D5\u05E8\u05D0\u05D9\u05DD \u05DC\u05D9 \u05DE\u05DC\u05DA \u05D4\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC \u05D5\u05D9\u05E9 \u05DC\u05D9 \u05E8\u05E2\u05DE\u05D4 \u05D2\u05D3\u05D5\u05DC\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Lion", "\u05D0\u05E8\u05D9\u05D4", listOf("Shark", "Elephant", "Cat"), listOf("\u05DB\u05E8\u05D9\u05E9", "\u05E4\u05D9\u05DC", "\u05D7\u05EA\u05D5\u05DC")),

                // --- Elephant (id=4) ---
                QSeed(4, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Elephant", "\u05E4\u05D9\u05DC", listOf("Cow", "Shark", "Cat"), listOf("\u05E4\u05E8\u05D4", "\u05DB\u05E8\u05D9\u05E9", "\u05D7\u05EA\u05D5\u05DC")),
                QSeed(4, "RIDDLE", "I have a long trunk, big ears, and I never forget. Who am I?", "\u05D9\u05E9 \u05DC\u05D9 \u05D7\u05D3\u05E7 \u05D0\u05E8\u05D5\u05DA, \u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD \u05D2\u05D3\u05D5\u05DC\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05D0\u05E3 \u05E4\u05E2\u05DD \u05DC\u05D0 \u05E9\u05D5\u05DB\u05D7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Elephant", "\u05E4\u05D9\u05DC", listOf("Lion", "Dog", "Dolphin"), listOf("\u05D0\u05E8\u05D9\u05D4", "\u05DB\u05DC\u05D1", "\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF")),
                QSeed(4, "DIET", "What does the elephant eat?", "\u05DE\u05D4 \u05D0\u05D5\u05DB\u05DC \u05D4\u05E4\u05D9\u05DC?",
                    "Plants, fruits, and tree bark", "\u05E6\u05DE\u05D7\u05D9\u05DD, \u05E4\u05D9\u05E8\u05D5\u05EA \u05D5\u05E7\u05DC\u05D9\u05E4\u05EA \u05E2\u05E6\u05D9\u05DD",
                    listOf("Meat", "Fish", "Seeds"), listOf("\u05D1\u05E9\u05E8", "\u05D3\u05D2\u05D9\u05DD", "\u05D6\u05E8\u05E2\u05D9\u05DD")),

                // --- Dolphin (id=5) ---
                QSeed(5, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Dolphin", "\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF", listOf("Shark", "Eagle", "Cow"), listOf("\u05DB\u05E8\u05D9\u05E9", "\u05E0\u05E9\u05E8", "\u05E4\u05E8\u05D4")),
                QSeed(5, "DIET", "What does the dolphin eat?", "\u05DE\u05D4 \u05D0\u05D5\u05DB\u05DC \u05D4\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF?",
                    "Fish and squid", "\u05D3\u05D2\u05D9\u05DD \u05D5\u05D3\u05D9\u05D5\u05E0\u05D5\u05E0\u05D9\u05DD",
                    listOf("Grass", "Seeds", "Meat"), listOf("\u05E2\u05E9\u05D1", "\u05D6\u05E8\u05E2\u05D9\u05DD", "\u05D1\u05E9\u05E8")),
                QSeed(5, "HABITAT", "Where does the dolphin live?", "\u05D0\u05D9\u05E4\u05D4 \u05D7\u05D9 \u05D4\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF?",
                    "Oceans and seas worldwide", "\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1\u05D9\u05DD \u05D5\u05D9\u05DE\u05D9\u05DD \u05D1\u05E8\u05D7\u05D1\u05D9 \u05D4\u05E2\u05D5\u05DC\u05DD",
                    listOf("Farm", "Jungle", "Desert"), listOf("\u05D7\u05D5\u05D5\u05D4", "\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC", "\u05DE\u05D3\u05D1\u05E8")),

                // --- Shark (id=6) ---
                QSeed(6, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Shark", "\u05DB\u05E8\u05D9\u05E9", listOf("Dolphin", "Eagle", "Dog"), listOf("\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF", "\u05E0\u05E9\u05E8", "\u05DB\u05DC\u05D1")),
                QSeed(6, "RIDDLE", "I swim in the ocean, I have lots of sharp teeth, and I am very fast. Who am I?", "\u05D0\u05E0\u05D9 \u05E9\u05D5\u05D7\u05D4 \u05D1\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1, \u05D9\u05E9 \u05DC\u05D9 \u05D4\u05E8\u05D1\u05D4 \u05E9\u05D9\u05E0\u05D9\u05D9\u05DD \u05D7\u05D3\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05DE\u05D4\u05D9\u05E8 \u05DE\u05D0\u05D5\u05D3. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Shark", "\u05DB\u05E8\u05D9\u05E9", listOf("Dolphin", "Lion", "Cow"), listOf("\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF", "\u05D0\u05E8\u05D9\u05D4", "\u05E4\u05E8\u05D4")),

                // --- Eagle (id=7) ---
                QSeed(7, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Eagle", "\u05E0\u05E9\u05E8", listOf("Parrot", "Chicken", "Cat"), listOf("\u05EA\u05D5\u05DB\u05D9", "\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA", "\u05D7\u05EA\u05D5\u05DC")),
                QSeed(7, "RIDDLE", "I fly very high in the sky and have the sharpest eyes. Who am I?", "\u05D0\u05E0\u05D9 \u05E2\u05E3 \u05D2\u05D1\u05D5\u05D4 \u05DE\u05D0\u05D5\u05D3 \u05D1\u05E9\u05DE\u05D9\u05D9\u05DD \u05D5\u05D9\u05E9 \u05DC\u05D9 \u05D0\u05EA \u05D4\u05E2\u05D9\u05E0\u05D9\u05D9\u05DD \u05D4\u05DB\u05D9 \u05D7\u05D3\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Eagle", "\u05E0\u05E9\u05E8", listOf("Parrot", "Chicken", "Dolphin"), listOf("\u05EA\u05D5\u05DB\u05D9", "\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA", "\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF")),
                QSeed(7, "HABITAT", "Where does the eagle live?", "\u05D0\u05D9\u05E4\u05D4 \u05D7\u05D9 \u05D4\u05E0\u05E9\u05E8?",
                    "Mountains, forests, and cliffs", "\u05D4\u05E8\u05D9\u05DD, \u05D9\u05E2\u05E8\u05D5\u05EA \u05D5\u05E6\u05D5\u05E7\u05D9\u05DD",
                    listOf("Ocean", "Farm", "Your home"), listOf("\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1", "\u05D7\u05D5\u05D5\u05D4", "\u05D4\u05D1\u05D9\u05EA \u05E9\u05DC\u05DA")),

                // --- Parrot (id=8) ---
                QSeed(8, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Parrot", "\u05EA\u05D5\u05DB\u05D9", listOf("Eagle", "Chicken", "Cat"), listOf("\u05E0\u05E9\u05E8", "\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA", "\u05D7\u05EA\u05D5\u05DC")),
                QSeed(8, "RIDDLE", "I can talk like a person and have colorful feathers. Who am I?", "\u05D0\u05E0\u05D9 \u05D9\u05D5\u05D3\u05E2 \u05DC\u05D3\u05D1\u05E8 \u05DB\u05DE\u05D5 \u05D1\u05DF \u05D0\u05D3\u05DD \u05D5\u05D9\u05E9 \u05DC\u05D9 \u05E0\u05D5\u05E6\u05D5\u05EA \u05E6\u05D1\u05E2\u05D5\u05E0\u05D9\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Parrot", "\u05EA\u05D5\u05DB\u05D9", listOf("Dog", "Eagle", "Cow"), listOf("\u05DB\u05DC\u05D1", "\u05E0\u05E9\u05E8", "\u05E4\u05E8\u05D4")),
                QSeed(8, "DIET", "What does the parrot eat?", "\u05DE\u05D4 \u05D0\u05D5\u05DB\u05DC \u05D4\u05EA\u05D5\u05DB\u05D9?",
                    "Fruits, seeds, and nuts", "\u05E4\u05D9\u05E8\u05D5\u05EA, \u05D6\u05E8\u05E2\u05D9\u05DD \u05D5\u05D0\u05D2\u05D5\u05D6\u05D9\u05DD",
                    listOf("Meat", "Fish and squid", "Grass"), listOf("\u05D1\u05E9\u05E8", "\u05D3\u05D2\u05D9\u05DD \u05D5\u05D3\u05D9\u05D5\u05E0\u05D5\u05E0\u05D9\u05DD", "\u05E2\u05E9\u05D1")),

                // --- Dog (id=9) ---
                QSeed(9, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Dog", "\u05DB\u05DC\u05D1", listOf("Cat", "Lion", "Cow"), listOf("\u05D7\u05EA\u05D5\u05DC", "\u05D0\u05E8\u05D9\u05D4", "\u05E4\u05E8\u05D4")),
                QSeed(9, "RIDDLE", "I am your best friend, I wag my tail, and I love to fetch. Who am I?", "\u05D0\u05E0\u05D9 \u05D4\u05D7\u05D1\u05E8 \u05D4\u05DB\u05D9 \u05D8\u05D5\u05D1 \u05E9\u05DC\u05DA, \u05D0\u05E0\u05D9 \u05DE\u05E0\u05D9\u05E3 \u05D6\u05E0\u05D1 \u05D5\u05D0\u05D5\u05D4\u05D1 \u05DC\u05D4\u05D1\u05D9\u05D0. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Dog", "\u05DB\u05DC\u05D1", listOf("Cat", "Parrot", "Elephant"), listOf("\u05D7\u05EA\u05D5\u05DC", "\u05EA\u05D5\u05DB\u05D9", "\u05E4\u05D9\u05DC")),
                QSeed(9, "HABITAT", "Where does the dog live?", "\u05D0\u05D9\u05E4\u05D4 \u05D7\u05D9 \u05D4\u05DB\u05DC\u05D1?",
                    "Homes with people", "\u05D1\u05EA\u05D9\u05DD \u05E2\u05DD \u05D0\u05E0\u05E9\u05D9\u05DD",
                    listOf("Ocean", "Jungle", "Mountains"), listOf("\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1", "\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC", "\u05D4\u05E8\u05D9\u05DD")),

                // --- Cat (id=10) ---
                QSeed(10, "PICTURE", "Which animal is this?", "\u05D0\u05D9\u05D6\u05D5 \u05D7\u05D9\u05D4 \u05D6\u05D5?",
                    "Cat", "\u05D7\u05EA\u05D5\u05DC", listOf("Dog", "Lion", "Parrot"), listOf("\u05DB\u05DC\u05D1", "\u05D0\u05E8\u05D9\u05D4", "\u05EA\u05D5\u05DB\u05D9")),
                QSeed(10, "RIDDLE", "I purr when I am happy, I love to chase mice, and I always land on my feet. Who am I?", "\u05D0\u05E0\u05D9 \u05DE\u05E8\u05D8\u05D8 \u05DB\u05E9\u05D0\u05E0\u05D9 \u05E9\u05DE\u05D7, \u05D0\u05D5\u05D4\u05D1 \u05DC\u05E8\u05D3\u05D5\u05E3 \u05D0\u05D7\u05E8\u05D9 \u05E2\u05DB\u05D1\u05E8\u05D9\u05DD \u05D5\u05EA\u05DE\u05D9\u05D3 \u05E0\u05D5\u05D7\u05EA \u05E2\u05DC \u05D4\u05E8\u05D2\u05DC\u05D9\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
                    "Cat", "\u05D7\u05EA\u05D5\u05DC", listOf("Dog", "Cow", "Shark"), listOf("\u05DB\u05DC\u05D1", "\u05E4\u05E8\u05D4", "\u05DB\u05E8\u05D9\u05E9")),
                QSeed(10, "DIET", "What does the cat eat?", "\u05DE\u05D4 \u05D0\u05D5\u05DB\u05DC \u05D4\u05D7\u05EA\u05D5\u05DC?",
                    "Cat food, fish, and meat", "\u05D0\u05D5\u05DB\u05DC \u05DC\u05D7\u05EA\u05D5\u05DC\u05D9\u05DD, \u05D3\u05D2\u05D9\u05DD \u05D5\u05D1\u05E9\u05E8",
                    listOf("Grass", "Seeds and nuts", "Plankton"), listOf("\u05E2\u05E9\u05D1", "\u05D6\u05E8\u05E2\u05D9\u05DD \u05D5\u05D0\u05D2\u05D5\u05D6\u05D9\u05DD", "\u05E4\u05DC\u05E0\u05E7\u05D8\u05D5\u05DF"))
            )

            questions.forEach { q ->
                db.execSQL(
                    """INSERT INTO quiz_questions (animal_id, question_type, question_text_en,
                       question_text_he, correct_answer_en, correct_answer_he,
                       wrong_answers_en, wrong_answers_he)
                       VALUES (?, ?, ?, ?, ?, ?, ?, ?)""",
                    arrayOf(
                        q.animalId, q.type, q.qEn, q.qHe,
                        q.aEn, q.aHe, toJson(q.wEn), toJson(q.wHe)
                    )
                )
            }
        }
    }
}
