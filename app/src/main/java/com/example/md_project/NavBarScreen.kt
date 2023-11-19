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

//
sealed class NavBar(
    val route: String, //Route for the navhost
    val label: String, //Label to display
    val icon: ImageVector //Icon to display
) {
    object Home: NavBar( //Object for the home button
        route="home",
        label="Home",
        icon= Icons.Rounded.Home
    )
    object Health: NavBar( //Object for the health button
        route="health",
        label="Health",
        icon= Icons.Rounded.HealthAndSafety
    )
    object Settings: NavBar( //Object for the settings button
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
    NavHost(navController = navController, //Configuring the navcontroller
        startDestination = NavBar.Home.route //First activity that is loaded
    ) {
        composable(route= NavBar.Home.route) {//Route to home screen
            HomeScreen(paddingModifier)
        }
        composable(route= NavBar.Health.route) {//Route to health screen
            HealthScreen(paddingModifier)
        }
        composable(route= NavBar.Settings.route) {//Route to settings screen
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
        screens.forEach { screen -> //Adds the screen and navcontoller to the screens list
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