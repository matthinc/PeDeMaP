package com.example.peopledensitymeasurementprototype.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.peopledensitymeasurementprototype.repository.LocationRepository

class MapViewModel(application: Application) : AndroidViewModel(application) {

    val locationRepository = LocationRepository(application)

    val lastLocation get() = locationRepository.lastLocation
}
