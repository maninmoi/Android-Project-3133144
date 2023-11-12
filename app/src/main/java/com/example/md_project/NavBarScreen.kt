package com.example.md_project

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.md_project.ui.theme.BlueCustom

sealed class NavBar(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home: NavBar(
        route="home",
        label="Home",
        icon= Icons.Rounded.Home
    )
    object Health: NavBar(
        route="health",
        label="Health",
        icon= Icons.Rounded.HealthAndSafety
    )
    object Settings: NavBar(
        route="settings",
        label="Settings",
        icon= Icons.Rounded.Settings
    )
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingModifier: Modifier
) {
    NavHost(navController = navController,
        startDestination = NavBar.Home.route
    ) {
        composable(route= NavBar.Home.route) {
            HomeScreen(paddingModifier)
        }
        composable(route= NavBar.Health.route) {
            HealthScreen(paddingModifier)
        }
        composable(route= NavBar.Settings.route) {
            SettingsScreen(paddingModifier)
        }
    }
}

@Composable
fun AppNavBar(navController: NavHostController) {
    val screens = listOf(
        NavBar.Home,
        NavBar.Health,
        NavBar.Settings
    )
    NavigationBar(
        containerColor = BlueCustom, //Navbar color
        )
     {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: NavBar,
    navController: NavHostController
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBarItem(
        label = {
            Text(text = screen.label)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.route + " icon"
            )
        },
        selected = screen.route == backStackEntry.value?.destination?.route,
        onClick = {
            navController.navigate(screen.route)
        }
    )
}