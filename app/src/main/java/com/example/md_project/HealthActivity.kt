package com.example.md_project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.md_project.ui.theme.BlueCustom
import java.text.DecimalFormat


@Composable
fun HealthScreen(paddingModifier: Modifier) {
    Surface(
        modifier = paddingModifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(40.dp),
        ) {
                Box() {
                    Column {
                        DisplayText(text = "Cold temperature alert", 16.sp)
                        TemperatureSlider()
                        DoubleTextField()
                        DisplayText(
                            text = "Disable/Enable cold temperature alert",
                            fontSize = 16.sp
                        )
                        Switch()
                    }
                }

            Spacer(modifier = Modifier.height(50.dp))
            Box(){
                Column {
                    DisplayText(text = "Hot temperature alert", 16.sp)
                    TemperatureSlider()
                    DoubleTextField()
                    DisplayText(text = "Disable/Enable hot temperature alert", fontSize = 16.sp)
                    Switch()
                }
            }
        }
    }
}

@Composable
fun TemperatureSlider() {
    var selectedValue by remember { mutableFloatStateOf(20f) } //The currently selected value

    val predefinedValues = generateTemperatures(-20.0,40.0,0.5) //Calls generateTemperatures to create points in the slider

    Column(

    ) {
        val decimalFormat = DecimalFormat("#.#")
        val formattedSelectedValue = decimalFormat.format(selectedValue)
        Text("${formattedSelectedValue}°C")
        Spacer(modifier = Modifier.height(10.dp))
        Slider(
            value = selectedValue,
            onValueChange = { newValue ->
                selectedValue = newValue
            },
            colors = SliderDefaults.colors(
                thumbColor = BlueCustom,
                activeTrackColor = BlueCustom.copy(alpha = 0.5f),
                inactiveTrackColor = Color.Gray.copy(alpha = 0.5f)
            ),
            valueRange = -20f..40f,
            steps = (predefinedValues.size - 1).toFloat().toInt(),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

//Takes a start temperature, end temperature and interval. This will generate temperatures for the slider. Returns a MutableList<Double>
fun generateTemperatures(start: Double, end: Double, interval: Double): MutableList<Double> {
    val temperatures = mutableListOf(start)
    var x = start+interval
    while(x < end){
        temperatures.add(x)
        x+=interval
    }
    return temperatures
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//Displays an editable text field to allow finer input of temperatures
fun DoubleTextField() {
    var text by remember { mutableStateOf("") }
    var doubleValue by remember { mutableDoubleStateOf(0.0) }

    TextField(
        value = text,
        onValueChange = {
            text = it

            doubleValue = try {
                it.toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }
        },
        label = { Text("Enter a temperature") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                //Needs to be implemented to give the slider the value, when it is changed
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}