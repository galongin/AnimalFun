package com.animalfun.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.animalfun.ui.screens.detail.AnimalDetailScreen
import com.animalfun.ui.screens.explore.ExploreScreen
import com.animalfun.ui.screens.home.HomeScreen
import com.animalfun.ui.screens.memory.MemoryScreen
import com.animalfun.ui.screens.quiz.QuizScreen
import com.animalfun.ui.screens.settings.SettingsScreen
import com.animalfun.ui.screens.sounds.SoundsScreen

@Composable
fun AnimalFunNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    windowWidthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier.padding(innerPadding)
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                windowWidthSizeClass = windowWidthSizeClass,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Explore.route) {
            ExploreScreen(
                windowWidthSizeClass = windowWidthSizeClass,
                onAnimalClick = { animalId ->
                    navController.navigate(Screen.AnimalDetail.createRoute(animalId))
                }
            )
        }

        composable(Screen.Quiz.route) {
            QuizScreen(
                onNavigateHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Sounds.route) {
            SoundsScreen(
                windowWidthSizeClass = windowWidthSizeClass
            )
        }

        composable(Screen.Memory.route) {
            MemoryScreen(
                windowWidthSizeClass = windowWidthSizeClass,
                onNavigateHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.AnimalDetail.route,
            arguments = listOf(
                navArgument("animalId") { type = NavType.IntType }
            )
        ) {
            AnimalDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
