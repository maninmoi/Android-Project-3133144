package com.example.md_project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {
    private val _latitude = MutableLiveData<Double>()
    private val _longitude = MutableLiveData<Double>()
    private val _locationUpdateStatus = MutableLiveData<Boolean>()

    val locationUpdateStatus: LiveData<Boolean>
        get() = _locationUpdateStatus

    val latitude: LiveData<Double>
        get() = _latitude

    val longitude: LiveData<Double>
        get() = _longitude

    fun updateLocation(latitude: Double, longitude: Double) {
        _latitude.value = latitude //lat value is set
        _longitude.value = longitude //long value is set
        _locationUpdateStatus.postValue(true) //Locationupdatestatus is set to true. API could now make a call
    }

    fun updateLocationStatus(status: Boolean) {
        _locationUpdateStatus.postValue(status) //Updates the locationupdatestatus
    }
}


