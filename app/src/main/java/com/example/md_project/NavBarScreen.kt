package com.example.md_project

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.md_project.ui.theme.BlueCustom
import com.example.md_project.ui.theme.BlueCustom2

sealed class BottomBarItem( //Holds info of the navbar objects
    val activityClass: Class<*>, //List of Activites
    val navLabel: String, //Label
    val navIcon: ImageVector //Icon
) {
    object Home : BottomBarItem( //Home object
        HomeActivity::class.java, //Activity
        "Home", //Label
        Icons.Rounded.Home //Icon
    )
    object Health : BottomBarItem( //Health object
        HealthActivity::class.java, //Activity
        "Health", //Label
        Icons.Rounded.HealthAndSafety //Icon
    )
    object Settings : BottomBarItem( //Settings object
        SettingsActivity::class.java, //Activity
        "Settings", //Label
        Icons.Rounded.Settings //Icon
    )
}


@Composable
fun BottomNavBar() {
    val context = LocalContext.current //Saves the current context into a variable
    val screenList = listOf(
        BottomBarItem.Home,
        BottomBarItem.Health,
        BottomBarItem.Settings,
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = BlueCustom2.copy(alpha = 0.3f), //Color for the container. Is Opaque
    ) {
        screenList.forEach { screen -> //Adds the screens to the screenlist
            AddScreen(
                screen = screen,
                context = context
            )
        }
    }
}

@Composable
fun RowScope.AddScreen(
    screen: BottomBarItem,
    context: Context
) {
    val isSelected = when (context) {
        is Activity -> context::class.java == screen.activityClass
        else -> false
    }

    NavigationBarItem(
        colors = androidx.compose.material3.NavigationBarItemDefaults
            .colors(
                selectedIconColor = BlueCustom,
                indicatorColor = BlueCustom,
            ),
        icon = {
            Icon(
                imageVector = screen.navIcon,
                contentDescription = screen.navLabel,
                tint = colorResource(id = R.color.black)
            )
        },
        label = {
            Text(
                text = screen.navLabel,
                color = colorResource(id = R.color.black)
            )
        },
        selected = isSelected,
        onClick = {
            if (!isSelected) {
                val intent = Intent(context, screen.activityClass)
                context.startActivity(intent)
            }
        }
    )
}
