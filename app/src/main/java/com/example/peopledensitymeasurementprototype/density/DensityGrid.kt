package com.example.peopledensitymeasurementprototype.density

interface DensityGrid {
    /**
     * Add a new UTM location to the grid. Updates or inserts based on [UTMLocation.deviceId]
     */
    fun add(utmLocation: UTMLocation)

    /**
     * Get the density at a given location
     */
    fun getDensityAt(utmLocation: UTMLocation): Density

    /**
     * Get number of devices used to calculate the density
     */
    fun getNumberOfDevices(): Int
}
