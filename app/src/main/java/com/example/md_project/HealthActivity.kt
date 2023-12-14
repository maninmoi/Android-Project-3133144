package com.example.md_project

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.md_project.api.WeatherViewModel
import com.example.md_project.ui.theme.BlueCustom
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.math.roundToInt


class HealthActivity : ComponentActivity() {
    private lateinit var weatherViewModel: WeatherViewModel
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            CheckTemperatureInIntervals::class.java,
            15, TimeUnit.MINUTES //Interval
        ).build()

        // Enqueue the work request
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
        setContent {
            val appSettings = dataStore.data.collectAsState(initial = AppSettings()).value
            val scope = rememberCoroutineScope()

            val appSettingsCold = appSettings.hotTemperatureValue
            val appSettingsHot = appSettings.hotTemperatureValue


            var selectedValueCold by remember { mutableFloatStateOf(appSettingsCold) } //The currently selected value (cold)
            var selectedValueHot by remember { mutableFloatStateOf(appSettingsHot) } //The currently selected value (hot)

            val predefinedValues = generateTemperatures(-20.0,40.0,0.5) //Calls generateTemperatures to create points in the slider

            LaunchedEffect(appSettings.coldTemperatureValue) {
                selectedValueCold = appSettings.coldTemperatureValue
            }

            LaunchedEffect(appSettings.hotTemperatureValue) {
                selectedValueHot = appSettings.hotTemperatureValue
            }
            var suffix = getTemperatureSuffix(appSettings.temperatureUnit)
            Scaffold(
                bottomBar = { BottomNavBar() }
            ) {
                Surface(
                    modifier = Modifier.padding().fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(40.dp),
                    ) {
                        Box() {
                            Column {
                                var coldTemperatureText = convertTemperature(selectedValueCold.round(1), appSettings.temperatureUnit)
                                Text(text = "Cold Temperature: $coldTemperatureText $suffix")
                                Slider(
                                    value = selectedValueCold,
                                    onValueChange = { newValue ->
                                        selectedValueCold = newValue
                                        scope.launch {
                                            dataStore.updateData { currentSettings ->
                                                currentSettings.copy(coldTemperatureValue = newValue)
                                            }
                                        }
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
                                Switch(
                                    label = "Cold Temperature Alert",
                                    isChecked = appSettings.coldTemperatureAlertSwitch,
                                    onCheckedChange = {
                                        scope.launch {
                                            dataStore.updateData { currentSettings ->
                                                currentSettings.copy(coldTemperatureAlertSwitch = it)
                                            }
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(50.dp))
                        Box(){
                            Column {
                                var hotTemperatureText = convertTemperature(selectedValueHot.round(1), appSettings.temperatureUnit)
                                Text(text = "Hot Temperature: $hotTemperatureText $suffix")
                                Slider(
                                    value = selectedValueHot,
                                    onValueChange = { newValue ->
                                        selectedValueHot = newValue
                                        scope.launch {
                                            dataStore.updateData { currentSettings ->
                                                currentSettings.copy(hotTemperatureValue = newValue)
                                            }
                                        }
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
                                //DoubleTextField()
                                Switch(
                                    label = "Hot Temperature Alert",
                                    isChecked = appSettings.hotTemperatureAlertSwitch,
                                    onCheckedChange = {
                                        scope.launch {
                                            dataStore.updateData { currentSettings ->
                                                currentSettings.copy(hotTemperatureAlertSwitch = it)
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


fun Float.round(decimalPlaces: Int): Float {
    val multiplier = 10.0.pow(decimalPlaces.toDouble()).toFloat()
    return (this * multiplier).roundToInt() / multiplier
}