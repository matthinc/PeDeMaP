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

    val intervalStrings get() = intervals.map { "$it Seconds" }

    var selectedInterval = intervals.indexOf(sharedPreferences.readPropertyInt(Preferences.gpsInterval))
        set(value) {
            val intervalValue = intervals[value]
            log(getApplication(), LOG_LEVEL_INFO, "Location", "Changed interval to $intervalValue")
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
            bApplication.grid.densityCalculationStrategy = when (value) {
                1 -> RadiusNormalDistributedDensityCalculationStrategy()
                else -> SimpleDensityCalculationStrategy()
            }
            sharedPreferences.storeProperty(Preferences.densityStrategy.withValue(value))
            field = value
        }
}
