package com.example.md_project

import LocationService
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.ViewModelProvider
import com.example.md_project.api.WeatherViewModel
import com.example.md_project.ui.theme.BlueCustom
import java.text.DecimalFormat

class HomeActivity : ComponentActivity() {
    private lateinit var locationService: LocationService
    private lateinit var locationViewModel: LocationViewModel
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java] //Location viewmodel gets created
        locationService = LocationService(this, locationViewModel) //Location service gets created

        locationService.startLocationUpdates() //Starts the location updates

        setContent {
            Scaffold(
                bottomBar = { BottomNavBar() }
            ) {
                HomeScreen(locationViewModel = locationViewModel) //Gives the homescreen composable the locationViewModel
            }
        }
    }
}

@Composable
fun HomeScreen(locationViewModel: LocationViewModel) {
    val locationUpdateStatus by locationViewModel.locationUpdateStatus.observeAsState() //Observes if a change in the locationviewmodel happened
    val context = LocalContext.current //Getting current context
    var weatherViewModel = WeatherViewModel() //Creating viewmodel

    //Weather API
    var temperature by remember { mutableFloatStateOf(0f) } ////MutableFloatStateOf to hold the temperature value
    var humidity by remember { mutableFloatStateOf(0f) } //MutableFloatStateOf to hold the humidity value

    if (locationUpdateStatus == true) {
        //Coroutine for API call
        LaunchedEffect(locationUpdateStatus) {
            weatherViewModel.fetchWeatherData("${locationViewModel.latitude.value},${locationViewModel.longitude.value}", "95c2e4348dcf444baeb194726231211" ,  "no")
            temperature = weatherViewModel.temperature.value //Temperature value is saved into mutable variable
            humidity = weatherViewModel.humidity.value //Humidity value is saved into mutable variable

            locationViewModel.updateLocationStatus(false) //Locationupdatestatus is set to false
        }
    }


    //Temperature sensor
    var actualtemperature by remember { mutableFloatStateOf(0f) } //MutableFloatStateOf to hold the actualtemperature value

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
    var actualthumidity by remember { mutableFloatStateOf(0f) } //MutableFloatStateOf to hold the actualhumidity value

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
        modifier = Modifier
            .padding()
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DrawBackground(temperature)
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
                value = temperature,
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
                value = humidity,
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
        temperature: Float
    ) { //Draws the background. Takes the current temperature from the api to determine the background
        var imageid = 0
        var contentdescription = ""
        if(temperature > 19){ //Draws a sunny background if the temperature is above 19 degrees
            imageid = R.drawable.sun_background
            contentdescription = "sunny background"
        }else if(temperature > 5){ //Draws a cloudy background if the temperature is above 5 degrees but below 20
            imageid = R.drawable.clouds_background
            contentdescription = "cloudy background"
        }else{ //Draws a freezing background if the temperature is 5 degrees or below
            imageid = R.drawable.freezing_background
            contentdescription = "freezing background"
        }
        Box() {
            Image(
                painter = painterResource(id = imageid),
                contentDescription = contentdescription,
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
