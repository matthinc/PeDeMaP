package edu.hm.pedemap.density.strategy

import edu.hm.pedemap.density.Density
import edu.hm.pedemap.density.UTMLocation

interface DensityCalculationStrategy {
    fun calculateDensityAt(locations: List<UTMLocation>, location: UTMLocation): Density
    fun update(locations: List<UTMLocation>) {}
}
