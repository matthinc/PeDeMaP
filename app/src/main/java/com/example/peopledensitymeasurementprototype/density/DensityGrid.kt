package com.example.peopledensitymeasurementprototype.density

interface DensityGrid {
    fun add(utmLocation: UTMLocation)
    fun getDensityAt(utmLocation: UTMLocation): Density
    fun getNumberOfDevices(): Int
}
