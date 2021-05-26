package edu.hm.pedemap.density.grid

import edu.hm.pedemap.density.Density
import edu.hm.pedemap.density.ForeignDensityMap
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.density.strategy.DensityCalculationStrategy
import edu.hm.pedemap.density.strategy.SimpleDensityCalculationStrategy
import edu.hm.pedemap.util.epochSecondTimestamp
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

    override fun integrate(grid: ForeignDensityMap) {
        grid.locations.forEach(this::add)
    }

    override fun getDensityAt(utmLocation: UTMLocation): Density {
        return densityCalculationStrategy.calculateDensityAt(locationList, utmLocation)
    }

    override fun getNumberOfDevices(): Int {
        return locationList.size
    }

    override fun getForeignDensityMap(ownDeviceId: Int): ForeignDensityMap {
        return ForeignDensityMap.Builder()
            .setLocations(locationList)
            .setSender(ownDeviceId)
            .build()
    }

    override fun containsDeviceId(deviceId: Int): Boolean {
        return locationList.find { it.deviceId == deviceId } != null
    }

    override fun purge() {
        if (aging) performAging()
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

    override fun hashCode(): Int {
        return locationList.map { it.hashCode() }.sum() % Int.MAX_VALUE
    }
}
