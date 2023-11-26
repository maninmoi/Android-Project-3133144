package com.example.md_project.api

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {
    private val weatherApiRepository = WeatherApiRepository()

    private val _temperature = mutableFloatStateOf(0f)
    var temperature: State<Float> = _temperature
    private val _humidity = mutableFloatStateOf(0f)
    val humidity: State<Float> = _humidity


    suspend fun fetchWeatherData(city: String, key: String, aqi: String) { //Fetches the weather data
        try {
            val response = weatherApiRepository.getWeatherData(city, key, aqi)
            if (response.isSuccessful) {
                val weatherData = response.body()
                _temperature.value = (weatherData?.current?.temp_c ?: 0f).toFloat() //Saves the temperature into the variable of the viewmodel
                _humidity.value = (weatherData?.current?.humidity ?: 0f).toFloat() //Saves the humidity into the variable of the viewmodel
            } else {
            }
        } catch (e: Exception) {
        }
    }
    companion object {
        private var instance: WeatherViewModel? = null

        fun getInstance(applicationContext: Context): WeatherViewModel {
            if (instance == null) {
                instance = WeatherViewModel()
            }
            return instance!!
        }
    }
}
