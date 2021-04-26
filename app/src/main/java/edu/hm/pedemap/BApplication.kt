package edu.hm.pedemap

import android.app.Application
import edu.hm.pedemap.density.grid.BaseDensityGrid
import edu.hm.pedemap.density.strategy.RadiusNormalDistributedDensityCalculationStrategy
import edu.hm.pedemap.density.strategy.SimpleDensityCalculationStrategy
import edu.hm.pedemap.messages.MessageManager
import edu.hm.pedemap.net.send.UDPBroadcastSend
import edu.hm.pedemap.util.Preferences
import edu.hm.pedemap.util.getSettingsPreferences
import edu.hm.pedemap.util.readPropertyBoolean
import edu.hm.pedemap.util.readPropertyInt

class BApplication() : Application() {
    val grid = BaseDensityGrid()

    val sendLocationStrategy = UDPBroadcastSend(this)

    var currentLocationTTL = 0

    val warnMessageManager = MessageManager()

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

    fun getDeviceId(): Int {
        return getSettingsPreferences().readPropertyInt(Preferences.uniqueDeviceId)
    }
}
