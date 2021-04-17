package edu.hm.pedemap.density.strategy

import android.app.Application
import edu.hm.pedemap.density.Density
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.density.sum
import edu.hm.pedemap.util.bApplication
import kotlin.collections.HashMap
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.pow

class RadiusNormalDistributedDensityCalculationStrategy(val application: Application) : DensityCalculationStrategy {

    private var density: List<Map<LocationKey, Density>> = emptyList()

    /**
     * Normalize distribution: Returns a probability field
     * where the sum of all probabilities is 1
     */
    private fun Map<LocationKey, Density>.normalizeDensityDistribution(): Map<LocationKey, Density> {
        val sum = values.sum()
        return mapValues { it.value.normalized(sum.people) }
    }

    /**
     * Calculate the probability field for each device.
     * @return [Map<LocationKey, Density>]
     */
    private fun densityDistributionByDevice(location: UTMLocation): Map<LocationKey, Density> {
        val densDist = HashMap<LocationKey, Density>()
        val cellSize = application.bApplication().cellSize

        // Calculate the density in a circle with r = accuracy * MAX_RADIUS
        val range = floor(location.accuracy!! * MAX_RADIUS).toInt() / cellSize

        // Get density function of normal distribution with sigma = accuracy
        val distribution = normDist(location.accuracy!!.toDouble())

        for (n in -range..range) {
            for (e in -range..range) {
                val offsetLocation = location.withOffset(n * cellSize, e * cellSize)

                // Only calculate within a MAX_RADIUS sigma radius
                if (offsetLocation.distanceTo(location) ?: 0 > MAX_RADIUS * location.accuracy!!) continue

                val distance = location.distanceTo(offsetLocation) ?: 0

                // Calculate the density
                val density = Density(distribution(distance))

                // Insert into the map
                densDist[LocationKey(offsetLocation.northing, offsetLocation.easting)] = density
            }
        }

        // normalize
        return densDist.normalizeDensityDistribution()
    }

    override fun update(locations: List<UTMLocation>) {
        density = locations.map { densityDistributionByDevice(it) }
    }

    override fun calculateDensityAt(locations: List<UTMLocation>, location: UTMLocation): Density {
        return density.map { it[LocationKey(location.northing, location.easting)] ?: Density.empty() }.sum()
    }

    /**
     * Returns the density function of the normal distribution
     * @param sigma Sigma value. Mu is always 0
     */
    private fun normDist(sigma: Double): (Int) -> Double {
        // Normal distribution with mu = 0 and variable sigma
        return { (1 / (sigma * PI2_SQRT)) * exp(-(it.toDouble()).pow(2.0) / (2 * sigma.pow(2.0))) }
    }

    /**
     * Helper class. Like [UTMLocation] but only northing and easting values.
     */
    private data class LocationKey(val n: Int, val e: Int)

    companion object {
        private const val MAX_RADIUS = 1.5
        private const val PI2_SQRT = 2.50662827463 // sqrt(2pi)
    }
}
