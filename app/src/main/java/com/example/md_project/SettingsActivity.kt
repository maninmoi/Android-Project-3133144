package com.example.md_project

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import com.example.md_project.ui.theme.BlueCustom
import kotlinx.coroutines.launch


class SettingsActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appSettings = dataStore.data.collectAsState(initial = AppSettings()).value
            val scope = rememberCoroutineScope()
            Scaffold(
                bottomBar = { BottomNavBar() }
            ) {
                Surface(
                    modifier = Modifier
                        .padding()
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(40.dp)) {
                        Box(){
                            Column {
                                DisplayText(text = "Unit selection", 16.sp)
                                ExposedDropdownMenuBox(dataStore = dataStore)

                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(){
                            Column {
                                Switch(
                                    label = "Humidity",
                                    isChecked = appSettings.humiditySwitch,
                                    onCheckedChange = {
                                        scope.launch {
                                            dataStore.updateData { currentSettings ->
                                                currentSettings.copy(humiditySwitch = it)
                                            }
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Box(){
                            Column {
                                Switch(
                                    label = "Reminder to Drink",
                                    isChecked = appSettings.reminderToDrinkSwitch,
                                    onCheckedChange = {
                                        scope.launch {
                                            dataStore.updateData { currentSettings ->
                                                currentSettings.copy(reminderToDrinkSwitch = it)
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//Displays a dropdown menu with multiple selectable values
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBox(dataStore: DataStore<AppSettings>) {
    val temperatureUnits = arrayOf("Celsius", "Fahrenheit", "Kelvin")

    var expanded by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    //Retrieves the current settings from DataStore
    val currentSettings by dataStore.data.collectAsState(initial = AppSettings())

    //Selects the unit that is saved in AppSettings
    selectedText = when (currentSettings.temperatureUnit) {
        TemperatureUnit.CELSIUS -> "Celsius"
        TemperatureUnit.FAHRENHEIT -> "Fahrenheit"
        TemperatureUnit.KELVIN -> "Kelvin"
    }

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

                            //Maps the text to the enum in AppSettings
                            val temperatureUnit = when (item) {
                                "Celsius" -> TemperatureUnit.CELSIUS
                                "Fahrenheit" -> TemperatureUnit.FAHRENHEIT
                                "Kelvin" -> TemperatureUnit.KELVIN
                                else -> TemperatureUnit.CELSIUS
                            }

                            //Updates the temperatureUnit in AppSettings
                            coroutineScope.launch {
                                dataStore.updateData { currentSettings ->
                                    currentSettings.copy(temperatureUnit = temperatureUnit)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


//Displays a switch with custom colors
@Composable
fun Switch(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val switchColors = SwitchDefaults.colors(
        checkedThumbColor = BlueCustom,
        checkedTrackColor = BlueCustom.copy(alpha = 0.5f),
        uncheckedThumbColor = Color.Red,
        uncheckedTrackColor = Color.Red.copy(alpha = 0.5f),
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = switchColors,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}