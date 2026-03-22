package com.animalfun.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val titleResId: Int? = null,
    val icon: ImageVector? = null
) {
    data object Home : Screen(
        route = "home",
        titleResId = com.animalfun.R.string.nav_home,
        icon = Icons.Filled.Home
    )

    data object Explore : Screen(
        route = "explore",
        titleResId = com.animalfun.R.string.nav_explore,
        icon = Icons.Filled.Explore
    )

    data object Quiz : Screen(
        route = "quiz",
        titleResId = com.animalfun.R.string.nav_quiz,
        icon = Icons.Filled.Quiz
    )

    data object Sounds : Screen(
        route = "sounds",
        titleResId = com.animalfun.R.string.nav_sounds,
        icon = Icons.Filled.MusicNote
    )

    data object Memory : Screen(
        route = "memory",
        titleResId = com.animalfun.R.string.nav_memory,
        icon = Icons.Filled.Psychology
    )

    data object Settings : Screen(
        route = "settings",
        titleResId = com.animalfun.R.string.nav_settings,
        icon = Icons.Filled.Settings
    )

    data object AnimalDetail : Screen(
        route = "animal_detail/{animalId}"
    ) {
        fun createRoute(animalId: Int): String = "animal_detail/$animalId"
    }

    companion object {
        val bottomNavItems = listOf(Home, Explore, Quiz, Sounds, Memory)
    }
}
