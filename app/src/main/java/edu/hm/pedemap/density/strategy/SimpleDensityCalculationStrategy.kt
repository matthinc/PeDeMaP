package edu.hm.pedemap.density.strategy

import edu.hm.pedemap.density.Density
import edu.hm.pedemap.density.UTMLocation

class SimpleDensityCalculationStrategy : DensityCalculationStrategy {

    override fun calculateDensityAt(locations: List<UTMLocation>, location: UTMLocation): Density {
        return Density(locations.count { location.matchesPosition(it) }.toDouble())
    }

    override fun update(locations: List<UTMLocation>) {}
}
