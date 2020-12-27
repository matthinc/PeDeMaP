package com.example.peopledensitymeasurementprototype.density.strategy

import android.app.Application
import com.example.peopledensitymeasurementprototype.density.Density
import com.example.peopledensitymeasurementprototype.density.UTMLocation
import com.example.peopledensitymeasurementprototype.density.sum
import com.example.peopledensitymeasurementprototype.util.bApplication
import kotlin.collections.HashMap
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.pow

class RadiusNormalDistributedDensityCalculationStrategy(val application: Application): DensityCalculationStrategy {

    private var density : List<Map<LocationKey, Density>> = emptyList()

    private fun Map<LocationKey, Density>.normalizeDensityDistribution(): Map<LocationKey, Density> {
        val sum = values.sum()
        return mapValues { it.value.normalized(sum.people) }
    }

    private fun densityDistributionByDevice(location: UTMLocation): Map<LocationKey, Density> {
        val densDist = HashMap<LocationKey, Density>()

        val cellSize = application.bApplication().cellSize

        val range = floor(location.accuracy!! * MAX_RADIUS).toInt() / cellSize
        val distribution = normDist(location.accuracy!!.toDouble())

        for (n in -range..range) {
            for (e in -range..range) {
                val offsetLocation = location.withOffset(n * cellSize, e * cellSize)

                // Only calculate within a MAX_RADIUS sigma radius
                if (offsetLocation.distanceTo(location) ?: 0 > MAX_RADIUS * location.accuracy!!) continue

                val distance = location.distanceTo(offsetLocation) ?: 0
                
                val density = Density(distribution(distance))
                densDist[LocationKey(offsetLocation.northing, offsetLocation.easting)] = density
            }
        }

        return densDist.normalizeDensityDistribution()
    }

    override fun update(locations: List<UTMLocation>) {
         density = locations.map { densityDistributionByDevice(it) }
    }

    override fun calculateDensityAt(locations: List<UTMLocation>, location: UTMLocation): Density {
        return density.map { it[LocationKey(location.northing, location.easting)] ?: Density.empty() }.sum()
    }

    private fun normDist(sigma: Double) : (Int) -> Double {
        // Normal distribution with mu = 0 and variable sigma
        return { (1 / (sigma * PI2_SQRT)) * exp(-(it.toDouble()).pow(2.0) / (2 * sigma.pow(2.0))) }
    }

    private data class LocationKey(val n: Int, val e: Int)

    companion object {
        private const val MAX_RADIUS = 1.5
        private const val PI2_SQRT = 2.50662827463 // sqrt(2pi)
    }

}

