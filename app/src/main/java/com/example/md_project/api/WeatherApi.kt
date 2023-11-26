package com.example.md_project.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {
    @Headers("accept: application/json") //Tells the api that json should be used instead of xml
    @GET("/v1/current.json") //Endpoint of the API request
    suspend fun getWeather(
        @Query("key") key: String, //API key
        @Query("q") cityName: String, //Name of the city. Other parameters like latitude and longitude can also be parsed
        @Query("aqi") aqi: String //Get air quality data yes or no
    ): Response<Weather>
}