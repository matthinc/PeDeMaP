package com.example.peopledensitymeasurementprototype.density.strategy

import android.content.Context
import com.example.peopledensitymeasurementprototype.density.Density
import com.example.peopledensitymeasurementprototype.density.UTMArea
import com.example.peopledensitymeasurementprototype.density.UTMLocation

interface DensityCalculationStrategy {
    fun calculateDensityAt(locations: List<UTMLocation>, location: UTMLocation): Density
    fun update(locations: List<UTMLocation>) {}
}
