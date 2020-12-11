package com.example.peopledensitymeasurementprototype.density

import com.example.peopledensitymeasurementprototype.density.strategy.DensityCalculationStrategy
import com.example.peopledensitymeasurementprototype.density.strategy.RadiusNormalDistributedDensityCalculationStrategy
import com.example.peopledensitymeasurementprototype.density.strategy.SimpleDensityCalculationStrategy
import java.util.*

class BaseDensityGrid: DensityGrid {

    private val locationList = LinkedList<UTMLocation>()
    var densityCalculationStrategy: DensityCalculationStrategy = SimpleDensityCalculationStrategy()

    var observer: (BaseDensityGrid)->Unit = {}

    override fun add(utmLocation: UTMLocation) {
        if (utmLocation.timestamp == null) {
            throw IllegalArgumentException("Timestamp is null!")
        }
        if (utmLocation.deviceId == null) {
            throw IllegalArgumentException("DeviceID is null!")
        }

        locationList.updateByDeviceId(utmLocation)
        densityCalculationStrategy.update(locationList)
        observer(this)
    }

    override fun getDensityAt(utmLocation: UTMLocation): Density {
        return densityCalculationStrategy.calculateDensityAt(locationList, utmLocation)
    }

    override fun getNumberOfDevices(): Int {
        return locationList.size
    }

    private fun LinkedList<UTMLocation>.updateByDeviceId(location: UTMLocation) {
        this.removeIf { it.deviceId == location.deviceId }
        add(location)
    }

}
