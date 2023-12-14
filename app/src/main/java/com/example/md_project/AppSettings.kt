package com.example.md_project

import kotlinx.serialization.Serializable


@Serializable
data class AppSettings( //Holds settings
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val humiditySwitch: Boolean = true,
    val reminderToDrinkSwitch: Boolean = true,
    val coldTemperatureAlertSwitch: Boolean = true,
    val hotTemperatureAlertSwitch: Boolean = true,
    val coldTemperatureValue: Float = 20f,
    val hotTemperatureValue: Float = 20f

)


enum class TemperatureUnit{ //Enum of units
    CELSIUS, FAHRENHEIT, KELVIN
}




