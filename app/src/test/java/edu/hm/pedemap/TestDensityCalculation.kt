package edu.hm.pedemap

import edu.hm.pedemap.density.Density
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.density.strategy.RadiusNormalDistributedDensityCalculationStrategy
import edu.hm.pedemap.density.strategy.SimpleDensityCalculationStrategy
import org.junit.Assert
import org.junit.Test

class TestDensityCalculation {

    private val testLocations by lazy {
        listOf(
            UTMLocation
                .newBuilder(32, 500, 300, true)
                .withAccuracy(20f)
                .build()
        )
    }

    private val testLocationsHighAccuracy by lazy {
        listOf(
            UTMLocation
                .newBuilder(32, 500, 300, true)
                .withAccuracy(1f)
                .build()
        )
    }

    @Test
    fun testSimpleStrategy() {
        val strategy = SimpleDensityCalculationStrategy()
        strategy.update(testLocations)

        UTMLocation.newBuilder(32, 500, 300, true).build().let {
            Assert.assertEquals(Density(1.0), strategy.calculateDensityAt(testLocations, it))
        }
        UTMLocation.newBuilder(32, 505, 300, true).build().let {
            Assert.assertEquals(Density(0.0), strategy.calculateDensityAt(testLocations, it))
        }
    }

    @Test
    fun testNormalDistributionStrategy() {
        val strategy = RadiusNormalDistributedDensityCalculationStrategy(5)
        strategy.update(testLocations)

        UTMLocation.newBuilder(32, 500, 300, true).build().let {
            Assert.assertNotEquals(Density(1.0), strategy.calculateDensityAt(testLocations, it))
        }

        var totalDensity = 0.0

        for (nOffset in -10..10) {
            for (eOffset in -10..10) {
                totalDensity += strategy
                    .calculateDensityAt(
                        testLocations,
                        testLocations[0].withOffset(nOffset * 5, eOffset * 5)
                    ).people
            }
        }

        Assert.assertEquals(1.0, totalDensity, 0.001)
    }

    @Test
    fun testNormalDistributionStrategyHighAccuracy() {
        val strategy = RadiusNormalDistributedDensityCalculationStrategy(5)
        strategy.update(testLocationsHighAccuracy)

        UTMLocation.newBuilder(32, 500, 300, true).build().let {
            Assert.assertEquals(Density(1.0), strategy.calculateDensityAt(testLocations, it))
        }
    }
}
