package com.example.peopledensitymeasurementprototype.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_INFO
import com.example.peopledensitymeasurementprototype.util.formatForLog
import com.example.peopledensitymeasurementprototype.util.log
import com.google.android.gms.location.LocationResult

class LocationUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (LocationResult.hasResult(intent)) {
            val locationResult = LocationResult.extractResult(intent)
            val location = locationResult.lastLocation

            log(context, LOG_LEVEL_INFO, "Location", location.formatForLog())
        }
    }
}
