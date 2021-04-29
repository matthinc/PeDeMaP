package edu.hm.pedemap

import edu.hm.pedemap.density.ForeignDensityMap
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.density.grid.BaseDensityGrid
import edu.hm.pedemap.density.grid.DensityGrid
import edu.hm.pedemap.util.epochSecondTimestamp
import org.junit.Assert
import org.junit.Test

class TestDensityGrid {

    @Test
    fun testReplaceDevice() {
        val grid: DensityGrid = BaseDensityGrid().also { it.aging = false }

        grid.add(
            UTMLocation.newBuilder(32, 100, 200, true)
                .withDeviceId(1510)
                .withTTL(5000)
                .withTimestamp(100)
                .build()
        )

        grid.add(
            UTMLocation.newBuilder(32, 105, 200, true)
                .withDeviceId(1510)
                .withTTL(5000)
                .withTimestamp(110)
                .build()
        )

        Assert.assertEquals(1, grid.getNumberOfDevices())
        Assert.assertTrue(grid.containsDeviceId(1510))
    }

    @Test
    fun testAging() {
        val grid = BaseDensityGrid().also { it.aging = true }

        grid.add(
            UTMLocation.newBuilder(32, 100, 200, true)
                .withDeviceId(1510)
                .withTTL(1)
                .withTimestamp(epochSecondTimestamp())
                .build()
        )

        grid.purge()
        Assert.assertEquals(1, grid.getNumberOfDevices())

        synchronized(this) { Thread.sleep(2500) }

        grid.purge()
        Assert.assertEquals(0, grid.getNumberOfDevices())
    }

    @Test
    fun testForeignDensityMap() {
        val grid = BaseDensityGrid().also { it.aging = true }

        val location1 = UTMLocation.newBuilder(32, 100, 200, true)
            .withDeviceId(1510)
            .withTTL(1)
            .withTimestamp(epochSecondTimestamp())
            .build()

        val location2 = UTMLocation.newBuilder(32, 100, 200, true)
            .withDeviceId(1511)
            .withTTL(1)
            .withTimestamp(epochSecondTimestamp())
            .build()

        grid.add(location1)

        val map = ForeignDensityMap.Builder()
            .setSender(1510)
            .setLocations(listOf(location1, location2))
            .build()

        Assert.assertEquals(1, grid.getNumberOfDevices())
        grid.integrate(map)
        Assert.assertEquals(2, grid.getNumberOfDevices())
    }
}
