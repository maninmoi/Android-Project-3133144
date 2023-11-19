package com.example.md_project

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class TemperatureSensorManager(private val context: Context, private val onSensorChangedCallback: (Float) -> Unit) : SensorEventListener {
    var temperatureval = 23.5f //Test value, only really relevant for emulators
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager //Declaring SensorManager
    private var temperature: Sensor? = null //Declaring temperature Sensor



    init {
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) //Setting the type of sensor
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        //Does nothing so far
    }

    override fun onSensorChanged(event: SensorEvent) {
        temperatureval = event.values[0] //Saves the temperature value into temperatureval when the event is triggered
        onSensorChangedCallback(temperatureval) //Provides the data to another class
    }

    fun onResume() {
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL) //Registers the listener onResume
    }

    fun onPause() {
        sensorManager.unregisterListener(this) //Unregisters the listener onPause
    }
}