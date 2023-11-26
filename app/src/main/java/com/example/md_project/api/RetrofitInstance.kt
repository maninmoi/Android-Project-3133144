package com.example.md_project.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private const val BASE_URL = "http://api.weatherapi.com/v1/" //Base url of the api service

    val instance: WeatherApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(WeatherApi::class.java)
    }
}

