package com.example.md_project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.md_project.ui.theme.BlueCustom
import java.text.DecimalFormat

@Composable
    fun HomeScreen(paddingModifier: Modifier) {
        //Temperature sensor
        val context = LocalContext.current //Gets the current context

        var actualtemperature by remember { mutableFloatStateOf(0f) } //MutableFloatStateOf to hold the temperature value

        val temperatureSensorManager = remember {
            TemperatureSensorManager(context) { newValue ->
                actualtemperature = newValue
            }
        }

        DisposableEffect(Unit) {
            temperatureSensorManager.onResume() //Triggers when the composable is built

            onDispose {
                temperatureSensorManager.onPause() //Triggers if another composable gets build
            }
        }

        LaunchedEffect(temperatureSensorManager.temperatureval) {
            actualtemperature = temperatureSensorManager.temperatureval
        }

        //Humidity Sensor
        var actualthumidity by remember { mutableFloatStateOf(0f) } //MutableFloatStateOf to hold the humidity value

        val humiditySensorManager = remember {
            HumiditySensorManager(context) { newValue ->
                actualthumidity = newValue
            }
        }

        DisposableEffect(Unit) {
            humiditySensorManager.onResume() //Triggers when the composable is built

            onDispose {
                humiditySensorManager.onPause() //Triggers if another composable gets build
            }
        }

        LaunchedEffect(humiditySensorManager.humidityval) {
            actualthumidity = humiditySensorManager.humidityval
        }

        Surface(
            modifier = paddingModifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DrawBackground(
                R.drawable.clouds_background,
                "cloud background"
            ) //The background will change according to the current weather in future versions
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                TextInBox(text = "Weather information hub")
                Spacer(modifier = Modifier.height(16.dp))
                //Temperature
                WeatherBox(
                    icon = Icons.Default.Thermostat,
                    label = "Temperature",
                    value = 25f, //Placeholder
                    suffix = "°C",
                    color = BlueCustom
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Actual temperature
                WeatherBox(
                    icon = Icons.Default.Thermostat,
                    label = "Actual Temperature",
                    value = actualtemperature,
                    suffix = "°C",
                    color = BlueCustom
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Predicted temperature
                WeatherBox(
                    icon = Icons.Default.Thermostat,
                    label = "Predicted Temperature(Tomorrow)",
                    value = 27.3f, //Placeholder,
                    suffix = "°C",
                    color = BlueCustom
                )


                Spacer(modifier = Modifier.height(16.dp))
                //Humidity Box
                WeatherBox(
                    icon = Icons.Default.WaterDrop,
                    label = "Humidity",
                    value = 60f, //Placeholder
                    suffix = "%",
                    color = BlueCustom
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Actual humidity
                WeatherBox(
                    icon = Icons.Default.WaterDrop,
                    label = "Actual Humidity",
                    value = actualthumidity,
                    suffix = "%",
                    color = BlueCustom
                )
        }
    }
}

    @Composable
    fun WeatherBox( //Takes 4 parameters and builds a box with content out of it
        icon: ImageVector,
        label: String,
        value: Float,
        suffix: String,
        color: Color
    ) {
        val decimalFormat = DecimalFormat("#.#")
        val formattedSelectedValue = decimalFormat.format(value)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = label,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = formattedSelectedValue + suffix,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }

    @Composable
    fun DisplayText(
        text: String,
        fontSize: TextUnit
    ) { //function to displaytext. text and fontsize must be given when called
        Column(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0f))
                .wrapContentHeight(Alignment.Top),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                fontSize = fontSize,
            )
        }
    }

    @Composable
    fun DrawBackground(
        image: Int,
        conentdescription: String
    ) { //Draws the background. Takes a R.drawable as a parameter
        Box() {
            Image(
                painter = painterResource(id = image),
                contentDescription = conentdescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

    @Composable
    fun TextInBox(text: String) { //Draws text in a box. Takes the text as a parameter
        Box(
            modifier = Modifier
                .background(BlueCustom)
                .fillMaxWidth()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
}