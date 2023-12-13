package com.example.md_project

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings( //Holds settings
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val temperatureRecords: PersistentList<TemperatureRecords> = persistentListOf(),
    val humiditySwitch: Boolean = true,
    val reminderToDrinkSwitch: Boolean = true,
    val coldTemperatureAlertSwitch: Boolean = true,
    val hotTemperatureAlertSwitch: Boolean = true,
    val coldTemperatureValue: Double = 20.0,
    val hotTemperatureValue: Double = 20.0
)

@Serializable
data class TemperatureRecords( //Records for the last 7 days
    val day1: Double,
    val day2: Double,
    val day3: Double,
    val day4: Double,
    val day5: Double,
    val day6: Double,
    val day7: Double
)
enum class TemperatureUnit{ //Enum of units
    CELSIUS, FAHRENHEIT, KELVIN
}
