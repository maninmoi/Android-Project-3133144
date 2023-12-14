package com.example.md_project

import kotlinx.serialization.Serializable


@Serializable
data class AppSettings( //Holds settings
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val humiditySwitch: Boolean = true,
    val reminderToDrinkSwitch: Boolean = true,
    val coldTemperatureAlertSwitch: Boolean = true,
    val hotTemperatureAlertSwitch: Boolean = true,
    val coldTemperatureValue: Double = 20.0,
    val hotTemperatureValue: Double = 20.0

)


enum class TemperatureUnit{ //Enum of units
    CELSIUS, FAHRENHEIT, KELVIN
}




