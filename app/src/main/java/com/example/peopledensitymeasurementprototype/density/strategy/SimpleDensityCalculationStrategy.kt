package com.example.peopledensitymeasurementprototype.density.strategy

import com.example.peopledensitymeasurementprototype.density.Density
import com.example.peopledensitymeasurementprototype.density.UTMLocation

class SimpleDensityCalculationStrategy : DensityCalculationStrategy {

    override fun calculateDensityAt(locations: List<UTMLocation>, location: UTMLocation): Density {
        return Density(locations.count { location.matchesPosition(it) }.toDouble())
    }

    override fun update(locations: List<UTMLocation>) {}
}
