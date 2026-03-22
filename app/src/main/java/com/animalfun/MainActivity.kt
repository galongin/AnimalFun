package com.animalfun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.animalfun.ui.navigation.AnimalFunNavGraph
import com.animalfun.ui.navigation.Screen
import com.animalfun.ui.theme.AnimalFunTheme
import com.animalfun.util.LocaleHelper
import com.animalfun.util.ProvideLayoutDirection

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val languageFlow = LocaleHelper.getLanguageFlow(this@MainActivity)
            val currentLanguage by languageFlow.collectAsState(initial = LocaleHelper.getCurrentLanguage())
            val windowSizeClass = calculateWindowSizeClass(this)
            val useNavigationRail = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

            ProvideLayoutDirection(language = currentLanguage) {
                AnimalFunTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AnimalFunMainScreen(
                            useNavigationRail = useNavigationRail,
                            windowWidthSizeClass = windowSizeClass.widthSizeClass
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimalFunMainScreen(
    useNavigationRail: Boolean,
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomNavItems = Screen.bottomNavItems

    // Determine if we should show navigation (hide on detail screens)
    val showNavigation = bottomNavItems.any { screen ->
        currentDestination?.hierarchy?.any { it.route == screen.route } == true
    } || currentDestination?.route == Screen.Settings.route
        || currentDestination == null

    if (useNavigationRail && showNavigation) {
        Row(modifier = modifier.fillMaxSize()) {
            NavigationRail {
                bottomNavItems.forEach { screen ->
                    NavigationRailItem(
                        icon = {
                            screen.icon?.let { Icon(imageVector = it, contentDescription = null) }
                        },
                        label = {
                            screen.titleResId?.let { Text(stringResource(it)) }
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
            Scaffold { innerPadding ->
                AnimalFunNavGraph(
                    navController = navController,
                    innerPadding = innerPadding,
                    windowWidthSizeClass = windowWidthSizeClass
                )
            }
        }
    } else {
        Scaffold(
            bottomBar = {
                if (showNavigation) {
                    NavigationBar {
                        bottomNavItems.forEach { screen ->
                            NavigationBarItem(
                                icon = {
                                    screen.icon?.let { Icon(imageVector = it, contentDescription = null) }
                                },
                                label = {
                                    screen.titleResId?.let { Text(stringResource(it)) }
                                },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            AnimalFunNavGraph(
                navController = navController,
                innerPadding = innerPadding,
                windowWidthSizeClass = windowWidthSizeClass
            )
        }
    }
}
