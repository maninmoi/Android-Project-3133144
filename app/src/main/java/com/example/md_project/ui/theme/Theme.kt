package com.example.md_project.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = BlueCustom, // Customize the primary color

    /*background = BlueCustom, // Customize the background color
    surface = BlueCustom, // Customize the surface color
    onPrimary = Color.Black, // Customize the text color on the primary background
    onSecondary = Color.Black, // Customize the text color on the secondary background
    onBackground = Color.Black, // Customize the text color on the background
    onSurface = Color.Black // Customize the text color on the surface*/

)

@Composable
fun NavigationBarTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}