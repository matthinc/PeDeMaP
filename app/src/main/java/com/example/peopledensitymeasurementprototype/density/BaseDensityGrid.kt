package com.example.peopledensitymeasurementprototype.density

import com.example.peopledensitymeasurementprototype.density.strategy.DensityCalculationStrategy
import com.example.peopledensitymeasurementprototype.density.strategy.SimpleDensityCalculationStrategy
import com.example.peopledensitymeasurementprototype.util.epochSecondTimestamp
import java.util.*

class BaseDensityGrid : DensityGrid {

    private val locationList = LinkedList<UTMLocation>()
    var densityCalculationStrategy: DensityCalculationStrategy = SimpleDensityCalculationStrategy()

    var observer: (BaseDensityGrid) -> Unit = {}

    var aging = true

    override fun add(utmLocation: UTMLocation) {
        if (utmLocation.timestamp == null) {
            throw IllegalArgumentException("Timestamp is null!")
        }
        if (utmLocation.deviceId == null) {
            throw IllegalArgumentException("DeviceID is null!")
        }
        if (utmLocation.ttl == null) {
            throw IllegalArgumentException("TTL is null!")
        }

        locationList.updateByDeviceId(utmLocation)
        densityCalculationStrategy.update(locationList)
        observer(this)
    }

    override fun getDensityAt(utmLocation: UTMLocation): Density {
        if (aging) performAging()
        return densityCalculationStrategy.calculateDensityAt(locationList, utmLocation)
    }

    override fun getNumberOfDevices(): Int {
        return locationList.size
    }

    private fun LinkedList<UTMLocation>.updateByDeviceId(location: UTMLocation) {
        this.removeIf { it.deviceId == location.deviceId }
        add(location)
    }

    private fun performAging() {
        if (locationList.removeIf { it.timestamp!! + it.ttl!! < epochSecondTimestamp() }) {
            densityCalculationStrategy.update(locationList)
            observer(this)
        }
    }
}
