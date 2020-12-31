package com.example.peopledensitymeasurementprototype

import android.app.Application
import com.example.peopledensitymeasurementprototype.density.BaseDensityGrid
import com.example.peopledensitymeasurementprototype.density.strategy.RadiusNormalDistributedDensityCalculationStrategy
import com.example.peopledensitymeasurementprototype.density.strategy.SimpleDensityCalculationStrategy
import com.example.peopledensitymeasurementprototype.net.send.UDPBroadcastSend
import com.example.peopledensitymeasurementprototype.util.Preferences
import com.example.peopledensitymeasurementprototype.util.getSettingsPreferences
import com.example.peopledensitymeasurementprototype.util.readPropertyBoolean
import com.example.peopledensitymeasurementprototype.util.readPropertyInt

class BApplication() : Application() {
    val grid = BaseDensityGrid()

    val sendLocationStrategy = UDPBroadcastSend(this)

    var currentLocationTTL = 0

    val cellSize by lazy {
        getSettingsPreferences().readPropertyInt(Preferences.cellSize)
    }

    fun reloadPreferences() {
        grid.densityCalculationStrategy = when (getSettingsPreferences().readPropertyInt(Preferences.densityStrategy)) {
            1 -> RadiusNormalDistributedDensityCalculationStrategy(this)
            else -> SimpleDensityCalculationStrategy()
        }
        grid.aging = getSettingsPreferences().readPropertyBoolean(Preferences.aging)
    }
}
