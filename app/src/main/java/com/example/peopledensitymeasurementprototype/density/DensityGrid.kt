package com.example.peopledensitymeasurementprototype.density

import com.example.peopledensitymeasurementprototype.density.strategy.SimpleDensityCalculationStrategy
import java.util.*

class DensityGrid {

    private val locationList = LinkedList<UTMLocation>()
    var densityCalculationStrategy = SimpleDensityCalculationStrategy()

    var observer: (DensityGrid)->Unit = {}

    fun add(utmLocation: UTMLocation) {
        if (utmLocation.timestamp == null) {
            throw IllegalArgumentException("Timestamp is null!")
        }
        if (utmLocation.deviceId == null) {
            throw IllegalArgumentException("DeviceID is null!")
        }

        locationList.updateByDeviceId(utmLocation)
        observer(this)
    }

    fun getDensityAt(utmLocation: UTMLocation): Density {
        return densityCalculationStrategy.calculateDensityAt(locationList, utmLocation)
    }

    fun getNumberOfDevices(): Int {
        return locationList.size
    }

    private fun LinkedList<UTMLocation>.updateByDeviceId(location: UTMLocation) {
        this.removeIf { it.deviceId == location.deviceId }
        add(location)
    }

}
