package com.example.md_project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.md_project.ui.theme.BlueCustom

@Composable
fun SettingsScreen(paddingModifier: Modifier) {
    Surface(
        modifier = paddingModifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(40.dp)) {
            Box(){
                Column {
                    DisplayText(text = "Unit selection", 16.sp)
                    ExposedDropdownMenuBox()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(){
                Column {
                    DisplayText(text = "Disable/Enable Humidity", fontSize = 16.sp)
                    Switch()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(){
                Column {
                    DisplayText(text = "Disable/Enable reminder to drink", fontSize = 16.sp)
                    Switch()
                }
            }
        }
    }
}

//Displays a dropdown menu with multiple selectable values
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBox() {
    val temperatureUnits = arrayOf("Celsius", "Fahrenheit", "Kelvin")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(temperatureUnits[0]) }
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                temperatureUnits.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

//Displays a switch with custom colors
@Composable
fun Switch() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = BlueCustom,
            checkedTrackColor = BlueCustom.copy(alpha = 0.5f),
            uncheckedThumbColor = Color.Red,
            uncheckedTrackColor = Color.Red.copy(alpha = 0.5f),
        )
    )
}
