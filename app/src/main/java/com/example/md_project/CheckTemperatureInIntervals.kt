package com.example.md_project

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.md_project.api.WeatherViewModel

class CheckTemperatureInIntervals(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val weatherViewModel = WeatherViewModel.getInstance(applicationContext)
        val currentTemperature = weatherViewModel.temperature.value

        return Result.success()
    }
}