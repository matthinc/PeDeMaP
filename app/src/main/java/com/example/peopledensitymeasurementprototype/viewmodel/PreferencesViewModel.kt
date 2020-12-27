package com.example.peopledensitymeasurementprototype.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.peopledensitymeasurementprototype.BApplication
import com.example.peopledensitymeasurementprototype.density.strategy.RadiusNormalDistributedDensityCalculationStrategy
import com.example.peopledensitymeasurementprototype.density.strategy.SimpleDensityCalculationStrategy
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_INFO
import com.example.peopledensitymeasurementprototype.util.*

class PreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSettingsPreferences()

    private val intervals = Preferences.gpsInterval.possibleValues
    private val cellSizes = Preferences.cellSize.possibleValues

    val intervalStrings get() = intervals.map { "$it Seconds" }
    val cellSizesStrings get() = cellSizes.map { "${it}x${it}m" }

    var selectedInterval = intervals.indexOf(sharedPreferences.readPropertyInt(Preferences.gpsInterval))
        set(value) {
            val intervalValue = intervals[value]
            sharedPreferences.storeProperty(Preferences.gpsInterval.withValue(intervalValue))
            field = value
        }

    var smallestDisplacementEnabled = sharedPreferences.readPropertyBoolean(Preferences.smallestDisplacement)
        set(value) {
            log(getApplication(), LOG_LEVEL_INFO, "Location", "Changed smallest displacement to $value")
            sharedPreferences.storeProperty(Preferences.smallestDisplacement.withValue(value))
            field = value
        }

    var selectedAlgorithm = sharedPreferences.readPropertyInt(Preferences.densityStrategy)
        set(value) {
            val bApplication = getApplication() as BApplication
            sharedPreferences.storeProperty(Preferences.densityStrategy.withValue(value))
            bApplication.reloadPreferences()
            field = value
        }

    var aging = sharedPreferences.readPropertyBoolean(Preferences.aging)
        set(value) {
            val bApplication = getApplication() as BApplication
            sharedPreferences.storeProperty(Preferences.aging.withValue(value))
            bApplication.reloadPreferences()
            field = value
        }

    var cellSize = cellSizes.indexOf(sharedPreferences.readPropertyInt(Preferences.cellSize))
        set(value) {
            val cellSize = cellSizes[value]
            sharedPreferences.storeProperty(Preferences.cellSize.withValue(cellSize))
            field = value
        }
}
