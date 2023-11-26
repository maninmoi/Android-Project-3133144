package com.example.md_project.api

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class WeatherApiRepository {
    private val weatherApi: WeatherApi = RetrofitInstance.instance

    suspend fun getWeatherData(city: String, key: String, aqi: String): Response<Weather> {
        return try {
            weatherApi.getWeather(key, city, aqi)
        } catch (e: Exception) {
            Response.error(500, "".toResponseBody(null))
        }
    }
}