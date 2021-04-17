package edu.hm.pedemap.density

interface DensityGrid {
    /**
     * Add a new UTM location to the grid. Updates or inserts based on [UTMLocation.deviceId]
     */
    fun add(utmLocation: UTMLocation)

    /**
     * Integrate foreign map into own map
     */
    fun integrate(grid: ForeignDensityMap)

    /**
     * Get the density at a given location
     */
    fun getDensityAt(utmLocation: UTMLocation): Density

    /**
     * Get number of devices used to calculate the density
     */
    fun getNumberOfDevices(): Int

    /**
     * Convert to object that can be sent via network
     */
    fun getForeignDensityMap(ownDeviceId: Int): ForeignDensityMap

    /**
     * Checks if the grid contains a device id
     */
    fun containsDeviceId(deviceId: Int): Boolean

    /**
     * Remove old / invalid locations
     */
    fun purge()
}
