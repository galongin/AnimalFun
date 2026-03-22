package com.animalfun.ui.screens.quiz

import com.animalfun.R

/**
 * The six quiz types available in AnimalFun.
 *
 * @param displayNameResId String resource ID for the user-visible quiz type name.
 * @param iconResId Drawable resource ID for the quiz type icon.
 */
enum class QuizType(
    val displayNameResId: Int,
    val iconResId: Int
) {
    /** Show animal image, pick the correct name. */
    PICTURE(
        displayNameResId = R.string.quiz_type_picture,
        iconResId = R.drawable.ic_quiz_picture
    ),

    /** Play animal sound, pick the correct animal. */
    SOUND(
        displayNameResId = R.string.quiz_type_sound,
        iconResId = R.drawable.ic_quiz_sound
    ),

    /** Show riddle text, pick the correct animal. */
    RIDDLE(
        displayNameResId = R.string.quiz_type_riddle,
        iconResId = R.drawable.ic_quiz_riddle
    ),

    /** "What does X eat?" — pick the correct food. */
    DIET(
        displayNameResId = R.string.quiz_type_diet,
        iconResId = R.drawable.ic_quiz_diet
    ),

    /** "Where does X live?" — pick the correct place. */
    HABITAT(
        displayNameResId = R.string.quiz_type_habitat,
        iconResId = R.drawable.ic_quiz_habitat
    ),

    /** Show question text, pick the correct animal image. */
    REVERSE(
        displayNameResId = R.string.quiz_type_reverse,
        iconResId = R.drawable.ic_quiz_reverse
    )
}
