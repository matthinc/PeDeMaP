package com.example.peopledensitymeasurementprototype.util

import android.content.Context
import android.content.SharedPreferences

fun Context.getSettingsPreferences() = getSharedPreferences("settings_preferences", Context.MODE_PRIVATE)

fun <T> SharedPreferences.storeProperty(prop: PropertyWithValue<T>) {
    if (prop.value is Int) {
        edit().putInt(prop.property.key, prop.value).apply()
    } else if (prop.value is Boolean) {
        edit().putBoolean(prop.property.key, prop.value).apply()
    }
}

fun SharedPreferences.readPropertyInt(prop: Property<Int>): Int {
    return getInt(prop.key, prop.defaultValue)
}

fun SharedPreferences.readPropertyBoolean(prop: Property<Boolean>): Boolean {
    return getBoolean(prop.key, prop.defaultValue)
}

object Preferences {
    val gpsInterval = Property<Int>("gps_interval", 5, arrayOf(5, 10, 15, 20, 25, 30, 60))
    val smallestDisplacement = Property<Boolean>("smallest_displacement", true, emptyArray())
    val uniqueDeviceId = Property<Int>("device_id", 0, emptyArray())
    val densityStrategy = Property<Int>("density_strategy",0, emptyArray())
    val aging = Property<Boolean>("aging", true, emptyArray())
    val cellSize = Property("cell_size", 10, arrayOf(1, 3, 5, 10, 20, 30, 100))
}

data class Property<T>(val key: String, val defaultValue: T, val possibleValues: Array<T>) {
    fun withValue(value: T) = PropertyWithValue(this, value)
}

data class PropertyWithValue<T>(val property: Property<T>, val value: T)
