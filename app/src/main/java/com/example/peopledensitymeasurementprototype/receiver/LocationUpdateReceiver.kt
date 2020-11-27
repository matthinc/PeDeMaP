package com.example.peopledensitymeasurementprototype.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.peopledensitymeasurementprototype.density.UTMLocation
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_DEBUG
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_INFO
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_WARN
import com.example.peopledensitymeasurementprototype.density.toLocationEntity
import com.example.peopledensitymeasurementprototype.density.toSingleProto
import com.example.peopledensitymeasurementprototype.net.send.UDPBroadcastSend
import com.example.peopledensitymeasurementprototype.repository.LocationRepository
import com.example.peopledensitymeasurementprototype.util.*
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocationUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val locationRepository = LocationRepository(context)

        if (LocationResult.hasResult(intent)) {
            val locationResult = LocationResult.extractResult(intent)
            val location = locationResult.lastLocation

            // Convert location to grid location
            val gridLocation = UTMLocation.builderFromLocation(location)
                .withDeviceId(context.getSettingsPreferences().readPropertyInt(Preferences.uniqueDeviceId))
                .withTimestamp(epochSecondTimestamp().toInt())
                .build()

            log(context, LOG_LEVEL_INFO, "Location Update", arrayOf(
                location.latitude,
                location.longitude,
                location.accuracy,
                location.bearing,
                location.speed,
                gridLocation.zoneId,
                gridLocation.hemisphere(),
                gridLocation.northing,
                gridLocation.easting
            ))

            GlobalScope.launch {
                locationRepository.insertLocation(gridLocation.toLocationEntity())
            }

            UDPBroadcastSend(context).sendSingleLocationData(gridLocation.toSingleProto())

        } else {
            log(context, LOG_LEVEL_WARN, "Location", "Received Broadcast without valid location")
            log(context, LOG_LEVEL_DEBUG, "Location", intent.extras.toString())
        }
    }
}
