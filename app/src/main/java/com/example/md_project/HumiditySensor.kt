package com.example.md_project

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class HumiditySensorManager(private val context: Context, private val onSensorChangedCallback: (Float) -> Unit) :
    SensorEventListener {
    var humidityval = 60.5f //Test value, only really relevant for emulators
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager //Declaring SensorManager
    private var humidity: Sensor? = null //Declaring humidity Sensor



    init {
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) //Setting the type of sensor
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        //Does nothing so far
    }

    override fun onSensorChanged(event: SensorEvent) {
        val temperature = event.values[0]
        humidityval = temperature //Saves the humidity value into humidityval when the event is triggered
        onSensorChangedCallback(humidityval) //Provides the data to another class
    }

    fun onResume() {
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL) //Registers the listener onResume
    }

    fun onPause() {
        sensorManager.unregisterListener(this) //Unregisters the listener onPause
    }
}