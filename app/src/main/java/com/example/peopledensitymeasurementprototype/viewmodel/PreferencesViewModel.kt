package com.example.peopledensitymeasurementprototype.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
}
