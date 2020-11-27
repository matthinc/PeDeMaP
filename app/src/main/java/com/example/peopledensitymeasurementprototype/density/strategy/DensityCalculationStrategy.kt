package com.example.peopledensitymeasurementprototype.density.strategy

import com.example.peopledensitymeasurementprototype.density.Density
import com.example.peopledensitymeasurementprototype.density.UTMLocation

interface DensityCalculationStrategy {
    fun calculateDensityAt(locations: List<UTMLocation>, location: UTMLocation): Density
}
