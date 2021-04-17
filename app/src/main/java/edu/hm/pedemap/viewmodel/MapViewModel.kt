package edu.hm.pedemap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.hm.pedemap.repository.LocationRepository

class MapViewModel(application: Application) : AndroidViewModel(application) {

    val locationRepository = LocationRepository(application)

    val lastLocation get() = locationRepository.lastLocation
}
