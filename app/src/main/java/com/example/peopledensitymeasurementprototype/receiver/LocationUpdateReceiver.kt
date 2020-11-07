package com.example.peopledensitymeasurementprototype.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_DEBUG
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_INFO
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_WARN
import com.example.peopledensitymeasurementprototype.repository.LocationRepository
import com.example.peopledensitymeasurementprototype.util.log
import com.example.peopledensitymeasurementprototype.util.toLocationEntity
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocationUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val locationRepository = LocationRepository(context)

        if (LocationResult.hasResult(intent)) {
            val locationResult = LocationResult.extractResult(intent)
            val location = locationResult.lastLocation

            log(context, LOG_LEVEL_INFO, "Location Update", arrayOf(
                location.latitude,
                location.longitude,
                location.accuracy,
                location.bearing,
                location.speed
            ))

            GlobalScope.launch {
                locationRepository.insertLocation(location.toLocationEntity())
            }
        } else {
            log(context, LOG_LEVEL_WARN, "Location", "Received Broadcast without valid location")
            log(context, LOG_LEVEL_DEBUG, "Location", intent.extras.toString())
        }
    }
}
