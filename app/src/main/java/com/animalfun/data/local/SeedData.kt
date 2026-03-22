package com.animalfun.data.local

import org.json.JSONArray

/**
 * Additional seed data for Farm and Jungle categories.
 * IDs start at 11 (existing animals occupy 1–10).
 */
object SeedData {

    private fun toJson(items: List<String>): String {
        val arr = JSONArray()
        items.forEach { arr.put(it) }
        return arr.toString().replace("'", "''")
    }

    private fun esc(s: String): String = s.replace("'", "''")

    private fun animalInsert(
        nameEn: String, nameHe: String, category: String,
        imageRes: String, soundRes: String,
        factsEn: List<String>, factsHe: List<String>,
        riddleEn: String, riddleHe: String,
        dietEn: String, dietHe: String,
        habitatEn: String, habitatHe: String,
        sizeInfo: String
    ): String =
        """INSERT INTO animals (name_en, name_he, category, image_res, sound_res, facts_en, facts_he, riddle_en, riddle_he, diet_en, diet_he, habitat_en, habitat_he, size_info) VALUES ('${esc(nameEn)}', '${esc(nameHe)}', '${esc(category)}', '${esc(imageRes)}', '${esc(soundRes)}', '${toJson(factsEn)}', '${toJson(factsHe)}', '${esc(riddleEn)}', '${esc(riddleHe)}', '${esc(dietEn)}', '${esc(dietHe)}', '${esc(habitatEn)}', '${esc(habitatHe)}', '${esc(sizeInfo)}')"""

    private fun quizInsert(
        animalId: Int, questionType: String,
        qEn: String, qHe: String,
        aEn: String, aHe: String,
        wEn: List<String>, wHe: List<String>
    ): String =
        """INSERT INTO quiz_questions (animal_id, question_type, question_text_en, question_text_he, correct_answer_en, correct_answer_he, wrong_answers_en, wrong_answers_he) VALUES ($animalId, '${esc(questionType)}', '${esc(qEn)}', '${esc(qHe)}', '${esc(aEn)}', '${esc(aHe)}', '${toJson(wEn)}', '${toJson(wHe)}')"""

    // ----------------------------------------------------------------
    // Farm animals (IDs 11–16)
    // ----------------------------------------------------------------
    fun farmAnimalSql(): List<String> = listOf(
        // 11. Horse
        animalInsert(
            "Horse", "\u05E1\u05D5\u05E1", "Farm",
            "animal_horse", "silence",
            listOf(
                "Horses can sleep standing up.",
                "A baby horse can walk just hours after being born.",
                "Horses have the largest eyes of any land mammal."
            ),
            listOf(
                "\u05E1\u05D5\u05E1\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D9\u05E9\u05D5\u05DF \u05D1\u05E2\u05DE\u05D9\u05D3\u05D4.",
                "\u05E1\u05D9\u05D9\u05D7 \u05D9\u05DB\u05D5\u05DC \u05DC\u05DC\u05DB\u05EA \u05E9\u05E2\u05D5\u05EA \u05E1\u05E4\u05D5\u05E8\u05D5\u05EA \u05D0\u05D7\u05E8\u05D9 \u05D4\u05DC\u05D9\u05D3\u05D4.",
                "\u05DC\u05E1\u05D5\u05E1\u05D9\u05DD \u05D9\u05E9 \u05D0\u05EA \u05D4\u05E2\u05D9\u05E0\u05D9\u05D9\u05DD \u05D4\u05D2\u05D3\u05D5\u05DC\u05D5\u05EA \u05D1\u05D9\u05D5\u05EA\u05E8 \u05DE\u05D1\u05D9\u05DF \u05D9\u05D5\u05E0\u05E7\u05D9 \u05D4\u05D9\u05D1\u05E9\u05D4."
            ),
            "I gallop fast and people ride on my back. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D3\u05D5\u05D4\u05E8 \u05DE\u05D4\u05E8 \u05D5\u05D0\u05E0\u05E9\u05D9\u05DD \u05E8\u05D5\u05DB\u05D1\u05D9\u05DD \u05E2\u05DC \u05D4\u05D2\u05D1. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Grass and hay", "\u05E2\u05E9\u05D1 \u05D5\u05D7\u05E6\u05D9\u05E8",
            "Farms and open grasslands", "\u05D7\u05D5\u05D5\u05EA \u05D5\u05DE\u05E8\u05E2\u05D9\u05DD \u05E4\u05EA\u05D5\u05D7\u05D9\u05DD",
            "Large \u2013 about 1.6 m tall at the shoulder"
        ),
        // 12. Pig
        animalInsert(
            "Pig", "\u05D7\u05D6\u05D9\u05E8", "Farm",
            "animal_pig", "silence",
            listOf(
                "Pigs are one of the smartest farm animals.",
                "Pigs roll in mud to cool down because they cannot sweat.",
                "A pig can run a kilometer in about seven minutes."
            ),
            listOf(
                "\u05D7\u05D6\u05D9\u05E8\u05D9\u05DD \u05D4\u05DD \u05DE\u05D4\u05D7\u05D9\u05D5\u05EA \u05D4\u05DE\u05E9\u05E7 \u05D4\u05D7\u05DB\u05DE\u05D5\u05EA \u05D1\u05D9\u05D5\u05EA\u05E8.",
                "\u05D7\u05D6\u05D9\u05E8\u05D9\u05DD \u05DE\u05EA\u05D2\u05DC\u05D2\u05DC\u05D9\u05DD \u05D1\u05D1\u05D5\u05E5 \u05DB\u05D9 \u05D4\u05DD \u05DC\u05D0 \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D4\u05D6\u05D9\u05E2.",
                "\u05D7\u05D6\u05D9\u05E8 \u05D9\u05DB\u05D5\u05DC \u05DC\u05E8\u05D5\u05E5 \u05E7\u05D9\u05DC\u05D5\u05DE\u05D8\u05E8 \u05D1\u05DB\u05E9\u05D1\u05E2 \u05D3\u05E7\u05D5\u05EA."
            ),
            "I roll in the mud and say oink. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05EA\u05D2\u05DC\u05D2\u05DC \u05D1\u05D1\u05D5\u05E5 \u05D5\u05D0\u05D5\u05DE\u05E8 \u05D7\u05E8\u05D5\u05E7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Grains, vegetables, and scraps", "\u05D3\u05D2\u05E0\u05D9\u05DD, \u05D9\u05E8\u05E7\u05D5\u05EA \u05D5\u05E9\u05D0\u05E8\u05D9\u05D5\u05EA",
            "Farms and pens", "\u05D7\u05D5\u05D5\u05EA \u05D5\u05D3\u05D9\u05E8\u05D5\u05EA",
            "Medium \u2013 about 0.9 m tall"
        ),
        // 13. Sheep
        animalInsert(
            "Sheep", "\u05DB\u05D1\u05E9\u05D4", "Farm",
            "animal_sheep", "silence",
            listOf(
                "Sheep can recognize up to 50 other sheep faces.",
                "A sheep's wool never stops growing.",
                "Sheep like to stay close together in a flock for safety."
            ),
            listOf(
                "\u05DB\u05D1\u05E9\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D5\u05EA \u05DC\u05D6\u05D4\u05D5\u05EA \u05E2\u05D3 50 \u05E4\u05E0\u05D9\u05DD \u05E9\u05DC \u05DB\u05D1\u05E9\u05D9\u05DD \u05D0\u05D7\u05E8\u05D5\u05EA.",
                "\u05D4\u05E6\u05DE\u05E8 \u05E9\u05DC \u05DB\u05D1\u05E9\u05D4 \u05DC\u05E2\u05D5\u05DC\u05DD \u05DC\u05D0 \u05DE\u05E4\u05E1\u05D9\u05E7 \u05DC\u05D2\u05D3\u05D5\u05DC.",
                "\u05DB\u05D1\u05E9\u05D9\u05DD \u05D0\u05D5\u05D4\u05D1\u05D5\u05EA \u05DC\u05D4\u05D9\u05E9\u05D0\u05E8 \u05E6\u05DE\u05D5\u05D3\u05D5\u05EA \u05D1\u05E2\u05D3\u05E8 \u05DC\u05D1\u05D8\u05D9\u05D7\u05D5\u05EA."
            ),
            "I have fluffy wool and say baa. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E6\u05DE\u05E8 \u05E8\u05DA \u05D5\u05D0\u05E0\u05D9 \u05D0\u05D5\u05DE\u05E8\u05EA \u05DE\u05E2\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Grass and clover", "\u05E2\u05E9\u05D1 \u05D5\u05EA\u05DC\u05EA\u05DF",
            "Farms and hillsides", "\u05D7\u05D5\u05D5\u05EA \u05D5\u05DE\u05D3\u05E8\u05D5\u05E0\u05D5\u05EA \u05D2\u05D1\u05E2\u05D5\u05EA",
            "Medium \u2013 about 0.8 m tall"
        ),
        // 14. Goat
        animalInsert(
            "Goat", "\u05E2\u05D6", "Farm",
            "animal_goat", "silence",
            listOf(
                "Goats have rectangular pupils that give them wide vision.",
                "Goats are great climbers and can scale steep rocks.",
                "Baby goats can stand and walk within minutes of birth."
            ),
            listOf(
                "\u05DC\u05E2\u05D9\u05D6\u05D9\u05DD \u05D9\u05E9 \u05D0\u05D9\u05E9\u05D5\u05E0\u05D9\u05DD \u05DE\u05DC\u05D1\u05E0\u05D9\u05D9\u05DD \u05E9\u05E0\u05D5\u05EA\u05E0\u05D9\u05DD \u05E8\u05D0\u05D9\u05D9\u05D4 \u05E8\u05D7\u05D1\u05D4.",
                "\u05E2\u05D9\u05D6\u05D9\u05DD \u05DE\u05D8\u05E4\u05E1\u05D9\u05DD \u05DE\u05E2\u05D5\u05DC\u05D9\u05DD \u05D5\u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D8\u05E4\u05E1 \u05E2\u05DC \u05E1\u05DC\u05E2\u05D9\u05DD \u05EA\u05DC\u05D5\u05DC\u05D9\u05DD.",
                "\u05D2\u05D3\u05D9 \u05E2\u05D9\u05D6\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E2\u05DE\u05D5\u05D3 \u05D5\u05DC\u05DC\u05DB\u05EA \u05D3\u05E7\u05D5\u05EA \u05D0\u05D7\u05E8\u05D9 \u05D4\u05DC\u05D9\u05D3\u05D4."
            ),
            "I climb rocks and eat almost anything. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05D8\u05E4\u05E1 \u05E2\u05DC \u05E1\u05DC\u05E2\u05D9\u05DD \u05D5\u05D0\u05D5\u05DB\u05DC \u05DB\u05DE\u05E2\u05D8 \u05D4\u05DB\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Grass, leaves, and shrubs", "\u05E2\u05E9\u05D1, \u05E2\u05DC\u05D9\u05DD \u05D5\u05E9\u05D9\u05D7\u05D9\u05DD",
            "Farms and rocky hillsides", "\u05D7\u05D5\u05D5\u05EA \u05D5\u05DE\u05D3\u05E8\u05D5\u05E0\u05D5\u05EA \u05E1\u05DC\u05E2\u05D9\u05D9\u05DD",
            "Medium \u2013 about 0.7 m tall"
        ),
        // 15. Duck
        animalInsert(
            "Duck", "\u05D1\u05E8\u05D5\u05D5\u05D6", "Farm",
            "animal_duck", "silence",
            listOf(
                "Ducks have waterproof feathers so they stay dry.",
                "Ducklings follow the first moving thing they see after hatching.",
                "Ducks can turn their heads almost all the way around."
            ),
            listOf(
                "\u05DC\u05D1\u05E8\u05D5\u05D5\u05D6\u05D9\u05DD \u05D9\u05E9 \u05E0\u05D5\u05E6\u05D5\u05EA \u05E2\u05DE\u05D9\u05D3\u05D5\u05EA \u05D1\u05DE\u05D9\u05DD \u05D5\u05D4\u05DD \u05E0\u05E9\u05D0\u05E8\u05D9\u05DD \u05D9\u05D1\u05E9\u05D9\u05DD.",
                "\u05D0\u05E4\u05E8\u05D5\u05D7\u05D9 \u05D1\u05E8\u05D5\u05D5\u05D6 \u05D4\u05D5\u05DC\u05DB\u05D9\u05DD \u05D0\u05D7\u05E8\u05D9 \u05D4\u05D3\u05D1\u05E8 \u05D4\u05E8\u05D0\u05E9\u05D5\u05DF \u05E9\u05D4\u05DD \u05E8\u05D5\u05D0\u05D9\u05DD \u05D0\u05D7\u05E8\u05D9 \u05D4\u05D1\u05E7\u05D9\u05E2\u05D4.",
                "\u05D1\u05E8\u05D5\u05D5\u05D6\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E1\u05D5\u05D1\u05D1 \u05D0\u05EA \u05D4\u05E8\u05D0\u05E9 \u05DB\u05DE\u05E2\u05D8 \u05E1\u05D9\u05D1\u05D5\u05D1 \u05E9\u05DC\u05DD."
            ),
            "I waddle and quack and love to swim. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05EA\u05E0\u05D3\u05E0\u05D3 \u05D5\u05E2\u05D5\u05E9\u05D4 \u05E7\u05D5\u05D5\u05E7 \u05D5\u05D0\u05D5\u05D4\u05D1 \u05DC\u05E9\u05D7\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Seeds, insects, and water plants", "\u05D6\u05E8\u05E2\u05D9\u05DD, \u05D7\u05E8\u05E7\u05D9\u05DD \u05D5\u05E6\u05DE\u05D7\u05D9 \u05DE\u05D9\u05DD",
            "Farms and ponds", "\u05D7\u05D5\u05D5\u05EA \u05D5\u05D1\u05E8\u05D9\u05DB\u05D5\u05EA",
            "Small \u2013 about 0.5 m tall"
        ),
        // 16. Donkey
        animalInsert(
            "Donkey", "\u05D7\u05DE\u05D5\u05E8", "Farm",
            "animal_donkey", "silence",
            listOf(
                "Donkeys have an excellent memory and can remember places they visited years ago.",
                "A donkey's big ears help it hear sounds from very far away.",
                "Donkeys are very strong and can carry heavy loads."
            ),
            listOf(
                "\u05DC\u05D7\u05DE\u05D5\u05E8\u05D9\u05DD \u05D9\u05E9 \u05D6\u05D9\u05DB\u05E8\u05D5\u05DF \u05DE\u05E6\u05D5\u05D9\u05DF \u05D5\u05D4\u05DD \u05D6\u05D5\u05DB\u05E8\u05D9\u05DD \u05DE\u05E7\u05D5\u05DE\u05D5\u05EA \u05E9\u05D1\u05D9\u05E7\u05E8\u05D5 \u05D1\u05D4\u05DD \u05DC\u05E4\u05E0\u05D9 \u05E9\u05E0\u05D9\u05DD.",
                "\u05D4\u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD \u05D4\u05D2\u05D3\u05D5\u05DC\u05D5\u05EA \u05E9\u05DC \u05D4\u05D7\u05DE\u05D5\u05E8 \u05E2\u05D5\u05D6\u05E8\u05D5\u05EA \u05DC\u05D5 \u05DC\u05E9\u05DE\u05D5\u05E2 \u05E6\u05DC\u05D9\u05DC\u05D9\u05DD \u05DE\u05E8\u05D7\u05D5\u05E7.",
                "\u05D7\u05DE\u05D5\u05E8\u05D9\u05DD \u05D7\u05D6\u05E7\u05D9\u05DD \u05DE\u05D0\u05D5\u05D3 \u05D5\u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E9\u05D0\u05EA \u05DE\u05E9\u05D0\u05D5\u05EA \u05DB\u05D1\u05D3\u05D9\u05DD."
            ),
            "I have long ears and carry heavy things on farms. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD \u05D0\u05E8\u05D5\u05DB\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05E0\u05D5\u05E9\u05D0 \u05D3\u05D1\u05E8\u05D9\u05DD \u05DB\u05D1\u05D3\u05D9\u05DD \u05D1\u05D7\u05D5\u05D5\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Grass, hay, and grains", "\u05E2\u05E9\u05D1, \u05D7\u05E6\u05D9\u05E8 \u05D5\u05D3\u05D2\u05E0\u05D9\u05DD",
            "Farms and dry fields", "\u05D7\u05D5\u05D5\u05EA \u05D5\u05E9\u05D3\u05D5\u05EA \u05D9\u05D1\u05E9\u05D9\u05DD",
            "Medium \u2013 about 1.2 m tall"
        )
    )

    // ----------------------------------------------------------------
    // Jungle animals (IDs 17–24)
    // ----------------------------------------------------------------
    fun jungleAnimalSql(): List<String> = listOf(
        // 17. Tiger
        animalInsert(
            "Tiger", "\u05E0\u05DE\u05E8", "Jungle",
            "animal_tiger", "silence",
            listOf(
                "Every tiger has a unique pattern of stripes, like a fingerprint.",
                "Tigers are the largest wild cats in the world.",
                "Tigers love water and are excellent swimmers."
            ),
            listOf(
                "\u05DC\u05DB\u05DC \u05E0\u05DE\u05E8 \u05D9\u05E9 \u05D3\u05D5\u05D2\u05DE\u05EA \u05E4\u05E1\u05D9\u05DD \u05D9\u05D9\u05D7\u05D5\u05D3\u05D9\u05EA, \u05DB\u05DE\u05D5 \u05D8\u05D1\u05D9\u05E2\u05EA \u05D0\u05E6\u05D1\u05E2.",
                "\u05E0\u05DE\u05E8\u05D9\u05DD \u05D4\u05DD \u05D7\u05EA\u05D5\u05DC\u05D9 \u05D4\u05D1\u05E8 \u05D4\u05D2\u05D3\u05D5\u05DC\u05D9\u05DD \u05D1\u05D9\u05D5\u05EA\u05E8 \u05D1\u05E2\u05D5\u05DC\u05DD.",
                "\u05E0\u05DE\u05E8\u05D9\u05DD \u05D0\u05D5\u05D4\u05D1\u05D9\u05DD \u05DE\u05D9\u05DD \u05D5\u05D4\u05DD \u05E9\u05D7\u05D9\u05D9\u05E0\u05D9\u05DD \u05DE\u05E6\u05D5\u05D9\u05E0\u05D9\u05DD."
            ),
            "I have orange fur with black stripes and I am the biggest cat. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E4\u05E8\u05D5\u05D5\u05D4 \u05DB\u05EA\u05D5\u05DE\u05D4 \u05E2\u05DD \u05E4\u05E1\u05D9\u05DD \u05E9\u05D7\u05D5\u05E8\u05D9\u05DD \u05D5\u05D0\u05E0\u05D9 \u05D4\u05D7\u05EA\u05D5\u05DC \u05D4\u05DB\u05D9 \u05D2\u05D3\u05D5\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Meat (deer and wild boar)", "\u05D1\u05E9\u05E8 (\u05D0\u05D9\u05D9\u05DC\u05D9\u05DD \u05D5\u05D7\u05D6\u05D9\u05E8\u05D9 \u05D1\u05E8)",
            "Asian jungles and forests", "\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC\u05D9\u05DD \u05D5\u05D9\u05E2\u05E8\u05D5\u05EA \u05D1\u05D0\u05E1\u05D9\u05D4",
            "Large \u2013 about 1 m tall at the shoulder"
        ),
        // 18. Monkey
        animalInsert(
            "Monkey", "\u05E7\u05D5\u05E3", "Jungle",
            "animal_monkey", "silence",
            listOf(
                "Monkeys use different sounds to warn about different dangers.",
                "Some monkeys use tools like sticks to get food.",
                "Monkeys groom each other to make friends."
            ),
            listOf(
                "\u05E7\u05D5\u05E4\u05D9\u05DD \u05DE\u05E9\u05EA\u05DE\u05E9\u05D9\u05DD \u05D1\u05E6\u05DC\u05D9\u05DC\u05D9\u05DD \u05E9\u05D5\u05E0\u05D9\u05DD \u05DC\u05D4\u05D6\u05D4\u05D9\u05E8 \u05DE\u05E1\u05DB\u05E0\u05D5\u05EA \u05E9\u05D5\u05E0\u05D5\u05EA.",
                "\u05D9\u05E9 \u05E7\u05D5\u05E4\u05D9\u05DD \u05E9\u05DE\u05E9\u05EA\u05DE\u05E9\u05D9\u05DD \u05D1\u05DB\u05DC\u05D9\u05DD \u05DB\u05DE\u05D5 \u05DE\u05E7\u05DC\u05D5\u05EA \u05DB\u05D3\u05D9 \u05DC\u05D4\u05E9\u05D9\u05D2 \u05D0\u05D5\u05DB\u05DC.",
                "\u05E7\u05D5\u05E4\u05D9\u05DD \u05DE\u05E0\u05E7\u05D9\u05DD \u05D0\u05D7\u05D3 \u05D0\u05EA \u05D4\u05E9\u05E0\u05D9 \u05DB\u05D3\u05D9 \u05DC\u05D4\u05EA\u05D7\u05D1\u05E8."
            ),
            "I swing from trees and love bananas. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05EA\u05E0\u05D3\u05E0\u05D3 \u05E2\u05DC \u05E2\u05E6\u05D9\u05DD \u05D5\u05D0\u05D5\u05D4\u05D1 \u05D1\u05E0\u05E0\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Fruits, seeds, and insects", "\u05E4\u05D9\u05E8\u05D5\u05EA, \u05D6\u05E8\u05E2\u05D9\u05DD \u05D5\u05D7\u05E8\u05E7\u05D9\u05DD",
            "Tropical jungles and forests", "\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC\u05D9\u05DD \u05D5\u05D9\u05E2\u05E8\u05D5\u05EA \u05D8\u05E8\u05D5\u05E4\u05D9\u05D9\u05DD",
            "Small to medium \u2013 varies by species"
        ),
        // 19. Giraffe
        animalInsert(
            "Giraffe", "\u05D2\u05F3\u05D9\u05E8\u05E4\u05D4", "Jungle",
            "animal_giraffe", "silence",
            listOf(
                "Giraffes are the tallest animals on Earth.",
                "A giraffe's tongue is about 50 cm long and is dark purple.",
                "Giraffes only need about 30 minutes of sleep a day."
            ),
            listOf(
                "\u05D2\u05F3\u05D9\u05E8\u05E4\u05D5\u05EA \u05D4\u05DF \u05D4\u05D7\u05D9\u05D5\u05EA \u05D4\u05D2\u05D1\u05D5\u05D4\u05D5\u05EA \u05D1\u05D9\u05D5\u05EA\u05E8 \u05D1\u05E2\u05D5\u05DC\u05DD.",
                "\u05D4\u05DC\u05E9\u05D5\u05DF \u05E9\u05DC \u05D2\u05F3\u05D9\u05E8\u05E4\u05D4 \u05D0\u05D5\u05E8\u05DB\u05D4 \u05DB-50 \u05E1\u05DE \u05D5\u05D4\u05D9\u05D0 \u05E1\u05D2\u05D5\u05DC\u05D4 \u05DB\u05D4\u05D4.",
                "\u05D2\u05F3\u05D9\u05E8\u05E4\u05D5\u05EA \u05E6\u05E8\u05D9\u05DB\u05D5\u05EA \u05E8\u05E7 \u05DB-30 \u05D3\u05E7\u05D5\u05EA \u05E9\u05D9\u05E0\u05D4 \u05D1\u05D9\u05D5\u05DD."
            ),
            "I have a very long neck and I am the tallest animal. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E6\u05D5\u05D5\u05D0\u05E8 \u05D0\u05E8\u05D5\u05DA \u05DE\u05D0\u05D5\u05D3 \u05D5\u05D0\u05E0\u05D9 \u05D4\u05D7\u05D9\u05D4 \u05D4\u05D4\u05DB\u05D9 \u05D2\u05D1\u05D5\u05D4\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Leaves from tall trees", "\u05E2\u05DC\u05D9\u05DD \u05DE\u05E2\u05E6\u05D9\u05DD \u05D2\u05D1\u05D5\u05D4\u05D9\u05DD",
            "African savannas", "\u05E1\u05D0\u05D5\u05D5\u05E0\u05D5\u05EA \u05D1\u05D0\u05E4\u05E8\u05D9\u05E7\u05D4",
            "Very tall \u2013 about 5.5 m tall"
        ),
        // 20. Zebra
        animalInsert(
            "Zebra", "\u05D6\u05D1\u05E8\u05D4", "Jungle",
            "animal_zebra", "silence",
            listOf(
                "Every zebra has a unique stripe pattern, like a fingerprint.",
                "Zebras sleep standing up so they can run from danger quickly.",
                "A group of zebras is called a dazzle."
            ),
            listOf(
                "\u05DC\u05DB\u05DC \u05D6\u05D1\u05E8\u05D4 \u05D9\u05E9 \u05D3\u05D5\u05D2\u05DE\u05EA \u05E4\u05E1\u05D9\u05DD \u05D9\u05D9\u05D7\u05D5\u05D3\u05D9\u05EA, \u05DB\u05DE\u05D5 \u05D8\u05D1\u05D9\u05E2\u05EA \u05D0\u05E6\u05D1\u05E2.",
                "\u05D6\u05D1\u05E8\u05D5\u05EA \u05D9\u05E9\u05E0\u05D5\u05EA \u05D1\u05E2\u05DE\u05D9\u05D3\u05D4 \u05DB\u05D3\u05D9 \u05DC\u05D1\u05E8\u05D5\u05D7 \u05DE\u05E1\u05DB\u05E0\u05D4 \u05DE\u05D4\u05E8.",
                "\u05DC\u05E7\u05D1\u05D5\u05E6\u05D4 \u05E9\u05DC \u05D6\u05D1\u05E8\u05D5\u05EA \u05E7\u05D5\u05E8\u05D0\u05D9\u05DD \u05D1\u05D0\u05E0\u05D2\u05DC\u05D9\u05EA dazzle."
            ),
            "I look like a horse with black and white stripes. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E0\u05E8\u05D0\u05D4 \u05DB\u05DE\u05D5 \u05E1\u05D5\u05E1 \u05E2\u05DD \u05E4\u05E1\u05D9\u05DD \u05E9\u05D7\u05D5\u05E8\u05D9\u05DD \u05D5\u05DC\u05D1\u05E0\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Grass", "\u05E2\u05E9\u05D1",
            "African savannas and grasslands", "\u05E1\u05D0\u05D5\u05D5\u05E0\u05D5\u05EA \u05D5\u05E2\u05E8\u05D1\u05D5\u05EA \u05D1\u05D0\u05E4\u05E8\u05D9\u05E7\u05D4",
            "Medium \u2013 about 1.4 m tall at the shoulder"
        ),
        // 21. Hippo
        animalInsert(
            "Hippo", "\u05D4\u05D9\u05E4\u05D5\u05E4\u05D5\u05D8\u05DD", "Jungle",
            "animal_hippo", "silence",
            listOf(
                "Hippos spend most of the day in water to keep cool.",
                "A hippo can hold its breath underwater for up to five minutes.",
                "Hippos are one of the heaviest land animals."
            ),
            listOf(
                "\u05D4\u05D9\u05E4\u05D5\u05E4\u05D5\u05D8\u05DE\u05D9\u05DD \u05DE\u05D1\u05DC\u05D9\u05DD \u05D0\u05EA \u05E8\u05D5\u05D1 \u05D4\u05D9\u05D5\u05DD \u05D1\u05DE\u05D9\u05DD \u05DB\u05D3\u05D9 \u05DC\u05D4\u05EA\u05E7\u05E8\u05E8.",
                "\u05D4\u05D9\u05E4\u05D5\u05E4\u05D5\u05D8\u05DD \u05D9\u05DB\u05D5\u05DC \u05DC\u05E2\u05E6\u05D5\u05E8 \u05D0\u05EA \u05D4\u05E0\u05E9\u05D9\u05DE\u05D4 \u05DE\u05EA\u05D7\u05EA \u05DC\u05DE\u05D9\u05DD \u05E2\u05D3 \u05D7\u05DE\u05E9 \u05D3\u05E7\u05D5\u05EA.",
                "\u05D4\u05D9\u05E4\u05D5\u05E4\u05D5\u05D8\u05DE\u05D9\u05DD \u05D4\u05DD \u05DE\u05D4\u05D7\u05D9\u05D5\u05EA \u05D4\u05D9\u05D1\u05E9\u05EA\u05D9\u05D5\u05EA \u05D4\u05DB\u05D1\u05D3\u05D5\u05EA \u05D1\u05D9\u05D5\u05EA\u05E8."
            ),
            "I am huge, love rivers, and open my mouth very wide. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E2\u05E0\u05E7, \u05D0\u05D5\u05D4\u05D1 \u05E0\u05D4\u05E8\u05D5\u05EA \u05D5\u05E4\u05D5\u05EA\u05D7 \u05D0\u05EA \u05D4\u05E4\u05D4 \u05E8\u05D7\u05D1 \u05DE\u05D0\u05D5\u05D3. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Grass", "\u05E2\u05E9\u05D1",
            "African rivers and lakes", "\u05E0\u05D4\u05E8\u05D5\u05EA \u05D5\u05D0\u05D2\u05DE\u05D9\u05DD \u05D1\u05D0\u05E4\u05E8\u05D9\u05E7\u05D4",
            "Very large \u2013 about 1.5 m tall"
        ),
        // 22. Gorilla
        animalInsert(
            "Gorilla", "\u05D2\u05D5\u05E8\u05D9\u05DC\u05D4", "Jungle",
            "animal_gorilla", "silence",
            listOf(
                "Gorillas share about 98% of their DNA with humans.",
                "Gorillas live in family groups led by a silverback male.",
                "Gorillas build a new sleeping nest every night."
            ),
            listOf(
                "\u05D2\u05D5\u05E8\u05D9\u05DC\u05D5\u05EA \u05D7\u05D5\u05DC\u05E7\u05D5\u05EA \u05DB-98% \u05DE\u05D4-DNA \u05E9\u05DC\u05D4\u05DF \u05E2\u05DD \u05D1\u05E0\u05D9 \u05D0\u05D3\u05DD.",
                "\u05D2\u05D5\u05E8\u05D9\u05DC\u05D5\u05EA \u05D7\u05D9\u05D5\u05EA \u05D1\u05E7\u05D1\u05D5\u05E6\u05D5\u05EA \u05DE\u05E9\u05E4\u05D7\u05EA\u05D9\u05D5\u05EA \u05D1\u05D4\u05E0\u05D4\u05D2\u05EA \u05D6\u05DB\u05E8 \u05D2\u05D1-\u05DB\u05E1\u05D5\u05E3.",
                "\u05D2\u05D5\u05E8\u05D9\u05DC\u05D5\u05EA \u05D1\u05D5\u05E0\u05D5\u05EA \u05E7\u05DF \u05E9\u05D9\u05E0\u05D4 \u05D7\u05D3\u05E9 \u05DB\u05DC \u05DC\u05D9\u05DC\u05D4."
            ),
            "I am a big strong ape that lives in the jungle. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D5\u05E3 \u05D2\u05D3\u05D5\u05DC \u05D5\u05D7\u05D6\u05E7 \u05E9\u05D7\u05D9 \u05D1\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Leaves, stems, and fruit", "\u05E2\u05DC\u05D9\u05DD, \u05D2\u05D1\u05E2\u05D5\u05DC\u05D9\u05DD \u05D5\u05E4\u05D9\u05E8\u05D5\u05EA",
            "African tropical forests", "\u05D9\u05E2\u05E8\u05D5\u05EA \u05D8\u05E8\u05D5\u05E4\u05D9\u05D9\u05DD \u05D1\u05D0\u05E4\u05E8\u05D9\u05E7\u05D4",
            "Large \u2013 about 1.7 m tall"
        ),
        // 23. Crocodile
        animalInsert(
            "Crocodile", "\u05EA\u05E0\u05D9\u05DF", "Jungle",
            "animal_crocodile", "silence",
            listOf(
                "Crocodiles have been around since the time of the dinosaurs.",
                "A crocodile can go through 4,000 teeth in its lifetime.",
                "Crocodiles cannot stick out their tongues."
            ),
            listOf(
                "\u05EA\u05E0\u05D9\u05E0\u05D9\u05DD \u05D7\u05D9\u05D9\u05DD \u05E2\u05DC \u05D4\u05D0\u05E8\u05E5 \u05DE\u05D0\u05D6 \u05EA\u05E7\u05D5\u05E4\u05EA \u05D4\u05D3\u05D9\u05E0\u05D5\u05D6\u05D0\u05D5\u05E8\u05D9\u05DD.",
                "\u05EA\u05E0\u05D9\u05DF \u05D9\u05DB\u05D5\u05DC \u05DC\u05D4\u05D7\u05DC\u05D9\u05E3 \u05E2\u05D3 4,000 \u05E9\u05D9\u05E0\u05D9\u05D9\u05DD \u05D1\u05DE\u05D4\u05DC\u05DA \u05D7\u05D9\u05D9\u05D5.",
                "\u05EA\u05E0\u05D9\u05E0\u05D9\u05DD \u05DC\u05D0 \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D4\u05D5\u05E6\u05D9\u05D0 \u05D0\u05EA \u05D4\u05DC\u05E9\u05D5\u05DF."
            ),
            "I have sharp teeth, a long tail, and I live in rivers. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E9\u05D9\u05E0\u05D9\u05D9\u05DD \u05D7\u05D3\u05D5\u05EA, \u05D6\u05E0\u05D1 \u05D0\u05E8\u05D5\u05DA \u05D5\u05D0\u05E0\u05D9 \u05D7\u05D9 \u05D1\u05E0\u05D4\u05E8\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Fish, birds, and mammals", "\u05D3\u05D2\u05D9\u05DD, \u05E6\u05D9\u05E4\u05D5\u05E8\u05D9\u05DD \u05D5\u05D9\u05D5\u05E0\u05E7\u05D9\u05DD",
            "Rivers and swamps in tropical regions", "\u05E0\u05D4\u05E8\u05D5\u05EA \u05D5\u05D1\u05D9\u05E6\u05D5\u05EA \u05D1\u05D0\u05D6\u05D5\u05E8\u05D9\u05DD \u05D8\u05E8\u05D5\u05E4\u05D9\u05D9\u05DD",
            "Large \u2013 about 4\u20135 m long"
        ),
        // 24. Snake
        animalInsert(
            "Snake", "\u05E0\u05D7\u05E9", "Jungle",
            "animal_snake", "silence",
            listOf(
                "Snakes smell with their tongues by flicking them in the air.",
                "Snakes have no legs, eyelids, or ear holes.",
                "Some snakes can go months without eating a single meal."
            ),
            listOf(
                "\u05E0\u05D7\u05E9\u05D9\u05DD \u05DE\u05E8\u05D9\u05D7\u05D9\u05DD \u05D1\u05E2\u05D6\u05E8\u05EA \u05D4\u05DC\u05E9\u05D5\u05DF \u05E9\u05DC\u05D4\u05DD.",
                "\u05DC\u05E0\u05D7\u05E9\u05D9\u05DD \u05D0\u05D9\u05DF \u05E8\u05D2\u05DC\u05D9\u05D9\u05DD, \u05E2\u05E4\u05E2\u05E4\u05D9\u05D9\u05DD \u05D0\u05D5 \u05E4\u05EA\u05D7\u05D9 \u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD.",
                "\u05D9\u05E9 \u05E0\u05D7\u05E9\u05D9\u05DD \u05E9\u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D4\u05D9\u05E9\u05D0\u05E8 \u05D7\u05D5\u05D3\u05E9\u05D9\u05DD \u05D1\u05DC\u05D9 \u05D0\u05E8\u05D5\u05D7\u05D4 \u05D0\u05D7\u05EA."
            ),
            "I slither on the ground and have no legs at all. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D6\u05D5\u05D7\u05DC \u05E2\u05DC \u05D4\u05D0\u05D3\u05DE\u05D4 \u05D5\u05D0\u05D9\u05DF \u05DC\u05D9 \u05E8\u05D2\u05DC\u05D9\u05D9\u05DD \u05D1\u05DB\u05DC\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Rodents, eggs, and insects", "\u05DE\u05DB\u05E8\u05E1\u05DE\u05D9\u05DD, \u05D1\u05D9\u05E6\u05D9\u05DD \u05D5\u05D7\u05E8\u05E7\u05D9\u05DD",
            "Jungles, deserts, and forests worldwide", "\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC\u05D9\u05DD, \u05DE\u05D3\u05D1\u05E8\u05D9\u05D5\u05EA \u05D5\u05D9\u05E2\u05E8\u05D5\u05EA \u05D1\u05E8\u05D7\u05D1\u05D9 \u05D4\u05E2\u05D5\u05DC\u05DD",
            "Varies \u2013 from 10 cm to over 5 m long"
        )
    )

    // ----------------------------------------------------------------
    // Quiz questions – 1 per animal (IDs 11–24)
    // ----------------------------------------------------------------
    fun farmJungleQuizSql(): List<String> = listOf(
        // 11. Horse
        quizInsert(11, "RIDDLE",
            "I gallop fast and people ride on my back. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D3\u05D5\u05D4\u05E8 \u05DE\u05D4\u05E8 \u05D5\u05D0\u05E0\u05E9\u05D9\u05DD \u05E8\u05D5\u05DB\u05D1\u05D9\u05DD \u05E2\u05DC \u05D4\u05D2\u05D1. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Horse", "\u05E1\u05D5\u05E1",
            listOf("Donkey", "Cow", "Goat"), listOf("\u05D7\u05DE\u05D5\u05E8", "\u05E4\u05E8\u05D4", "\u05E2\u05D6")),
        // 12. Pig
        quizInsert(12, "RIDDLE",
            "I roll in the mud and say oink. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05EA\u05D2\u05DC\u05D2\u05DC \u05D1\u05D1\u05D5\u05E5 \u05D5\u05D0\u05D5\u05DE\u05E8 \u05D7\u05E8\u05D5\u05E7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Pig", "\u05D7\u05D6\u05D9\u05E8",
            listOf("Sheep", "Duck", "Horse"), listOf("\u05DB\u05D1\u05E9\u05D4", "\u05D1\u05E8\u05D5\u05D5\u05D6", "\u05E1\u05D5\u05E1")),
        // 13. Sheep
        quizInsert(13, "RIDDLE",
            "I have fluffy wool and say baa. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E6\u05DE\u05E8 \u05E8\u05DA \u05D5\u05D0\u05E0\u05D9 \u05D0\u05D5\u05DE\u05E8\u05EA \u05DE\u05E2\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Sheep", "\u05DB\u05D1\u05E9\u05D4",
            listOf("Goat", "Pig", "Donkey"), listOf("\u05E2\u05D6", "\u05D7\u05D6\u05D9\u05E8", "\u05D7\u05DE\u05D5\u05E8")),
        // 14. Goat
        quizInsert(14, "RIDDLE",
            "I climb rocks and eat almost anything. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05D8\u05E4\u05E1 \u05E2\u05DC \u05E1\u05DC\u05E2\u05D9\u05DD \u05D5\u05D0\u05D5\u05DB\u05DC \u05DB\u05DE\u05E2\u05D8 \u05D4\u05DB\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Goat", "\u05E2\u05D6",
            listOf("Sheep", "Horse", "Duck"), listOf("\u05DB\u05D1\u05E9\u05D4", "\u05E1\u05D5\u05E1", "\u05D1\u05E8\u05D5\u05D5\u05D6")),
        // 15. Duck
        quizInsert(15, "RIDDLE",
            "I waddle and quack and love to swim. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05EA\u05E0\u05D3\u05E0\u05D3 \u05D5\u05E2\u05D5\u05E9\u05D4 \u05E7\u05D5\u05D5\u05E7 \u05D5\u05D0\u05D5\u05D4\u05D1 \u05DC\u05E9\u05D7\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Duck", "\u05D1\u05E8\u05D5\u05D5\u05D6",
            listOf("Chicken", "Goat", "Pig"), listOf("\u05EA\u05E8\u05E0\u05D2\u05D5\u05DC\u05EA", "\u05E2\u05D6", "\u05D7\u05D6\u05D9\u05E8")),
        // 16. Donkey
        quizInsert(16, "RIDDLE",
            "I have long ears and carry heavy things on farms. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD \u05D0\u05E8\u05D5\u05DB\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05E0\u05D5\u05E9\u05D0 \u05D3\u05D1\u05E8\u05D9\u05DD \u05DB\u05D1\u05D3\u05D9\u05DD \u05D1\u05D7\u05D5\u05D5\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Donkey", "\u05D7\u05DE\u05D5\u05E8",
            listOf("Horse", "Cow", "Sheep"), listOf("\u05E1\u05D5\u05E1", "\u05E4\u05E8\u05D4", "\u05DB\u05D1\u05E9\u05D4")),
        // 17. Tiger
        quizInsert(17, "RIDDLE",
            "I have orange fur with black stripes and I am the biggest cat. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E4\u05E8\u05D5\u05D5\u05D4 \u05DB\u05EA\u05D5\u05DE\u05D4 \u05E2\u05DD \u05E4\u05E1\u05D9\u05DD \u05E9\u05D7\u05D5\u05E8\u05D9\u05DD \u05D5\u05D0\u05E0\u05D9 \u05D4\u05D7\u05EA\u05D5\u05DC \u05D4\u05DB\u05D9 \u05D2\u05D3\u05D5\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Tiger", "\u05E0\u05DE\u05E8",
            listOf("Lion", "Zebra", "Monkey"), listOf("\u05D0\u05E8\u05D9\u05D4", "\u05D6\u05D1\u05E8\u05D4", "\u05E7\u05D5\u05E3")),
        // 18. Monkey
        quizInsert(18, "RIDDLE",
            "I swing from trees and love bananas. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05EA\u05E0\u05D3\u05E0\u05D3 \u05E2\u05DC \u05E2\u05E6\u05D9\u05DD \u05D5\u05D0\u05D5\u05D4\u05D1 \u05D1\u05E0\u05E0\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Monkey", "\u05E7\u05D5\u05E3",
            listOf("Gorilla", "Snake", "Giraffe"), listOf("\u05D2\u05D5\u05E8\u05D9\u05DC\u05D4", "\u05E0\u05D7\u05E9", "\u05D2\u05F3\u05D9\u05E8\u05E4\u05D4")),
        // 19. Giraffe
        quizInsert(19, "RIDDLE",
            "I have a very long neck and I am the tallest animal. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E6\u05D5\u05D5\u05D0\u05E8 \u05D0\u05E8\u05D5\u05DA \u05DE\u05D0\u05D5\u05D3 \u05D5\u05D0\u05E0\u05D9 \u05D4\u05D7\u05D9\u05D4 \u05D4\u05D4\u05DB\u05D9 \u05D2\u05D1\u05D5\u05D4\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Giraffe", "\u05D2\u05F3\u05D9\u05E8\u05E4\u05D4",
            listOf("Elephant", "Hippo", "Zebra"), listOf("\u05E4\u05D9\u05DC", "\u05D4\u05D9\u05E4\u05D5\u05E4\u05D5\u05D8\u05DD", "\u05D6\u05D1\u05E8\u05D4")),
        // 20. Zebra
        quizInsert(20, "RIDDLE",
            "I look like a horse with black and white stripes. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E0\u05E8\u05D0\u05D4 \u05DB\u05DE\u05D5 \u05E1\u05D5\u05E1 \u05E2\u05DD \u05E4\u05E1\u05D9\u05DD \u05E9\u05D7\u05D5\u05E8\u05D9\u05DD \u05D5\u05DC\u05D1\u05E0\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Zebra", "\u05D6\u05D1\u05E8\u05D4",
            listOf("Horse", "Tiger", "Giraffe"), listOf("\u05E1\u05D5\u05E1", "\u05E0\u05DE\u05E8", "\u05D2\u05F3\u05D9\u05E8\u05E4\u05D4")),
        // 21. Hippo
        quizInsert(21, "RIDDLE",
            "I am huge, love rivers, and open my mouth very wide. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E2\u05E0\u05E7, \u05D0\u05D5\u05D4\u05D1 \u05E0\u05D4\u05E8\u05D5\u05EA \u05D5\u05E4\u05D5\u05EA\u05D7 \u05D0\u05EA \u05D4\u05E4\u05D4 \u05E8\u05D7\u05D1 \u05DE\u05D0\u05D5\u05D3. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Hippo", "\u05D4\u05D9\u05E4\u05D5\u05E4\u05D5\u05D8\u05DD",
            listOf("Crocodile", "Elephant", "Gorilla"), listOf("\u05EA\u05E0\u05D9\u05DF", "\u05E4\u05D9\u05DC", "\u05D2\u05D5\u05E8\u05D9\u05DC\u05D4")),
        // 22. Gorilla
        quizInsert(22, "RIDDLE",
            "I am a big strong ape that lives in the jungle. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D5\u05E3 \u05D2\u05D3\u05D5\u05DC \u05D5\u05D7\u05D6\u05E7 \u05E9\u05D7\u05D9 \u05D1\u05D2\u05F3\u05D5\u05E0\u05D2\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Gorilla", "\u05D2\u05D5\u05E8\u05D9\u05DC\u05D4",
            listOf("Monkey", "Tiger", "Hippo"), listOf("\u05E7\u05D5\u05E3", "\u05E0\u05DE\u05E8", "\u05D4\u05D9\u05E4\u05D5\u05E4\u05D5\u05D8\u05DD")),
        // 23. Crocodile
        quizInsert(23, "RIDDLE",
            "I have sharp teeth, a long tail, and I live in rivers. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E9\u05D9\u05E0\u05D9\u05D9\u05DD \u05D7\u05D3\u05D5\u05EA, \u05D6\u05E0\u05D1 \u05D0\u05E8\u05D5\u05DA \u05D5\u05D0\u05E0\u05D9 \u05D7\u05D9 \u05D1\u05E0\u05D4\u05E8\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Crocodile", "\u05EA\u05E0\u05D9\u05DF",
            listOf("Snake", "Hippo", "Shark"), listOf("\u05E0\u05D7\u05E9", "\u05D4\u05D9\u05E4\u05D5\u05E4\u05D5\u05D8\u05DD", "\u05DB\u05E8\u05D9\u05E9")),
        // 24. Snake
        quizInsert(24, "RIDDLE",
            "I slither on the ground and have no legs at all. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D6\u05D5\u05D7\u05DC \u05E2\u05DC \u05D4\u05D0\u05D3\u05DE\u05D4 \u05D5\u05D0\u05D9\u05DF \u05DC\u05D9 \u05E8\u05D2\u05DC\u05D9\u05D9\u05DD \u05D1\u05DB\u05DC\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Snake", "\u05E0\u05D7\u05E9",
            listOf("Crocodile", "Monkey", "Tiger"), listOf("\u05EA\u05E0\u05D9\u05DF", "\u05E7\u05D5\u05E3", "\u05E0\u05DE\u05E8"))
    )

    // ----------------------------------------------------------------
    // Ocean animals (IDs 25–30)
    // ----------------------------------------------------------------
    fun oceanAnimalSql(): List<String> = listOf(
        // 25. Whale
        animalInsert(
            "Whale", "\u05DC\u05D5\u05D5\u05D9\u05D9\u05EA\u05DF", "Ocean",
            "animal_whale", "silence",
            listOf(
                "Blue whales are the largest animals ever to live on Earth.",
                "Whales breathe air through a blowhole on top of their head.",
                "A whale's heart can be as big as a small car."
            ),
            listOf(
                "\u05DC\u05D5\u05D5\u05D9\u05D9\u05EA\u05E0\u05D9\u05DD \u05DB\u05D7\u05D5\u05DC\u05D9\u05DD \u05D4\u05DD \u05D4\u05D7\u05D9\u05D5\u05EA \u05D4\u05D2\u05D3\u05D5\u05DC\u05D5\u05EA \u05D1\u05D9\u05D5\u05EA\u05E8 \u05E9\u05D7\u05D9\u05D5 \u05D0\u05D9 \u05E4\u05E2\u05DD \u05E2\u05DC \u05E4\u05E0\u05D9 \u05D4\u05D0\u05E8\u05E5.",
                "\u05DC\u05D5\u05D5\u05D9\u05D9\u05EA\u05E0\u05D9\u05DD \u05E0\u05D5\u05E9\u05DE\u05D9\u05DD \u05D0\u05D5\u05D5\u05D9\u05E8 \u05D3\u05E8\u05DA \u05E4\u05EA\u05D7 \u05E0\u05E9\u05D9\u05E4\u05D4 \u05D1\u05E8\u05D0\u05E9.",
                "\u05D4\u05DC\u05D1 \u05E9\u05DC \u05DC\u05D5\u05D5\u05D9\u05D9\u05EA\u05DF \u05D9\u05DB\u05D5\u05DC \u05DC\u05D4\u05D9\u05D5\u05EA \u05D2\u05D3\u05D5\u05DC \u05DB\u05DE\u05D5 \u05DE\u05DB\u05D5\u05E0\u05D9\u05EA \u05E7\u05D8\u05E0\u05D4."
            ),
            "I am the biggest animal in the sea and spray water from my head. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D4\u05D7\u05D9\u05D4 \u05D4\u05DB\u05D9 \u05D2\u05D3\u05D5\u05DC\u05D4 \u05D1\u05D9\u05DD \u05D5\u05DE\u05EA\u05D9\u05D6 \u05DE\u05D9\u05DD \u05DE\u05D4\u05E8\u05D0\u05E9. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Krill and small fish", "\u05E7\u05E8\u05D9\u05DC \u05D5\u05D3\u05D2\u05D9\u05DD \u05E7\u05D8\u05E0\u05D9\u05DD",
            "All oceans worldwide", "\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1\u05D9\u05DD \u05D1\u05E8\u05D7\u05D1\u05D9 \u05D4\u05E2\u05D5\u05DC\u05DD",
            "Very large \u2013 up to 30 m long"
        ),
        // 26. Octopus
        animalInsert(
            "Octopus", "\u05EA\u05DE\u05E0\u05D5\u05DF", "Ocean",
            "animal_octopus", "silence",
            listOf(
                "An octopus has three hearts and blue blood.",
                "Octopuses can change color to hide from enemies.",
                "An octopus has eight flexible arms."
            ),
            listOf(
                "\u05DC\u05EA\u05DE\u05E0\u05D5\u05DF \u05D9\u05E9 \u05E9\u05DC\u05D5\u05E9\u05D4 \u05DC\u05D1\u05D1\u05D5\u05EA \u05D5\u05D3\u05DD \u05DB\u05D7\u05D5\u05DC.",
                "\u05EA\u05DE\u05E0\u05D5\u05E0\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E9\u05E0\u05D5\u05EA \u05E6\u05D1\u05E2 \u05DB\u05D3\u05D9 \u05DC\u05D4\u05EA\u05D7\u05D1\u05D0.",
                "\u05DC\u05EA\u05DE\u05E0\u05D5\u05DF \u05D9\u05E9 \u05E9\u05DE\u05D5\u05E0\u05D4 \u05D6\u05E8\u05D5\u05E2\u05D5\u05EA \u05D2\u05DE\u05D9\u05E9\u05D5\u05EA."
            ),
            "I have eight arms and can change my color. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E9\u05DE\u05D5\u05E0\u05D4 \u05D6\u05E8\u05D5\u05E2\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05DE\u05E9\u05E0\u05D4 \u05E6\u05D1\u05E2. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Crabs, fish, and shellfish", "\u05E1\u05E8\u05D8\u05E0\u05D9\u05DD, \u05D3\u05D2\u05D9\u05DD \u05D5\u05E8\u05DB\u05D9\u05DB\u05D5\u05EA",
            "Ocean floors and coral reefs", "\u05E7\u05E8\u05E7\u05E2\u05D9\u05EA \u05D4\u05D9\u05DD \u05D5\u05E9\u05D5\u05E0\u05D9\u05D5\u05EA \u05D0\u05DC\u05DE\u05D5\u05D2\u05D9\u05DD",
            "Small to medium \u2013 up to 5 m arm span"
        ),
        // 27. Seahorse
        animalInsert(
            "Seahorse", "\u05E1\u05D5\u05E1\u05D5\u05DF \u05D9\u05DD", "Ocean",
            "animal_seahorse", "silence",
            listOf(
                "The male seahorse carries the babies in a pouch.",
                "Seahorses have no stomach so they eat almost constantly.",
                "Seahorses swim upright, unlike most fish."
            ),
            listOf(
                "\u05D0\u05E6\u05DC \u05E1\u05D5\u05E1\u05D5\u05DF \u05D4\u05D9\u05DD \u05D4\u05D6\u05DB\u05E8 \u05E0\u05D5\u05E9\u05D0 \u05D0\u05EA \u05D4\u05D2\u05D5\u05E8\u05D9\u05DD \u05D1\u05DB\u05D9\u05E1.",
                "\u05DC\u05E1\u05D5\u05E1\u05D5\u05E0\u05D9 \u05D9\u05DD \u05D0\u05D9\u05DF \u05E7\u05D9\u05D1\u05D4 \u05D5\u05DC\u05DB\u05DF \u05D4\u05DD \u05D0\u05D5\u05DB\u05DC\u05D9\u05DD \u05DB\u05DC \u05D4\u05D6\u05DE\u05DF.",
                "\u05E1\u05D5\u05E1\u05D5\u05E0\u05D9 \u05D9\u05DD \u05E9\u05D5\u05D7\u05D9\u05DD \u05D1\u05DE\u05E6\u05D1 \u05D6\u05E7\u05D5\u05E3, \u05D1\u05E9\u05D5\u05E0\u05D4 \u05DE\u05E8\u05D5\u05D1 \u05D4\u05D3\u05D2\u05D9\u05DD."
            ),
            "I look like a tiny horse that lives in the sea. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E0\u05E8\u05D0\u05D4 \u05DB\u05DE\u05D5 \u05E1\u05D5\u05E1 \u05D6\u05E2\u05D9\u05E8 \u05E9\u05D7\u05D9 \u05D1\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Tiny shrimp and plankton", "\u05E9\u05E8\u05D9\u05DE\u05E4\u05E1 \u05D6\u05E2\u05D9\u05E8\u05D9\u05DD \u05D5\u05E4\u05DC\u05E0\u05E7\u05D8\u05D5\u05DF",
            "Shallow warm waters and coral reefs", "\u05DE\u05D9\u05DD \u05E8\u05D3\u05D5\u05D3\u05D9\u05DD \u05D5\u05E9\u05D5\u05E0\u05D9\u05D5\u05EA \u05D0\u05DC\u05DE\u05D5\u05D2\u05D9\u05DD",
            "Tiny \u2013 about 2\u201335 cm tall"
        ),
        // 28. Turtle
        animalInsert(
            "Turtle", "\u05E6\u05D1 \u05D9\u05DD", "Ocean",
            "animal_turtle", "silence",
            listOf(
                "Sea turtles can live for over 100 years.",
                "Turtles return to the same beach where they were born to lay eggs.",
                "A sea turtle's shell is part of its skeleton."
            ),
            listOf(
                "\u05E6\u05D1\u05D9 \u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D7\u05D9\u05D5\u05EA \u05DE\u05E2\u05DC 100 \u05E9\u05E0\u05D4.",
                "\u05E6\u05D1\u05D9\u05DD \u05D7\u05D5\u05D6\u05E8\u05D9\u05DD \u05DC\u05D0\u05D5\u05EA\u05D5 \u05D7\u05D5\u05E3 \u05E9\u05D1\u05D5 \u05E0\u05D5\u05DC\u05D3\u05D5 \u05DB\u05D3\u05D9 \u05DC\u05D4\u05D8\u05D9\u05DC \u05D1\u05D9\u05E6\u05D9\u05DD.",
                "\u05D4\u05E9\u05E8\u05D9\u05D5\u05DF \u05E9\u05DC \u05E6\u05D1 \u05D9\u05DD \u05D4\u05D5\u05D0 \u05D7\u05DC\u05E7 \u05DE\u05D4\u05E9\u05DC\u05D3 \u05E9\u05DC\u05D5."
            ),
            "I carry my home on my back and swim in the ocean. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E0\u05D5\u05E9\u05D0 \u05D0\u05EA \u05D4\u05D1\u05D9\u05EA \u05E2\u05DC \u05D4\u05D2\u05D1 \u05D5\u05E9\u05D5\u05D7\u05D4 \u05D1\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Jellyfish and seagrass", "\u05DE\u05D3\u05D5\u05D6\u05D5\u05EA \u05D5\u05E2\u05E9\u05D1 \u05D9\u05DD",
            "Warm and tropical oceans", "\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1\u05D9\u05DD \u05D7\u05DE\u05D9\u05DD \u05D5\u05D8\u05E8\u05D5\u05E4\u05D9\u05D9\u05DD",
            "Large \u2013 up to 1.5 m long"
        ),
        // 29. Jellyfish
        animalInsert(
            "Jellyfish", "\u05DE\u05D3\u05D5\u05D6\u05D4", "Ocean",
            "animal_jellyfish", "silence",
            listOf(
                "Jellyfish have no brain, heart, or bones.",
                "Some jellyfish glow in the dark.",
                "Jellyfish are made of about 95% water."
            ),
            listOf(
                "\u05DC\u05DE\u05D3\u05D5\u05D6\u05D5\u05EA \u05D0\u05D9\u05DF \u05DE\u05D5\u05D7, \u05DC\u05D1 \u05D0\u05D5 \u05E2\u05E6\u05DE\u05D5\u05EA.",
                "\u05D9\u05E9 \u05DE\u05D3\u05D5\u05D6\u05D5\u05EA \u05E9\u05D6\u05D5\u05D4\u05E8\u05D5\u05EA \u05D1\u05D7\u05D5\u05E9\u05DA.",
                "\u05DE\u05D3\u05D5\u05D6\u05D5\u05EA \u05E2\u05E9\u05D5\u05D9\u05D5\u05EA \u05DE\u05DB-95% \u05DE\u05D9\u05DD."
            ),
            "I float in the sea and can sting, but I have no brain. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E6\u05E4\u05D4 \u05D1\u05D9\u05DD \u05D5\u05D9\u05DB\u05D5\u05DC\u05D4 \u05DC\u05E2\u05E7\u05D5\u05E5, \u05D0\u05D1\u05DC \u05D0\u05D9\u05DF \u05DC\u05D9 \u05DE\u05D5\u05D7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Plankton and small fish", "\u05E4\u05DC\u05E0\u05E7\u05D8\u05D5\u05DF \u05D5\u05D3\u05D2\u05D9\u05DD \u05E7\u05D8\u05E0\u05D9\u05DD",
            "All oceans worldwide", "\u05D0\u05D5\u05E7\u05D9\u05D9\u05E0\u05D5\u05E1\u05D9\u05DD \u05D1\u05E8\u05D7\u05D1\u05D9 \u05D4\u05E2\u05D5\u05DC\u05DD",
            "Varies \u2013 from 1 cm to 2 m wide"
        ),
        // 30. Clownfish
        animalInsert(
            "Clownfish", "\u05D3\u05D2 \u05DC\u05D9\u05E6\u05DF", "Ocean",
            "animal_clownfish", "silence",
            listOf(
                "Clownfish live safely among stinging sea anemones.",
                "All clownfish are born male; some later become female.",
                "A clownfish covers itself in anemone mucus for protection."
            ),
            listOf(
                "\u05D3\u05D2\u05D9 \u05DC\u05D9\u05E6\u05DF \u05D7\u05D9\u05D9\u05DD \u05D1\u05D1\u05D8\u05D7\u05D4 \u05D1\u05D9\u05DF \u05E9\u05D5\u05E9\u05E0\u05EA \u05D9\u05DD \u05E2\u05D5\u05E7\u05E6\u05E0\u05D9\u05EA.",
                "\u05DB\u05DC \u05D3\u05D2\u05D9 \u05D4\u05DC\u05D9\u05E6\u05DF \u05E0\u05D5\u05DC\u05D3\u05D9\u05DD \u05D6\u05DB\u05E8\u05D9\u05DD; \u05D7\u05DC\u05E7\u05DD \u05D4\u05D5\u05E4\u05DB\u05D9\u05DD \u05DC\u05E0\u05E7\u05D1\u05D5\u05EA.",
                "\u05D3\u05D2 \u05DC\u05D9\u05E6\u05DF \u05DE\u05DB\u05E1\u05D4 \u05E2\u05E6\u05DE\u05D5 \u05D1\u05E8\u05D9\u05E8 \u05E9\u05DC \u05E9\u05D5\u05E9\u05E0\u05D4 \u05DC\u05D4\u05D2\u05E0\u05D4."
            ),
            "I am a small orange fish that hides in anemones. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D3\u05D2 \u05E7\u05D8\u05DF \u05D5\u05DB\u05EA\u05D5\u05DD \u05E9\u05DE\u05EA\u05D7\u05D1\u05D0 \u05D1\u05E9\u05D5\u05E9\u05E0\u05EA \u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Algae and tiny plankton", "\u05D0\u05E6\u05D5\u05EA \u05D5\u05E4\u05DC\u05E0\u05E7\u05D8\u05D5\u05DF \u05D6\u05E2\u05D9\u05E8",
            "Coral reefs in warm waters", "\u05E9\u05D5\u05E0\u05D9\u05D5\u05EA \u05D0\u05DC\u05DE\u05D5\u05D2\u05D9\u05DD \u05D1\u05DE\u05D9\u05DD \u05D7\u05DE\u05D9\u05DD",
            "Small \u2013 about 8\u201311 cm long"
        )
    )

    // ----------------------------------------------------------------
    // Birds (IDs 31–36)
    // ----------------------------------------------------------------
    fun birdAnimalSql(): List<String> = listOf(
        // 31. Owl
        animalInsert(
            "Owl", "\u05D9\u05E0\u05E9\u05D5\u05E3", "Birds",
            "animal_owl", "silence",
            listOf(
                "Owls can rotate their heads up to 270 degrees.",
                "Owls fly almost silently thanks to special feathers.",
                "Most owls are active at night and sleep during the day."
            ),
            listOf(
                "\u05D9\u05E0\u05E9\u05D5\u05E4\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E1\u05D5\u05D1\u05D1 \u05D0\u05EA \u05D4\u05E8\u05D0\u05E9 \u05E2\u05D3 270 \u05DE\u05E2\u05DC\u05D5\u05EA.",
                "\u05D9\u05E0\u05E9\u05D5\u05E4\u05D9\u05DD \u05E2\u05E4\u05D9\u05DD \u05DB\u05DE\u05E2\u05D8 \u05D1\u05E9\u05E7\u05D8 \u05D1\u05D6\u05DB\u05D5\u05EA \u05E0\u05D5\u05E6\u05D5\u05EA \u05DE\u05D9\u05D5\u05D7\u05D3\u05D5\u05EA.",
                "\u05E8\u05D5\u05D1 \u05D4\u05D9\u05E0\u05E9\u05D5\u05E4\u05D9\u05DD \u05E4\u05E2\u05D9\u05DC\u05D9\u05DD \u05D1\u05DC\u05D9\u05DC\u05D4 \u05D5\u05D9\u05E9\u05E0\u05D9\u05DD \u05D1\u05D9\u05D5\u05DD."
            ),
            "I can turn my head almost all the way around and I hunt at night. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05E1\u05D5\u05D1\u05D1 \u05D0\u05EA \u05D4\u05E8\u05D0\u05E9 \u05DB\u05DE\u05E2\u05D8 \u05E1\u05D9\u05D1\u05D5\u05D1 \u05E9\u05DC\u05DD \u05D5\u05E6\u05D3 \u05D1\u05DC\u05D9\u05DC\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Mice, insects, and small birds", "\u05E2\u05DB\u05D1\u05E8\u05D9\u05DD, \u05D7\u05E8\u05E7\u05D9\u05DD \u05D5\u05E6\u05D9\u05E4\u05D5\u05E8\u05D9\u05DD \u05E7\u05D8\u05E0\u05D9\u05DD",
            "Forests, deserts, and cities worldwide", "\u05D9\u05E2\u05E8\u05D5\u05EA, \u05DE\u05D3\u05D1\u05E8\u05D9\u05D5\u05EA \u05D5\u05E2\u05E8\u05D9\u05DD \u05D1\u05E8\u05D7\u05D1\u05D9 \u05D4\u05E2\u05D5\u05DC\u05DD",
            "Small to medium \u2013 30\u201370 cm tall"
        ),
        // 32. Penguin
        animalInsert(
            "Penguin", "\u05E4\u05D9\u05E0\u05D2\u05D5\u05D5\u05D9\u05DF", "Birds",
            "animal_penguin", "silence",
            listOf(
                "Penguins cannot fly but are excellent swimmers.",
                "Emperor penguins can dive deeper than 500 meters.",
                "Penguin parents take turns keeping their egg warm."
            ),
            listOf(
                "\u05E4\u05D9\u05E0\u05D2\u05D5\u05D5\u05D9\u05E0\u05D9\u05DD \u05DC\u05D0 \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E2\u05D5\u05E3 \u05D0\u05D1\u05DC \u05E9\u05D5\u05D7\u05D9\u05DD \u05DE\u05E6\u05D5\u05D9\u05DF.",
                "\u05E4\u05D9\u05E0\u05D2\u05D5\u05D5\u05D9\u05E0\u05D9 \u05E7\u05D9\u05E1\u05E8 \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E6\u05DC\u05D5\u05DC \u05DC\u05E2\u05D5\u05DE\u05E7 \u05E9\u05DC \u05DE\u05E2\u05DC 500 \u05DE\u05D8\u05E8.",
                "\u05D4\u05D5\u05E8\u05D9 \u05D4\u05E4\u05D9\u05E0\u05D2\u05D5\u05D5\u05D9\u05DF \u05DE\u05EA\u05D7\u05DC\u05E4\u05D9\u05DD \u05D1\u05E9\u05DE\u05D9\u05E8\u05D4 \u05E2\u05DC \u05D4\u05D1\u05D9\u05E6\u05D4."
            ),
            "I am a bird that cannot fly but I love to swim in icy water. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E6\u05D9\u05E4\u05D5\u05E8 \u05E9\u05DC\u05D0 \u05E2\u05E3 \u05D0\u05D1\u05DC \u05D0\u05D5\u05D4\u05D1 \u05DC\u05E9\u05D7\u05D5\u05EA \u05D1\u05DE\u05D9\u05DD \u05E7\u05E8\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Fish, krill, and squid", "\u05D3\u05D2\u05D9\u05DD, \u05E7\u05E8\u05D9\u05DC \u05D5\u05D3\u05D9\u05D5\u05E0\u05D5\u05E0\u05D9\u05DD",
            "Antarctica and cold southern coasts", "\u05D0\u05E0\u05D8\u05E8\u05E7\u05D8\u05D9\u05E7\u05D4 \u05D5\u05D7\u05D5\u05E4\u05D9\u05DD \u05E7\u05E8\u05D9\u05DD \u05D1\u05D3\u05E8\u05D5\u05DD",
            "Medium \u2013 40\u2013115 cm tall"
        ),
        // 33. Flamingo
        animalInsert(
            "Flamingo", "\u05E4\u05DC\u05DE\u05D9\u05E0\u05D2\u05D5", "Birds",
            "animal_flamingo", "silence",
            listOf(
                "Flamingos are pink because of the shrimp they eat.",
                "Flamingos often stand on one leg to save body heat.",
                "A group of flamingos is called a flamboyance."
            ),
            listOf(
                "\u05E4\u05DC\u05DE\u05D9\u05E0\u05D2\u05D5\u05D9\u05DD \u05D5\u05E8\u05D5\u05D3\u05D9\u05DD \u05D1\u05D2\u05DC\u05DC \u05D4\u05E9\u05E8\u05D9\u05DE\u05E4\u05E1 \u05E9\u05D4\u05DD \u05D0\u05D5\u05DB\u05DC\u05D9\u05DD.",
                "\u05E4\u05DC\u05DE\u05D9\u05E0\u05D2\u05D5\u05D9\u05DD \u05E2\u05D5\u05DE\u05D3\u05D9\u05DD \u05E2\u05DC \u05E8\u05D2\u05DC \u05D0\u05D7\u05EA \u05DB\u05D3\u05D9 \u05DC\u05E9\u05DE\u05D5\u05E8 \u05E2\u05DC \u05D7\u05D5\u05DD \u05D4\u05D2\u05D5\u05E3.",
                "\u05DC\u05E7\u05D1\u05D5\u05E6\u05EA \u05E4\u05DC\u05DE\u05D9\u05E0\u05D2\u05D5\u05D9\u05DD \u05E7\u05D5\u05E8\u05D0\u05D9\u05DD \u05D1\u05D0\u05E0\u05D2\u05DC\u05D9\u05EA flamboyance."
            ),
            "I am a tall pink bird that likes to stand on one leg. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E6\u05D9\u05E4\u05D5\u05E8 \u05D2\u05D1\u05D5\u05D4 \u05D5\u05D5\u05E8\u05D5\u05D3 \u05E9\u05D0\u05D5\u05D4\u05D1 \u05DC\u05E2\u05DE\u05D5\u05D3 \u05E2\u05DC \u05E8\u05D2\u05DC \u05D0\u05D7\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Shrimp, algae, and small crustaceans", "\u05E9\u05E8\u05D9\u05DE\u05E4\u05E1, \u05D0\u05E6\u05D5\u05EA \u05D5\u05E1\u05E8\u05D8\u05E0\u05D9\u05DD \u05E7\u05D8\u05E0\u05D9\u05DD",
            "Shallow lakes and lagoons", "\u05D0\u05D2\u05DE\u05D9\u05DD \u05D5\u05DC\u05D2\u05D5\u05E0\u05D5\u05EA \u05E8\u05D3\u05D5\u05D3\u05D9\u05DD",
            "Tall \u2013 about 1.1\u20131.5 m tall"
        ),
        // 34. Peacock
        animalInsert(
            "Peacock", "\u05D8\u05D5\u05D5\u05E1", "Birds",
            "animal_peacock", "silence",
            listOf(
                "Only male peacocks have the colorful fan-shaped tail.",
                "A peacock's tail feathers can be 1.5 meters long.",
                "Peacocks can fly short distances despite their big tails."
            ),
            listOf(
                "\u05E8\u05E7 \u05DC\u05D8\u05D5\u05D5\u05E1 \u05D4\u05D6\u05DB\u05E8 \u05D9\u05E9 \u05D0\u05EA \u05D4\u05D6\u05E0\u05D1 \u05D4\u05E6\u05D1\u05E2\u05D5\u05E0\u05D9 \u05D4\u05DE\u05E4\u05D5\u05D0\u05E8.",
                "\u05E0\u05D5\u05E6\u05D5\u05EA \u05D4\u05D6\u05E0\u05D1 \u05E9\u05DC \u05D8\u05D5\u05D5\u05E1 \u05D9\u05DB\u05D5\u05DC\u05D5\u05EA \u05DC\u05D4\u05D2\u05D9\u05E2 \u05DC-1.5 \u05DE\u05D8\u05E8.",
                "\u05D8\u05D5\u05D5\u05E1\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E2\u05D5\u05E3 \u05DE\u05E8\u05D7\u05E7\u05D9\u05DD \u05E7\u05E6\u05E8\u05D9\u05DD \u05DC\u05DE\u05E8\u05D5\u05EA \u05D4\u05D6\u05E0\u05D1 \u05D4\u05D2\u05D3\u05D5\u05DC."
            ),
            "I spread a huge colorful tail like a fan. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E4\u05D5\u05E8\u05E9 \u05D6\u05E0\u05D1 \u05E6\u05D1\u05E2\u05D5\u05E0\u05D9 \u05E2\u05E0\u05E7 \u05DB\u05DE\u05D5 \u05DE\u05E0\u05D9\u05E4\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Seeds, insects, and small lizards", "\u05D6\u05E8\u05E2\u05D9\u05DD, \u05D7\u05E8\u05E7\u05D9\u05DD \u05D5\u05DC\u05D8\u05D0\u05D5\u05EA \u05E7\u05D8\u05E0\u05D5\u05EA",
            "Forests and gardens in South Asia", "\u05D9\u05E2\u05E8\u05D5\u05EA \u05D5\u05D2\u05E0\u05D5\u05EA \u05D1\u05D3\u05E8\u05D5\u05DD \u05D0\u05E1\u05D9\u05D4",
            "Large \u2013 about 1\u20131.2 m body length"
        ),
        // 35. Hummingbird
        animalInsert(
            "Hummingbird", "\u05D9\u05D5\u05E0\u05E7 \u05D3\u05D1\u05E9", "Birds",
            "animal_hummingbird", "silence",
            listOf(
                "Hummingbirds can fly backwards and hover in place.",
                "A hummingbird's heart beats over 1,000 times per minute.",
                "Hummingbirds are the smallest birds in the world."
            ),
            listOf(
                "\u05D9\u05D5\u05E0\u05E7\u05D9 \u05D3\u05D1\u05E9 \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E2\u05D5\u05E3 \u05D0\u05D7\u05D5\u05E8\u05D4 \u05D5\u05DC\u05E8\u05D7\u05E3 \u05D1\u05DE\u05E7\u05D5\u05DD.",
                "\u05D4\u05DC\u05D1 \u05E9\u05DC \u05D9\u05D5\u05E0\u05E7 \u05D3\u05D1\u05E9 \u05E4\u05D5\u05E2\u05DD \u05DE\u05E2\u05DC 1,000 \u05E4\u05E2\u05DE\u05D9\u05DD \u05D1\u05D3\u05E7\u05D4.",
                "\u05D9\u05D5\u05E0\u05E7\u05D9 \u05D3\u05D1\u05E9 \u05D4\u05DD \u05D4\u05E6\u05D9\u05E4\u05D5\u05E8\u05D9\u05DD \u05D4\u05E7\u05D8\u05E0\u05D9\u05DD \u05D1\u05D9\u05D5\u05EA\u05E8 \u05D1\u05E2\u05D5\u05DC\u05DD."
            ),
            "I am the tiniest bird and I can fly in place like a helicopter. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D4\u05E6\u05D9\u05E4\u05D5\u05E8 \u05D4\u05DB\u05D9 \u05E7\u05D8\u05DF \u05D5\u05D0\u05E0\u05D9 \u05D9\u05DB\u05D5\u05DC \u05DC\u05E8\u05D7\u05E3 \u05D1\u05DE\u05E7\u05D5\u05DD \u05DB\u05DE\u05D5 \u05DE\u05E1\u05D5\u05E7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Nectar from flowers", "\u05E6\u05D5\u05E3 \u05DE\u05E4\u05E8\u05D7\u05D9\u05DD",
            "Americas \u2013 from Alaska to Chile", "\u05D4\u05D0\u05DE\u05E8\u05D9\u05E7\u05D5\u05EA \u2013 \u05DE\u05D0\u05DC\u05E1\u05E7\u05D4 \u05E2\u05D3 \u05E6\u05F3\u05D9\u05DC\u05D4",
            "Tiny \u2013 about 5\u201313 cm long"
        ),
        // 36. Toucan
        animalInsert(
            "Toucan", "\u05D8\u05D5\u05E7\u05DF", "Birds",
            "animal_toucan", "silence",
            listOf(
                "A toucan's beak can be one-third of its body length.",
                "Toucans use their big beak to reach fruit on thin branches.",
                "Toucans sleep by tucking their beak over their back."
            ),
            listOf(
                "\u05D4\u05DE\u05E7\u05D5\u05E8 \u05E9\u05DC \u05D8\u05D5\u05E7\u05DF \u05D9\u05DB\u05D5\u05DC \u05DC\u05D4\u05D9\u05D5\u05EA \u05E9\u05DC\u05D9\u05E9 \u05DE\u05D0\u05D5\u05E8\u05DA \u05D2\u05D5\u05E4\u05D5.",
                "\u05D8\u05D5\u05E7\u05E0\u05D9\u05DD \u05DE\u05E9\u05EA\u05DE\u05E9\u05D9\u05DD \u05D1\u05DE\u05E7\u05D5\u05E8 \u05D4\u05D2\u05D3\u05D5\u05DC \u05DB\u05D3\u05D9 \u05DC\u05D4\u05D2\u05D9\u05E2 \u05DC\u05E4\u05D9\u05E8\u05D5\u05EA \u05E2\u05DC \u05E2\u05E0\u05E4\u05D9\u05DD \u05D3\u05E7\u05D9\u05DD.",
                "\u05D8\u05D5\u05E7\u05E0\u05D9\u05DD \u05D9\u05E9\u05E0\u05D9\u05DD \u05DB\u05E9\u05D4\u05DD \u05DE\u05E0\u05D9\u05D7\u05D9\u05DD \u05D0\u05EA \u05D4\u05DE\u05E7\u05D5\u05E8 \u05E2\u05DC \u05D4\u05D2\u05D1."
            ),
            "I have a huge colorful beak and live in the rainforest. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05DE\u05E7\u05D5\u05E8 \u05E6\u05D1\u05E2\u05D5\u05E0\u05D9 \u05E2\u05E0\u05E7 \u05D5\u05D0\u05E0\u05D9 \u05D7\u05D9 \u05D1\u05D9\u05E2\u05E8 \u05D4\u05D2\u05E9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Fruit, insects, and small lizards", "\u05E4\u05D9\u05E8\u05D5\u05EA, \u05D7\u05E8\u05E7\u05D9\u05DD \u05D5\u05DC\u05D8\u05D0\u05D5\u05EA \u05E7\u05D8\u05E0\u05D5\u05EA",
            "Central and South American rainforests", "\u05D9\u05E2\u05E8\u05D5\u05EA \u05D2\u05E9\u05DD \u05D1\u05DE\u05E8\u05DB\u05D6 \u05D5\u05D3\u05E8\u05D5\u05DD \u05D0\u05DE\u05E8\u05D9\u05E7\u05D4",
            "Medium \u2013 about 50\u201365 cm long"
        )
    )

    // ----------------------------------------------------------------
    // Quiz questions – Ocean & Birds (IDs 25–36)
    // ----------------------------------------------------------------
    fun oceanBirdQuizSql(): List<String> = listOf(
        // 25. Whale
        quizInsert(25, "RIDDLE",
            "I am the biggest animal in the sea and spray water from my head. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D4\u05D7\u05D9\u05D4 \u05D4\u05DB\u05D9 \u05D2\u05D3\u05D5\u05DC\u05D4 \u05D1\u05D9\u05DD \u05D5\u05DE\u05EA\u05D9\u05D6 \u05DE\u05D9\u05DD \u05DE\u05D4\u05E8\u05D0\u05E9. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Whale", "\u05DC\u05D5\u05D5\u05D9\u05D9\u05EA\u05DF",
            listOf("Shark", "Dolphin", "Octopus"), listOf("\u05DB\u05E8\u05D9\u05E9", "\u05D3\u05D5\u05DC\u05E4\u05D9\u05DF", "\u05EA\u05DE\u05E0\u05D5\u05DF")),
        // 26. Octopus
        quizInsert(26, "RIDDLE",
            "I have eight arms and can change my color. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05E9\u05DE\u05D5\u05E0\u05D4 \u05D6\u05E8\u05D5\u05E2\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05DE\u05E9\u05E0\u05D4 \u05E6\u05D1\u05E2. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Octopus", "\u05EA\u05DE\u05E0\u05D5\u05DF",
            listOf("Jellyfish", "Squid", "Crab"), listOf("\u05DE\u05D3\u05D5\u05D6\u05D4", "\u05D3\u05D9\u05D5\u05E0\u05D5\u05DF", "\u05E1\u05E8\u05D8\u05DF")),
        // 27. Seahorse
        quizInsert(27, "RIDDLE",
            "I look like a tiny horse that lives in the sea. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E0\u05E8\u05D0\u05D4 \u05DB\u05DE\u05D5 \u05E1\u05D5\u05E1 \u05D6\u05E2\u05D9\u05E8 \u05E9\u05D7\u05D9 \u05D1\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Seahorse", "\u05E1\u05D5\u05E1\u05D5\u05DF \u05D9\u05DD",
            listOf("Starfish", "Clownfish", "Turtle"), listOf("\u05DB\u05D5\u05DB\u05D1 \u05D9\u05DD", "\u05D3\u05D2 \u05DC\u05D9\u05E6\u05DF", "\u05E6\u05D1 \u05D9\u05DD")),
        // 28. Turtle
        quizInsert(28, "RIDDLE",
            "I carry my home on my back and swim in the ocean. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E0\u05D5\u05E9\u05D0 \u05D0\u05EA \u05D4\u05D1\u05D9\u05EA \u05E2\u05DC \u05D4\u05D2\u05D1 \u05D5\u05E9\u05D5\u05D7\u05D4 \u05D1\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Turtle", "\u05E6\u05D1 \u05D9\u05DD",
            listOf("Crab", "Whale", "Seahorse"), listOf("\u05E1\u05E8\u05D8\u05DF", "\u05DC\u05D5\u05D5\u05D9\u05D9\u05EA\u05DF", "\u05E1\u05D5\u05E1\u05D5\u05DF \u05D9\u05DD")),
        // 29. Jellyfish
        quizInsert(29, "RIDDLE",
            "I float in the sea and can sting, but I have no brain. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E6\u05E4\u05D4 \u05D1\u05D9\u05DD \u05D5\u05D9\u05DB\u05D5\u05DC\u05D4 \u05DC\u05E2\u05E7\u05D5\u05E5, \u05D0\u05D1\u05DC \u05D0\u05D9\u05DF \u05DC\u05D9 \u05DE\u05D5\u05D7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Jellyfish", "\u05DE\u05D3\u05D5\u05D6\u05D4",
            listOf("Octopus", "Clownfish", "Whale"), listOf("\u05EA\u05DE\u05E0\u05D5\u05DF", "\u05D3\u05D2 \u05DC\u05D9\u05E6\u05DF", "\u05DC\u05D5\u05D5\u05D9\u05D9\u05EA\u05DF")),
        // 30. Clownfish
        quizInsert(30, "RIDDLE",
            "I am a small orange fish that hides in anemones. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D3\u05D2 \u05E7\u05D8\u05DF \u05D5\u05DB\u05EA\u05D5\u05DD \u05E9\u05DE\u05EA\u05D7\u05D1\u05D0 \u05D1\u05E9\u05D5\u05E9\u05E0\u05EA \u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Clownfish", "\u05D3\u05D2 \u05DC\u05D9\u05E6\u05DF",
            listOf("Seahorse", "Goldfish", "Turtle"), listOf("\u05E1\u05D5\u05E1\u05D5\u05DF \u05D9\u05DD", "\u05D3\u05D2 \u05D6\u05D4\u05D1", "\u05E6\u05D1 \u05D9\u05DD")),
        // 31. Owl
        quizInsert(31, "RIDDLE",
            "I can turn my head almost all the way around and I hunt at night. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05E1\u05D5\u05D1\u05D1 \u05D0\u05EA \u05D4\u05E8\u05D0\u05E9 \u05DB\u05DE\u05E2\u05D8 \u05E1\u05D9\u05D1\u05D5\u05D1 \u05E9\u05DC\u05DD \u05D5\u05E6\u05D3 \u05D1\u05DC\u05D9\u05DC\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Owl", "\u05D9\u05E0\u05E9\u05D5\u05E3",
            listOf("Eagle", "Bat", "Penguin"), listOf("\u05E0\u05E9\u05E8", "\u05E2\u05D8\u05DC\u05E3", "\u05E4\u05D9\u05E0\u05D2\u05D5\u05D5\u05D9\u05DF")),
        // 32. Penguin
        quizInsert(32, "RIDDLE",
            "I am a bird that cannot fly but I love to swim in icy water. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E6\u05D9\u05E4\u05D5\u05E8 \u05E9\u05DC\u05D0 \u05E2\u05E3 \u05D0\u05D1\u05DC \u05D0\u05D5\u05D4\u05D1 \u05DC\u05E9\u05D7\u05D5\u05EA \u05D1\u05DE\u05D9\u05DD \u05E7\u05E8\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Penguin", "\u05E4\u05D9\u05E0\u05D2\u05D5\u05D5\u05D9\u05DF",
            listOf("Owl", "Flamingo", "Duck"), listOf("\u05D9\u05E0\u05E9\u05D5\u05E3", "\u05E4\u05DC\u05DE\u05D9\u05E0\u05D2\u05D5", "\u05D1\u05E8\u05D5\u05D5\u05D6")),
        // 33. Flamingo
        quizInsert(33, "RIDDLE",
            "I am a tall pink bird that likes to stand on one leg. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E6\u05D9\u05E4\u05D5\u05E8 \u05D2\u05D1\u05D5\u05D4 \u05D5\u05D5\u05E8\u05D5\u05D3 \u05E9\u05D0\u05D5\u05D4\u05D1 \u05DC\u05E2\u05DE\u05D5\u05D3 \u05E2\u05DC \u05E8\u05D2\u05DC \u05D0\u05D7\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Flamingo", "\u05E4\u05DC\u05DE\u05D9\u05E0\u05D2\u05D5",
            listOf("Peacock", "Toucan", "Penguin"), listOf("\u05D8\u05D5\u05D5\u05E1", "\u05D8\u05D5\u05E7\u05DF", "\u05E4\u05D9\u05E0\u05D2\u05D5\u05D5\u05D9\u05DF")),
        // 34. Peacock
        quizInsert(34, "RIDDLE",
            "I spread a huge colorful tail like a fan. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E4\u05D5\u05E8\u05E9 \u05D6\u05E0\u05D1 \u05E6\u05D1\u05E2\u05D5\u05E0\u05D9 \u05E2\u05E0\u05E7 \u05DB\u05DE\u05D5 \u05DE\u05E0\u05D9\u05E4\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Peacock", "\u05D8\u05D5\u05D5\u05E1",
            listOf("Flamingo", "Toucan", "Owl"), listOf("\u05E4\u05DC\u05DE\u05D9\u05E0\u05D2\u05D5", "\u05D8\u05D5\u05E7\u05DF", "\u05D9\u05E0\u05E9\u05D5\u05E3")),
        // 35. Hummingbird
        quizInsert(35, "RIDDLE",
            "I am the tiniest bird and I can fly in place like a helicopter. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D4\u05E6\u05D9\u05E4\u05D5\u05E8 \u05D4\u05DB\u05D9 \u05E7\u05D8\u05DF \u05D5\u05D0\u05E0\u05D9 \u05D9\u05DB\u05D5\u05DC \u05DC\u05E8\u05D7\u05E3 \u05D1\u05DE\u05E7\u05D5\u05DD \u05DB\u05DE\u05D5 \u05DE\u05E1\u05D5\u05E7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Hummingbird", "\u05D9\u05D5\u05E0\u05E7 \u05D3\u05D1\u05E9",
            listOf("Penguin", "Owl", "Flamingo"), listOf("\u05E4\u05D9\u05E0\u05D2\u05D5\u05D5\u05D9\u05DF", "\u05D9\u05E0\u05E9\u05D5\u05E3", "\u05E4\u05DC\u05DE\u05D9\u05E0\u05D2\u05D5")),
        // 36. Toucan
        quizInsert(36, "RIDDLE",
            "I have a huge colorful beak and live in the rainforest. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05DE\u05E7\u05D5\u05E8 \u05E6\u05D1\u05E2\u05D5\u05E0\u05D9 \u05E2\u05E0\u05E7 \u05D5\u05D0\u05E0\u05D9 \u05D7\u05D9 \u05D1\u05D9\u05E2\u05E8 \u05D4\u05D2\u05E9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Toucan", "\u05D8\u05D5\u05E7\u05DF",
            listOf("Peacock", "Flamingo", "Hummingbird"), listOf("\u05D8\u05D5\u05D5\u05E1", "\u05E4\u05DC\u05DE\u05D9\u05E0\u05D2\u05D5", "\u05D9\u05D5\u05E0\u05E7 \u05D3\u05D1\u05E9"))
    )

    // ----------------------------------------------------------------
    // Pet animals (IDs 37–42)
    // ----------------------------------------------------------------
    fun petAnimalSql(): List<String> = listOf(
        // 37. Hamster
        animalInsert(
            "Hamster", "\u05D0\u05D5\u05D2\u05E8", "Pets",
            "animal_hamster", "silence",
            listOf(
                "Hamsters can store food in their cheek pouches.",
                "Hamsters are nocturnal and most active at night.",
                "A hamster's teeth never stop growing."
            ),
            listOf(
                "\u05D0\u05D5\u05D2\u05E8\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D0\u05D7\u05E1\u05DF \u05D0\u05D5\u05DB\u05DC \u05D1\u05DB\u05D9\u05E1\u05D9 \u05D4\u05DC\u05D7\u05D9\u05D9\u05DD \u05E9\u05DC\u05D4\u05DD.",
                "\u05D0\u05D5\u05D2\u05E8\u05D9\u05DD \u05D4\u05DD \u05D7\u05D9\u05D5\u05EA \u05DC\u05D9\u05DC\u05D9\u05D5\u05EA \u05D5\u05E4\u05E2\u05D9\u05DC\u05D9\u05DD \u05D1\u05E2\u05D9\u05E7\u05E8 \u05D1\u05DC\u05D9\u05DC\u05D4.",
                "\u05D4\u05E9\u05D9\u05E0\u05D9\u05D9\u05DD \u05E9\u05DC \u05D0\u05D5\u05D2\u05E8 \u05DC\u05E2\u05D5\u05DC\u05DD \u05DC\u05D0 \u05DE\u05E4\u05E1\u05D9\u05E7\u05D5\u05EA \u05DC\u05D2\u05D3\u05D5\u05DC."
            ),
            "I have puffy cheeks and run on a wheel at night. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05DC\u05D7\u05D9\u05D9\u05DD \u05E0\u05E4\u05D5\u05D7\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05E8\u05E5 \u05E2\u05DC \u05D2\u05DC\u05D2\u05DC \u05D1\u05DC\u05D9\u05DC\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Seeds, grains, and vegetables", "\u05D6\u05E8\u05E2\u05D9\u05DD, \u05D3\u05D2\u05E0\u05D9\u05DD \u05D5\u05D9\u05E8\u05E7\u05D5\u05EA",
            "Cages and burrows in dry areas", "\u05DB\u05DC\u05D5\u05D1\u05D9\u05DD \u05D5\u05DE\u05D7\u05D9\u05DC\u05D5\u05EA \u05D1\u05D0\u05D6\u05D5\u05E8\u05D9\u05DD \u05D9\u05D1\u05E9\u05D9\u05DD",
            "Very small \u2013 about 13 cm long"
        ),
        // 38. Rabbit
        animalInsert(
            "Rabbit", "\u05D0\u05E8\u05E0\u05D1", "Pets",
            "animal_rabbit", "silence",
            listOf(
                "Rabbits can see almost 360 degrees around them.",
                "A rabbit's teeth never stop growing throughout its life.",
                "Rabbits communicate by thumping their hind legs."
            ),
            listOf(
                "\u05D0\u05E8\u05E0\u05D1\u05D5\u05EA \u05D9\u05DB\u05D5\u05DC\u05D5\u05EA \u05DC\u05E8\u05D0\u05D5\u05EA \u05DB\u05DE\u05E2\u05D8 360 \u05DE\u05E2\u05DC\u05D5\u05EA \u05E1\u05D1\u05D9\u05D1\u05DF.",
                "\u05D4\u05E9\u05D9\u05E0\u05D9\u05D9\u05DD \u05E9\u05DC \u05D0\u05E8\u05E0\u05D1 \u05DC\u05E2\u05D5\u05DC\u05DD \u05DC\u05D0 \u05DE\u05E4\u05E1\u05D9\u05E7\u05D5\u05EA \u05DC\u05D2\u05D3\u05D5\u05DC.",
                "\u05D0\u05E8\u05E0\u05D1\u05D5\u05EA \u05DE\u05EA\u05E7\u05E9\u05E8\u05D5\u05EA \u05E2\u05DC \u05D9\u05D3\u05D9 \u05D3\u05E4\u05D9\u05E7\u05D4 \u05D1\u05E8\u05D2\u05DC\u05D9\u05D9\u05DD \u05D4\u05D0\u05D7\u05D5\u05E8\u05D9\u05D5\u05EA."
            ),
            "I have long ears, a fluffy tail, and I love carrots. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD \u05D0\u05E8\u05D5\u05DB\u05D5\u05EA, \u05D6\u05E0\u05D1 \u05E4\u05E8\u05D5\u05D5\u05EA\u05D9 \u05D5\u05D0\u05E0\u05D9 \u05D0\u05D5\u05D4\u05D1 \u05D2\u05D6\u05E8\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Hay, vegetables, and leafy greens", "\u05D7\u05E6\u05D9\u05E8, \u05D9\u05E8\u05E7\u05D5\u05EA \u05D5\u05E2\u05DC\u05D9\u05DD \u05D9\u05E8\u05D5\u05E7\u05D9\u05DD",
            "Meadows, gardens, and homes", "\u05D0\u05D7\u05D5\u05D5\u05EA, \u05D2\u05D9\u05E0\u05D5\u05EA \u05D5\u05D1\u05EA\u05D9\u05DD",
            "Small \u2013 about 20\u201340 cm long"
        ),
        // 39. Goldfish
        animalInsert(
            "Goldfish", "\u05D3\u05D2 \u05D6\u05D4\u05D1", "Pets",
            "animal_goldfish", "silence",
            listOf(
                "Goldfish can live for over 10 years with good care.",
                "Goldfish can see more colors than humans can.",
                "Goldfish do not have stomachs; they digest food along the way."
            ),
            listOf(
                "\u05D3\u05D2 \u05D6\u05D4\u05D1 \u05D9\u05DB\u05D5\u05DC \u05DC\u05D7\u05D9\u05D5\u05EA \u05DE\u05E2\u05DC 10 \u05E9\u05E0\u05D9\u05DD \u05D1\u05D8\u05D9\u05E4\u05D5\u05DC \u05E0\u05DB\u05D5\u05DF.",
                "\u05D3\u05D2\u05D9 \u05D6\u05D4\u05D1 \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E8\u05D0\u05D5\u05EA \u05D9\u05D5\u05EA\u05E8 \u05E6\u05D1\u05E2\u05D9\u05DD \u05DE\u05D1\u05E0\u05D9 \u05D0\u05D3\u05DD.",
                "\u05DC\u05D3\u05D2\u05D9 \u05D6\u05D4\u05D1 \u05D0\u05D9\u05DF \u05E7\u05D9\u05D1\u05D4; \u05D4\u05DD \u05DE\u05E2\u05DB\u05DC\u05D9\u05DD \u05D0\u05EA \u05D4\u05D0\u05D5\u05DB\u05DC \u05DC\u05D0\u05D5\u05E8\u05DA \u05D4\u05D3\u05E8\u05DA."
            ),
            "I am small, orange, and swim in a bowl. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D8\u05DF, \u05DB\u05EA\u05D5\u05DD \u05D5\u05E9\u05D5\u05D7\u05D4 \u05D1\u05E7\u05E2\u05E8\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Fish flakes and small pellets", "\u05E4\u05EA\u05D9\u05EA\u05D9 \u05D3\u05D2\u05D9\u05DD \u05D5\u05D2\u05E8\u05D2\u05D9\u05E8\u05D9\u05DD \u05E7\u05D8\u05E0\u05D9\u05DD",
            "Freshwater ponds and aquariums", "\u05D1\u05E8\u05D9\u05DB\u05D5\u05EA \u05DE\u05D9\u05DD \u05DE\u05EA\u05D5\u05E7\u05D9\u05DD \u05D5\u05D0\u05E7\u05D5\u05D5\u05E8\u05D9\u05D5\u05DE\u05D9\u05DD",
            "Very small \u2013 about 10\u201320 cm long"
        ),
        // 40. Pet Turtle
        animalInsert(
            "PetTurtle", "\u05E6\u05D1", "Pets",
            "animal_petturtle", "silence",
            listOf(
                "Turtles have been on Earth for over 200 million years.",
                "A turtle's shell is part of its skeleton.",
                "Some pet turtles can live for over 50 years."
            ),
            listOf(
                "\u05E6\u05D1\u05D9\u05DD \u05D7\u05D9\u05D9\u05DD \u05E2\u05DC \u05D4\u05D0\u05E8\u05E5 \u05DB\u05D1\u05E8 \u05DE\u05E2\u05DC 200 \u05DE\u05D9\u05DC\u05D9\u05D5\u05DF \u05E9\u05E0\u05D4.",
                "\u05D4\u05E9\u05E8\u05D9\u05D5\u05DF \u05E9\u05DC \u05E6\u05D1 \u05D4\u05D5\u05D0 \u05D7\u05DC\u05E7 \u05DE\u05D4\u05E9\u05DC\u05D3 \u05E9\u05DC\u05D5.",
                "\u05D9\u05E9 \u05E6\u05D1\u05D9\u05DD \u05E9\u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D7\u05D9\u05D5\u05EA \u05DE\u05E2\u05DC 50 \u05E9\u05E0\u05D4."
            ),
            "I carry my house on my back and walk very slowly. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E0\u05D5\u05E9\u05D0 \u05D0\u05EA \u05D4\u05D1\u05D9\u05EA \u05E9\u05DC\u05D9 \u05E2\u05DC \u05D4\u05D2\u05D1 \u05D5\u05D4\u05D5\u05DC\u05DA \u05DC\u05D0\u05D8 \u05DE\u05D0\u05D5\u05D3. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Vegetables, fruits, and insects", "\u05D9\u05E8\u05E7\u05D5\u05EA, \u05E4\u05D9\u05E8\u05D5\u05EA \u05D5\u05D7\u05E8\u05E7\u05D9\u05DD",
            "Ponds, gardens, and terrariums", "\u05D1\u05E8\u05D9\u05DB\u05D5\u05EA, \u05D2\u05D9\u05E0\u05D5\u05EA \u05D5\u05D8\u05E8\u05E8\u05D9\u05D5\u05DE\u05D9\u05DD",
            "Small \u2013 about 15\u201330 cm long"
        ),
        // 41. Guinea Pig
        animalInsert(
            "GuineaPig", "\u05E9\u05E8\u05E7\u05DF", "Pets",
            "animal_guineapig", "silence",
            listOf(
                "Guinea pigs purr like cats when they are happy.",
                "Guinea pigs are born with fur and open eyes.",
                "Guinea pigs need vitamin C in their diet, just like humans."
            ),
            listOf(
                "\u05E9\u05E8\u05E7\u05E0\u05D9\u05DD \u05DE\u05E8\u05E4\u05E8\u05E4\u05D9\u05DD \u05DB\u05DE\u05D5 \u05D7\u05EA\u05D5\u05DC\u05D9\u05DD \u05DB\u05E9\u05D4\u05DD \u05E9\u05DE\u05D7\u05D9\u05DD.",
                "\u05E9\u05E8\u05E7\u05E0\u05D9\u05DD \u05E0\u05D5\u05DC\u05D3\u05D9\u05DD \u05E2\u05DD \u05E4\u05E8\u05D5\u05D5\u05D4 \u05D5\u05E2\u05D9\u05E0\u05D9\u05D9\u05DD \u05E4\u05EA\u05D5\u05D7\u05D5\u05EA.",
                "\u05E9\u05E8\u05E7\u05E0\u05D9\u05DD \u05E6\u05E8\u05D9\u05DB\u05D9\u05DD \u05D5\u05D9\u05D8\u05DE\u05D9\u05DF C \u05D1\u05DE\u05D6\u05D5\u05DF, \u05D1\u05D3\u05D9\u05D5\u05E7 \u05DB\u05DE\u05D5 \u05D1\u05E0\u05D9 \u05D0\u05D3\u05DD."
            ),
            "I am small and furry and I squeak when I am happy. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D8\u05DF \u05D5\u05E4\u05E8\u05D5\u05D5\u05EA\u05D9 \u05D5\u05DE\u05E6\u05E4\u05E6\u05E3 \u05DB\u05E9\u05D0\u05E0\u05D9 \u05E9\u05DE\u05D7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Hay, vegetables, and fruits", "\u05D7\u05E6\u05D9\u05E8, \u05D9\u05E8\u05E7\u05D5\u05EA \u05D5\u05E4\u05D9\u05E8\u05D5\u05EA",
            "Grasslands and homes", "\u05DE\u05E8\u05E2\u05D9\u05DD \u05D5\u05D1\u05EA\u05D9\u05DD",
            "Small \u2013 about 20\u201325 cm long"
        ),
        // 42. Budgie
        animalInsert(
            "Budgie", "\u05EA\u05D5\u05DB\u05D5\u05DF", "Pets",
            "animal_budgie", "silence",
            listOf(
                "Budgies can learn to mimic human speech.",
                "Budgies are one of the most popular pet birds in the world.",
                "A budgie's heart beats over 300 times per minute."
            ),
            listOf(
                "\u05EA\u05D5\u05DB\u05D5\u05E0\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05DC\u05DE\u05D5\u05D3 \u05DC\u05D7\u05E7\u05D5\u05EA \u05D3\u05D9\u05D1\u05D5\u05E8 \u05D0\u05E0\u05D5\u05E9\u05D9.",
                "\u05EA\u05D5\u05DB\u05D5\u05E0\u05D9\u05DD \u05D4\u05DD \u05DE\u05E6\u05D9\u05E4\u05D5\u05E8\u05D9 \u05D4\u05DE\u05D7\u05DE\u05D3 \u05D4\u05E4\u05D5\u05E4\u05D5\u05DC\u05E8\u05D9\u05D9\u05DD \u05D1\u05D9\u05D5\u05EA\u05E8 \u05D1\u05E2\u05D5\u05DC\u05DD.",
                "\u05D4\u05DC\u05D1 \u05E9\u05DC \u05EA\u05D5\u05DB\u05D5\u05DF \u05E4\u05D5\u05E2\u05DD \u05DE\u05E2\u05DC 300 \u05E4\u05E2\u05DE\u05D9\u05DD \u05D1\u05D3\u05E7\u05D4."
            ),
            "I am a small colorful bird that can talk like a person. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E6\u05D9\u05E4\u05D5\u05E8 \u05E7\u05D8\u05DF \u05D5\u05E6\u05D1\u05E2\u05D5\u05E0\u05D9 \u05E9\u05D9\u05DB\u05D5\u05DC \u05DC\u05D3\u05D1\u05E8 \u05DB\u05DE\u05D5 \u05D1\u05DF \u05D0\u05D3\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Seeds, fruits, and vegetables", "\u05D6\u05E8\u05E2\u05D9\u05DD, \u05E4\u05D9\u05E8\u05D5\u05EA \u05D5\u05D9\u05E8\u05E7\u05D5\u05EA",
            "Australian grasslands and homes", "\u05DE\u05E8\u05E2\u05D9\u05DD \u05D1\u05D0\u05D5\u05E1\u05D8\u05E8\u05DC\u05D9\u05D4 \u05D5\u05D1\u05EA\u05D9\u05DD",
            "Very small \u2013 about 18 cm long"
        )
    )

    // ----------------------------------------------------------------
    // Insect animals (IDs 43–50)
    // ----------------------------------------------------------------
    fun insectAnimalSql(): List<String> = listOf(
        // 43. Butterfly
        animalInsert(
            "Butterfly", "\u05E4\u05E8\u05E4\u05E8", "Insects",
            "animal_butterfly", "silence",
            listOf(
                "Butterflies taste with their feet.",
                "A butterfly starts life as a caterpillar before transforming.",
                "Some butterflies migrate thousands of kilometers."
            ),
            listOf(
                "\u05E4\u05E8\u05E4\u05E8\u05D9\u05DD \u05D8\u05D5\u05E2\u05DE\u05D9\u05DD \u05D1\u05E2\u05D6\u05E8\u05EA \u05D4\u05E8\u05D2\u05DC\u05D9\u05D9\u05DD.",
                "\u05E4\u05E8\u05E4\u05E8 \u05DE\u05EA\u05D7\u05D9\u05DC \u05D0\u05EA \u05D7\u05D9\u05D9\u05D5 \u05DB\u05D6\u05D7\u05DC \u05DC\u05E4\u05E0\u05D9 \u05E9\u05D4\u05D5\u05D0 \u05E2\u05D5\u05D1\u05E8 \u05DE\u05D8\u05DE\u05D5\u05E8\u05E4\u05D5\u05D6\u05D4.",
                "\u05D9\u05E9 \u05E4\u05E8\u05E4\u05E8\u05D9\u05DD \u05E9\u05E0\u05D5\u05D3\u05D3\u05D9\u05DD \u05D0\u05DC\u05E4\u05D9 \u05E7\u05D9\u05DC\u05D5\u05DE\u05D8\u05E8\u05D9\u05DD."
            ),
            "I start as a caterpillar and then grow beautiful wings. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05EA\u05D7\u05D9\u05DC \u05DB\u05D6\u05D7\u05DC \u05D5\u05D0\u05D7\u05E8 \u05DB\u05DA \u05DE\u05E6\u05DE\u05D9\u05D7 \u05DB\u05E0\u05E4\u05D9\u05D9\u05DD \u05D9\u05E4\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Nectar from flowers", "\u05E6\u05D5\u05E3 \u05DE\u05E4\u05E8\u05D7\u05D9\u05DD",
            "Gardens, meadows, and forests", "\u05D2\u05D9\u05E0\u05D5\u05EA, \u05D0\u05D7\u05D5\u05D5\u05EA \u05D5\u05D9\u05E2\u05E8\u05D5\u05EA",
            "Very small \u2013 wingspan about 5\u201312 cm"
        ),
        // 44. Bee
        animalInsert(
            "Bee", "\u05D3\u05D1\u05D5\u05E8\u05D4", "Insects",
            "animal_bee", "silence",
            listOf(
                "Bees communicate by dancing to show where flowers are.",
                "A bee visits about 2,000 flowers every day.",
                "Honey bees make honey to feed the hive during winter."
            ),
            listOf(
                "\u05D3\u05D1\u05D5\u05E8\u05D9\u05DD \u05DE\u05EA\u05E7\u05E9\u05E8\u05D5\u05EA \u05E2\u05DC \u05D9\u05D3\u05D9 \u05E8\u05D9\u05E7\u05D5\u05D3 \u05DB\u05D3\u05D9 \u05DC\u05D4\u05E8\u05D0\u05D5\u05EA \u05D0\u05D9\u05E4\u05D4 \u05D4\u05E4\u05E8\u05D7\u05D9\u05DD.",
                "\u05D3\u05D1\u05D5\u05E8\u05D4 \u05DE\u05D1\u05E7\u05E8\u05EA \u05D1\u05DB-2,000 \u05E4\u05E8\u05D7\u05D9\u05DD \u05DB\u05DC \u05D9\u05D5\u05DD.",
                "\u05D3\u05D1\u05D5\u05E8\u05D9 \u05D3\u05D1\u05E9 \u05DE\u05D9\u05D9\u05E6\u05E8\u05D5\u05EA \u05D3\u05D1\u05E9 \u05DB\u05D3\u05D9 \u05DC\u05D4\u05D0\u05DB\u05D9\u05DC \u05D0\u05EA \u05D4\u05DB\u05D5\u05D5\u05E8\u05EA \u05D1\u05D7\u05D5\u05E8\u05E3."
            ),
            "I buzz around flowers and make sweet honey. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D6\u05DE\u05D6\u05DE\u05EA \u05E1\u05D1\u05D9\u05D1 \u05E4\u05E8\u05D7\u05D9\u05DD \u05D5\u05DE\u05DB\u05D9\u05E0\u05D4 \u05D3\u05D1\u05E9 \u05DE\u05EA\u05D5\u05E7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Nectar and pollen", "\u05E6\u05D5\u05E3 \u05D5\u05D0\u05D1\u05E7\u05EA \u05E4\u05E8\u05D7\u05D9\u05DD",
            "Gardens, meadows, and hives", "\u05D2\u05D9\u05E0\u05D5\u05EA, \u05D0\u05D7\u05D5\u05D5\u05EA \u05D5\u05DB\u05D5\u05D5\u05E8\u05D5\u05EA",
            "Very small \u2013 about 1.5 cm long"
        ),
        // 45. Ladybug
        animalInsert(
            "Ladybug", "\u05D7\u05D9\u05E4\u05D5\u05E9\u05D9\u05EA", "Insects",
            "animal_ladybug", "silence",
            listOf(
                "Ladybugs can eat up to 5,000 aphids in their lifetime.",
                "A ladybug's bright color warns predators that it tastes bad.",
                "Ladybugs fold their wings under their spotted shell."
            ),
            listOf(
                "\u05D7\u05D9\u05E4\u05D5\u05E9\u05D9\u05D5\u05EA \u05D9\u05DB\u05D5\u05DC\u05D5\u05EA \u05DC\u05D0\u05DB\u05D5\u05DC \u05E2\u05D3 5,000 \u05DB\u05E0\u05D9\u05DE\u05D5\u05EA \u05D1\u05D7\u05D9\u05D9\u05D4\u05DF.",
                "\u05D4\u05E6\u05D1\u05E2 \u05D4\u05D1\u05D5\u05DC\u05D8 \u05E9\u05DC \u05D7\u05D9\u05E4\u05D5\u05E9\u05D9\u05EA \u05DE\u05D6\u05D4\u05D9\u05E8 \u05D8\u05D5\u05E8\u05E4\u05D9\u05DD \u05E9\u05D4\u05D8\u05E2\u05DD \u05E9\u05DC\u05D4 \u05E8\u05E2.",
                "\u05D7\u05D9\u05E4\u05D5\u05E9\u05D9\u05D5\u05EA \u05DE\u05E7\u05E4\u05DC\u05D5\u05EA \u05D0\u05EA \u05D4\u05DB\u05E0\u05E4\u05D9\u05D9\u05DD \u05DE\u05EA\u05D7\u05EA \u05DC\u05E9\u05E8\u05D9\u05D5\u05DF \u05D4\u05DE\u05E0\u05D5\u05E7\u05D3."
            ),
            "I am a tiny red bug with black spots. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D7\u05E8\u05E7 \u05E7\u05D8\u05DF \u05D0\u05D3\u05D5\u05DD \u05E2\u05DD \u05E0\u05E7\u05D5\u05D3\u05D5\u05EA \u05E9\u05D7\u05D5\u05E8\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Aphids and small insects", "\u05DB\u05E0\u05D9\u05DE\u05D5\u05EA \u05D5\u05D7\u05E8\u05E7\u05D9\u05DD \u05E7\u05D8\u05E0\u05D9\u05DD",
            "Gardens, fields, and forests", "\u05D2\u05D9\u05E0\u05D5\u05EA, \u05E9\u05D3\u05D5\u05EA \u05D5\u05D9\u05E2\u05E8\u05D5\u05EA",
            "Very small \u2013 about 0.5\u20131 cm long"
        ),
        // 46. Ant
        animalInsert(
            "Ant", "\u05E0\u05DE\u05DC\u05D4", "Insects",
            "animal_ant", "silence",
            listOf(
                "Ants can carry objects 50 times their own body weight.",
                "Ants live in large colonies that can have millions of members.",
                "Ants communicate using chemicals called pheromones."
            ),
            listOf(
                "\u05E0\u05DE\u05DC\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D5\u05EA \u05DC\u05E9\u05D0\u05EA \u05D7\u05E4\u05E6\u05D9\u05DD \u05E9\u05DE\u05E9\u05E7\u05DC\u05DD \u05E4\u05D9 50 \u05DE\u05DE\u05E9\u05E7\u05DC \u05D2\u05D5\u05E4\u05DF.",
                "\u05E0\u05DE\u05DC\u05D9\u05DD \u05D7\u05D9\u05D5\u05EA \u05D1\u05DE\u05D5\u05E9\u05D1\u05D5\u05EA \u05D2\u05D3\u05D5\u05DC\u05D5\u05EA \u05E9\u05D9\u05DB\u05D5\u05DC\u05D5\u05EA \u05DC\u05D4\u05DB\u05D9\u05DC \u05DE\u05D9\u05DC\u05D9\u05D5\u05E0\u05D9 \u05E4\u05E8\u05D8\u05D9\u05DD.",
                "\u05E0\u05DE\u05DC\u05D9\u05DD \u05DE\u05EA\u05E7\u05E9\u05E8\u05D5\u05EA \u05D1\u05E2\u05D6\u05E8\u05EA \u05D7\u05D5\u05DE\u05E8\u05D9\u05DD \u05DB\u05D9\u05DE\u05D9\u05D9\u05DD \u05D4\u05E0\u05E7\u05E8\u05D0\u05D9\u05DD \u05E4\u05E8\u05D5\u05DE\u05D5\u05E0\u05D9\u05DD."
            ),
            "I am very tiny but very strong and I live in a big colony. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D8\u05E0\u05D8\u05E0\u05D4 \u05D0\u05D1\u05DC \u05D7\u05D6\u05E7\u05D4 \u05DE\u05D0\u05D5\u05D3 \u05D5\u05D7\u05D9\u05D4 \u05D1\u05DE\u05D5\u05E9\u05D1\u05D4 \u05D2\u05D3\u05D5\u05DC\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Seeds, leaves, and other insects", "\u05D6\u05E8\u05E2\u05D9\u05DD, \u05E2\u05DC\u05D9\u05DD \u05D5\u05D7\u05E8\u05E7\u05D9\u05DD \u05D0\u05D7\u05E8\u05D9\u05DD",
            "Underground nests worldwide", "\u05E7\u05D9\u05E0\u05D9\u05DD \u05EA\u05EA-\u05E7\u05E8\u05E7\u05E2\u05D9\u05D9\u05DD \u05D1\u05E8\u05D7\u05D1\u05D9 \u05D4\u05E2\u05D5\u05DC\u05DD",
            "Very small \u2013 about 1\u20135 mm long"
        ),
        // 47. Dragonfly
        animalInsert(
            "Dragonfly", "\u05E9\u05E4\u05D9\u05E8\u05D9\u05EA", "Insects",
            "animal_dragonfly", "silence",
            listOf(
                "Dragonflies can fly in all directions, including backwards.",
                "Dragonflies have been on Earth for over 300 million years.",
                "A dragonfly can catch prey mid-flight with incredible accuracy."
            ),
            listOf(
                "\u05E9\u05E4\u05D9\u05E8\u05D9\u05D5\u05EA \u05D9\u05DB\u05D5\u05DC\u05D5\u05EA \u05DC\u05E2\u05D5\u05E3 \u05DC\u05DB\u05DC \u05D4\u05DB\u05D9\u05D5\u05D5\u05E0\u05D9\u05DD, \u05DB\u05D5\u05DC\u05DC \u05D0\u05D7\u05D5\u05E8\u05D4.",
                "\u05E9\u05E4\u05D9\u05E8\u05D9\u05D5\u05EA \u05D7\u05D9\u05D5\u05EA \u05E2\u05DC \u05D4\u05D0\u05E8\u05E5 \u05DB\u05D1\u05E8 \u05DE\u05E2\u05DC 300 \u05DE\u05D9\u05DC\u05D9\u05D5\u05DF \u05E9\u05E0\u05D4.",
                "\u05E9\u05E4\u05D9\u05E8\u05D9\u05EA \u05D9\u05DB\u05D5\u05DC\u05D4 \u05DC\u05EA\u05E4\u05D5\u05E1 \u05D8\u05E8\u05E3 \u05D1\u05D0\u05DE\u05E6\u05E2 \u05D4\u05EA\u05E2\u05D5\u05E4\u05D4 \u05D1\u05D3\u05D9\u05D5\u05E7 \u05DE\u05D3\u05D4\u05D9\u05DD."
            ),
            "I have four see-through wings and zoom over ponds. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05D0\u05E8\u05D1\u05E2 \u05DB\u05E0\u05E4\u05D9\u05D9\u05DD \u05E9\u05E7\u05D5\u05E4\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05D3\u05D5\u05D4\u05E8\u05EA \u05DE\u05E2\u05DC \u05D1\u05E8\u05D9\u05DB\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Mosquitoes and small insects", "\u05D9\u05EA\u05D5\u05E9\u05D9\u05DD \u05D5\u05D7\u05E8\u05E7\u05D9\u05DD \u05E7\u05D8\u05E0\u05D9\u05DD",
            "Near ponds, lakes, and wetlands", "\u05DC\u05D9\u05D3 \u05D1\u05E8\u05D9\u05DB\u05D5\u05EA, \u05D0\u05D2\u05DE\u05D9\u05DD \u05D5\u05D1\u05D9\u05E6\u05D5\u05EA",
            "Small \u2013 about 5\u201310 cm long"
        ),
        // 48. Grasshopper
        animalInsert(
            "Grasshopper", "\u05D7\u05D2\u05D1", "Insects",
            "animal_grasshopper", "silence",
            listOf(
                "Grasshoppers can jump up to 20 times their own body length.",
                "Grasshoppers have ears on their bellies, not on their heads.",
                "Some grasshoppers can fly as well as jump."
            ),
            listOf(
                "\u05D7\u05D2\u05D1\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05E7\u05E4\u05D5\u05E5 \u05E4\u05D9 20 \u05DE\u05D0\u05D5\u05E8\u05DA \u05D2\u05D5\u05E4\u05DD.",
                "\u05DC\u05D7\u05D2\u05D1\u05D9\u05DD \u05D9\u05E9 \u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD \u05E2\u05DC \u05D4\u05D1\u05D8\u05DF, \u05DC\u05D0 \u05E2\u05DC \u05D4\u05E8\u05D0\u05E9.",
                "\u05D9\u05E9 \u05D7\u05D2\u05D1\u05D9\u05DD \u05E9\u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05D2\u05DD \u05DC\u05E2\u05D5\u05E3 \u05D5\u05DC\u05D0 \u05E8\u05E7 \u05DC\u05E7\u05E4\u05D5\u05E5."
            ),
            "I jump really high in the grass and chirp loudly. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D5\u05E4\u05E5 \u05D2\u05D1\u05D5\u05D4 \u05D1\u05EA\u05D5\u05DA \u05D4\u05E2\u05E9\u05D1 \u05D5\u05DE\u05E6\u05E8\u05E6\u05E8 \u05D1\u05E7\u05D5\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Grass, leaves, and crops", "\u05E2\u05E9\u05D1, \u05E2\u05DC\u05D9\u05DD \u05D5\u05D2\u05D9\u05D3\u05D5\u05DC\u05D9\u05DD",
            "Grasslands and fields worldwide", "\u05DE\u05E8\u05E2\u05D9\u05DD \u05D5\u05E9\u05D3\u05D5\u05EA \u05D1\u05E8\u05D7\u05D1\u05D9 \u05D4\u05E2\u05D5\u05DC\u05DD",
            "Small \u2013 about 2\u20137 cm long"
        ),
        // 49. Firefly
        animalInsert(
            "Firefly", "\u05D2\u05D7\u05DC\u05D9\u05DC\u05D9\u05EA", "Insects",
            "animal_firefly", "silence",
            listOf(
                "Fireflies produce their own light using a chemical reaction.",
                "Firefly light is the most efficient light in the world \u2013 almost no heat.",
                "Each firefly species has its own unique flashing pattern."
            ),
            listOf(
                "\u05D2\u05D7\u05DC\u05D9\u05DC\u05D9\u05D5\u05EA \u05DE\u05D9\u05D9\u05E6\u05E8\u05D5\u05EA \u05D0\u05D5\u05E8 \u05DE\u05E9\u05DC\u05D4\u05DF \u05D1\u05E2\u05D6\u05E8\u05EA \u05EA\u05D2\u05D5\u05D1\u05D4 \u05DB\u05D9\u05DE\u05D9\u05EA.",
                "\u05D4\u05D0\u05D5\u05E8 \u05E9\u05DC \u05D2\u05D7\u05DC\u05D9\u05DC\u05D9\u05EA \u05D4\u05D5\u05D0 \u05D4\u05D9\u05E2\u05D9\u05DC \u05D1\u05D9\u05D5\u05EA\u05E8 \u05D1\u05E2\u05D5\u05DC\u05DD \u2013 \u05DB\u05DE\u05E2\u05D8 \u05D1\u05DC\u05D9 \u05D7\u05D5\u05DD.",
                "\u05DC\u05DB\u05DC \u05DE\u05D9\u05DF \u05D2\u05D7\u05DC\u05D9\u05DC\u05D9\u05EA \u05D9\u05E9 \u05D3\u05E4\u05D5\u05E1 \u05D4\u05D1\u05D4\u05D5\u05D1 \u05D9\u05D9\u05D7\u05D5\u05D3\u05D9 \u05DE\u05E9\u05DC\u05D4."
            ),
            "I glow in the dark on warm summer nights. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D6\u05D5\u05D4\u05E8\u05EA \u05D1\u05D7\u05D5\u05E9\u05DA \u05D1\u05DC\u05D9\u05DC\u05D5\u05EA \u05E7\u05D9\u05E5 \u05D7\u05DE\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Small insects and pollen", "\u05D7\u05E8\u05E7\u05D9\u05DD \u05E7\u05D8\u05E0\u05D9\u05DD \u05D5\u05D0\u05D1\u05E7\u05EA \u05E4\u05E8\u05D7\u05D9\u05DD",
            "Warm forests, fields, and marshes", "\u05D9\u05E2\u05E8\u05D5\u05EA \u05D7\u05DE\u05D9\u05DD, \u05E9\u05D3\u05D5\u05EA \u05D5\u05D1\u05D9\u05E6\u05D5\u05EA",
            "Very small \u2013 about 1\u20132 cm long"
        ),
        // 50. Caterpillar
        animalInsert(
            "Caterpillar", "\u05D6\u05D7\u05DC", "Insects",
            "animal_caterpillar", "silence",
            listOf(
                "Caterpillars have up to 16 legs.",
                "A caterpillar's first meal is usually its own eggshell.",
                "Caterpillars can increase their body weight 10,000 times before becoming a butterfly or moth."
            ),
            listOf(
                "\u05DC\u05D6\u05D7\u05DC\u05D9\u05DD \u05D9\u05E9 \u05E2\u05D3 16 \u05E8\u05D2\u05DC\u05D9\u05D9\u05DD.",
                "\u05D4\u05D0\u05E8\u05D5\u05D7\u05D4 \u05D4\u05E8\u05D0\u05E9\u05D5\u05E0\u05D4 \u05E9\u05DC \u05D6\u05D7\u05DC \u05D4\u05D9\u05D0 \u05D1\u05D3\u05E8\u05DA \u05DB\u05DC\u05DC \u05E7\u05DC\u05D9\u05E4\u05EA \u05D4\u05D1\u05D9\u05E6\u05D4 \u05E9\u05DC\u05D5.",
                "\u05D6\u05D7\u05DC\u05D9\u05DD \u05D9\u05DB\u05D5\u05DC\u05D9\u05DD \u05DC\u05D4\u05D2\u05D3\u05D9\u05DC \u05D0\u05EA \u05DE\u05E9\u05E7\u05DC \u05D2\u05D5\u05E4\u05DD \u05E4\u05D9 10,000 \u05DC\u05E4\u05E0\u05D9 \u05E9\u05D4\u05DD \u05D4\u05D5\u05E4\u05DB\u05D9\u05DD \u05DC\u05E4\u05E8\u05E4\u05E8 \u05D0\u05D5 \u05E2\u05E9."
            ),
            "I am a little worm that eats leaves and will one day become a butterfly. Who am I?",
            "\u05D0\u05E0\u05D9 \u05EA\u05D5\u05DC\u05E2\u05EA \u05E7\u05D8\u05E0\u05D4 \u05E9\u05D0\u05D5\u05DB\u05DC\u05EA \u05E2\u05DC\u05D9\u05DD \u05D5\u05D9\u05D5\u05DD \u05D0\u05D7\u05D3 \u05D0\u05D4\u05E4\u05D5\u05DA \u05DC\u05E4\u05E8\u05E4\u05E8. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Leaves and plants", "\u05E2\u05DC\u05D9\u05DD \u05D5\u05E6\u05DE\u05D7\u05D9\u05DD",
            "Gardens, forests, and fields", "\u05D2\u05D9\u05E0\u05D5\u05EA, \u05D9\u05E2\u05E8\u05D5\u05EA \u05D5\u05E9\u05D3\u05D5\u05EA",
            "Very small \u2013 about 2\u201310 cm long"
        )
    )

    // ----------------------------------------------------------------
    // Quiz questions – Pets & Insects (IDs 37–50)
    // ----------------------------------------------------------------
    fun petInsectQuizSql(): List<String> = listOf(
        // 37. Hamster
        quizInsert(37, "RIDDLE",
            "I have puffy cheeks and run on a wheel at night. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05DC\u05D7\u05D9\u05D9\u05DD \u05E0\u05E4\u05D5\u05D7\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05E8\u05E5 \u05E2\u05DC \u05D2\u05DC\u05D2\u05DC \u05D1\u05DC\u05D9\u05DC\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Hamster", "\u05D0\u05D5\u05D2\u05E8",
            listOf("Rabbit", "Guinea Pig", "Budgie"), listOf("\u05D0\u05E8\u05E0\u05D1", "\u05E9\u05E8\u05E7\u05DF", "\u05EA\u05D5\u05DB\u05D5\u05DF")),
        // 38. Rabbit
        quizInsert(38, "RIDDLE",
            "I have long ears, a fluffy tail, and I love carrots. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05D0\u05D5\u05D6\u05E0\u05D9\u05D9\u05DD \u05D0\u05E8\u05D5\u05DB\u05D5\u05EA, \u05D6\u05E0\u05D1 \u05E4\u05E8\u05D5\u05D5\u05EA\u05D9 \u05D5\u05D0\u05E0\u05D9 \u05D0\u05D5\u05D4\u05D1 \u05D2\u05D6\u05E8\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Rabbit", "\u05D0\u05E8\u05E0\u05D1",
            listOf("Hamster", "Goldfish", "Guinea Pig"), listOf("\u05D0\u05D5\u05D2\u05E8", "\u05D3\u05D2 \u05D6\u05D4\u05D1", "\u05E9\u05E8\u05E7\u05DF")),
        // 39. Goldfish
        quizInsert(39, "RIDDLE",
            "I am small, orange, and swim in a bowl. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D8\u05DF, \u05DB\u05EA\u05D5\u05DD \u05D5\u05E9\u05D5\u05D7\u05D4 \u05D1\u05E7\u05E2\u05E8\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Goldfish", "\u05D3\u05D2 \u05D6\u05D4\u05D1",
            listOf("Turtle", "Budgie", "Hamster"), listOf("\u05E6\u05D1", "\u05EA\u05D5\u05DB\u05D5\u05DF", "\u05D0\u05D5\u05D2\u05E8")),
        // 40. Pet Turtle
        quizInsert(40, "RIDDLE",
            "I carry my house on my back and walk very slowly. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E0\u05D5\u05E9\u05D0 \u05D0\u05EA \u05D4\u05D1\u05D9\u05EA \u05E9\u05DC\u05D9 \u05E2\u05DC \u05D4\u05D2\u05D1 \u05D5\u05D4\u05D5\u05DC\u05DA \u05DC\u05D0\u05D8 \u05DE\u05D0\u05D5\u05D3. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Pet Turtle", "\u05E6\u05D1",
            listOf("Goldfish", "Rabbit", "Guinea Pig"), listOf("\u05D3\u05D2 \u05D6\u05D4\u05D1", "\u05D0\u05E8\u05E0\u05D1", "\u05E9\u05E8\u05E7\u05DF")),
        // 41. Guinea Pig
        quizInsert(41, "RIDDLE",
            "I am small and furry and I squeak when I am happy. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D8\u05DF \u05D5\u05E4\u05E8\u05D5\u05D5\u05EA\u05D9 \u05D5\u05DE\u05E6\u05E4\u05E6\u05E3 \u05DB\u05E9\u05D0\u05E0\u05D9 \u05E9\u05DE\u05D7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Guinea Pig", "\u05E9\u05E8\u05E7\u05DF",
            listOf("Hamster", "Rabbit", "Budgie"), listOf("\u05D0\u05D5\u05D2\u05E8", "\u05D0\u05E8\u05E0\u05D1", "\u05EA\u05D5\u05DB\u05D5\u05DF")),
        // 42. Budgie
        quizInsert(42, "RIDDLE",
            "I am a small colorful bird that can talk like a person. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E6\u05D9\u05E4\u05D5\u05E8 \u05E7\u05D8\u05DF \u05D5\u05E6\u05D1\u05E2\u05D5\u05E0\u05D9 \u05E9\u05D9\u05DB\u05D5\u05DC \u05DC\u05D3\u05D1\u05E8 \u05DB\u05DE\u05D5 \u05D1\u05DF \u05D0\u05D3\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Budgie", "\u05EA\u05D5\u05DB\u05D5\u05DF",
            listOf("Rabbit", "Goldfish", "Hamster"), listOf("\u05D0\u05E8\u05E0\u05D1", "\u05D3\u05D2 \u05D6\u05D4\u05D1", "\u05D0\u05D5\u05D2\u05E8")),
        // 43. Butterfly
        quizInsert(43, "RIDDLE",
            "I start as a caterpillar and then grow beautiful wings. Who am I?",
            "\u05D0\u05E0\u05D9 \u05DE\u05EA\u05D7\u05D9\u05DC \u05DB\u05D6\u05D7\u05DC \u05D5\u05D0\u05D7\u05E8 \u05DB\u05DA \u05DE\u05E6\u05DE\u05D9\u05D7 \u05DB\u05E0\u05E4\u05D9\u05D9\u05DD \u05D9\u05E4\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Butterfly", "\u05E4\u05E8\u05E4\u05E8",
            listOf("Bee", "Dragonfly", "Ladybug"), listOf("\u05D3\u05D1\u05D5\u05E8\u05D4", "\u05E9\u05E4\u05D9\u05E8\u05D9\u05EA", "\u05D7\u05D9\u05E4\u05D5\u05E9\u05D9\u05EA")),
        // 44. Bee
        quizInsert(44, "RIDDLE",
            "I buzz around flowers and make sweet honey. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D6\u05DE\u05D6\u05DE\u05EA \u05E1\u05D1\u05D9\u05D1 \u05E4\u05E8\u05D7\u05D9\u05DD \u05D5\u05DE\u05DB\u05D9\u05E0\u05D4 \u05D3\u05D1\u05E9 \u05DE\u05EA\u05D5\u05E7. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Bee", "\u05D3\u05D1\u05D5\u05E8\u05D4",
            listOf("Butterfly", "Ant", "Firefly"), listOf("\u05E4\u05E8\u05E4\u05E8", "\u05E0\u05DE\u05DC\u05D4", "\u05D2\u05D7\u05DC\u05D9\u05DC\u05D9\u05EA")),
        // 45. Ladybug
        quizInsert(45, "RIDDLE",
            "I am a tiny red bug with black spots. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D7\u05E8\u05E7 \u05E7\u05D8\u05DF \u05D0\u05D3\u05D5\u05DD \u05E2\u05DD \u05E0\u05E7\u05D5\u05D3\u05D5\u05EA \u05E9\u05D7\u05D5\u05E8\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Ladybug", "\u05D7\u05D9\u05E4\u05D5\u05E9\u05D9\u05EA",
            listOf("Ant", "Bee", "Caterpillar"), listOf("\u05E0\u05DE\u05DC\u05D4", "\u05D3\u05D1\u05D5\u05E8\u05D4", "\u05D6\u05D7\u05DC")),
        // 46. Ant
        quizInsert(46, "RIDDLE",
            "I am very tiny but very strong and I live in a big colony. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D8\u05E0\u05D8\u05E0\u05D4 \u05D0\u05D1\u05DC \u05D7\u05D6\u05E7\u05D4 \u05DE\u05D0\u05D5\u05D3 \u05D5\u05D7\u05D9\u05D4 \u05D1\u05DE\u05D5\u05E9\u05D1\u05D4 \u05D2\u05D3\u05D5\u05DC\u05D4. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Ant", "\u05E0\u05DE\u05DC\u05D4",
            listOf("Bee", "Ladybug", "Grasshopper"), listOf("\u05D3\u05D1\u05D5\u05E8\u05D4", "\u05D7\u05D9\u05E4\u05D5\u05E9\u05D9\u05EA", "\u05D7\u05D2\u05D1")),
        // 47. Dragonfly
        quizInsert(47, "RIDDLE",
            "I have four see-through wings and zoom over ponds. Who am I?",
            "\u05D9\u05E9 \u05DC\u05D9 \u05D0\u05E8\u05D1\u05E2 \u05DB\u05E0\u05E4\u05D9\u05D9\u05DD \u05E9\u05E7\u05D5\u05E4\u05D5\u05EA \u05D5\u05D0\u05E0\u05D9 \u05D3\u05D5\u05D4\u05E8\u05EA \u05DE\u05E2\u05DC \u05D1\u05E8\u05D9\u05DB\u05D5\u05EA. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Dragonfly", "\u05E9\u05E4\u05D9\u05E8\u05D9\u05EA",
            listOf("Butterfly", "Firefly", "Bee"), listOf("\u05E4\u05E8\u05E4\u05E8", "\u05D2\u05D7\u05DC\u05D9\u05DC\u05D9\u05EA", "\u05D3\u05D1\u05D5\u05E8\u05D4")),
        // 48. Grasshopper
        quizInsert(48, "RIDDLE",
            "I jump really high in the grass and chirp loudly. Who am I?",
            "\u05D0\u05E0\u05D9 \u05E7\u05D5\u05E4\u05E5 \u05D2\u05D1\u05D5\u05D4 \u05D1\u05EA\u05D5\u05DA \u05D4\u05E2\u05E9\u05D1 \u05D5\u05DE\u05E6\u05E8\u05E6\u05E8 \u05D1\u05E7\u05D5\u05DC. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Grasshopper", "\u05D7\u05D2\u05D1",
            listOf("Ant", "Caterpillar", "Dragonfly"), listOf("\u05E0\u05DE\u05DC\u05D4", "\u05D6\u05D7\u05DC", "\u05E9\u05E4\u05D9\u05E8\u05D9\u05EA")),
        // 49. Firefly
        quizInsert(49, "RIDDLE",
            "I glow in the dark on warm summer nights. Who am I?",
            "\u05D0\u05E0\u05D9 \u05D6\u05D5\u05D4\u05E8\u05EA \u05D1\u05D7\u05D5\u05E9\u05DA \u05D1\u05DC\u05D9\u05DC\u05D5\u05EA \u05E7\u05D9\u05E5 \u05D7\u05DE\u05D9\u05DD. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Firefly", "\u05D2\u05D7\u05DC\u05D9\u05DC\u05D9\u05EA",
            listOf("Ladybug", "Dragonfly", "Butterfly"), listOf("\u05D7\u05D9\u05E4\u05D5\u05E9\u05D9\u05EA", "\u05E9\u05E4\u05D9\u05E8\u05D9\u05EA", "\u05E4\u05E8\u05E4\u05E8")),
        // 50. Caterpillar
        quizInsert(50, "RIDDLE",
            "I am a little worm that eats leaves and will one day become a butterfly. Who am I?",
            "\u05D0\u05E0\u05D9 \u05EA\u05D5\u05DC\u05E2\u05EA \u05E7\u05D8\u05E0\u05D4 \u05E9\u05D0\u05D5\u05DB\u05DC\u05EA \u05E2\u05DC\u05D9\u05DD \u05D5\u05D9\u05D5\u05DD \u05D0\u05D7\u05D3 \u05D0\u05D4\u05E4\u05D5\u05DA \u05DC\u05E4\u05E8\u05E4\u05E8. \u05DE\u05D9 \u05D0\u05E0\u05D9?",
            "Caterpillar", "\u05D6\u05D7\u05DC",
            listOf("Grasshopper", "Ant", "Bee"), listOf("\u05D7\u05D2\u05D1", "\u05E0\u05DE\u05DC\u05D4", "\u05D3\u05D1\u05D5\u05E8\u05D4"))
    )

    // ----------------------------------------------------------------
    // Convenience aggregators
    // ----------------------------------------------------------------
    fun allNewAnimalSql(): List<String> = farmAnimalSql() + jungleAnimalSql() + oceanAnimalSql() + birdAnimalSql() + petAnimalSql() + insectAnimalSql()

    fun allNewQuizSql(): List<String> = farmJungleQuizSql() + oceanBirdQuizSql() + petInsectQuizSql()
}
