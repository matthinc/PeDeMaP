package edu.hm.pedemap.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.LocationResult
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.density.toLocationEntity
import edu.hm.pedemap.density.toSingleProto
import edu.hm.pedemap.model.entity.LOG_LEVEL_DEBUG
import edu.hm.pedemap.model.entity.LOG_LEVEL_INFO
import edu.hm.pedemap.model.entity.LOG_LEVEL_WARN
import edu.hm.pedemap.repository.LocationRepository
import edu.hm.pedemap.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocationUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val locationRepository = LocationRepository(context)

        if (LocationResult.hasResult(intent)) {
            val locationResult = LocationResult.extractResult(intent)
            val location = locationResult.lastLocation

            // Convert location to grid location
            val gridLocation = UTMLocation.builderFromLocation(location, context.bApplication().cellSize)
                .withDeviceId(context.bApplication().getDeviceId())
                .withTimestamp(epochSecondTimestamp())
                .withTTL(context.bApplication().currentLocationTTL)
                .build()

            log(
                context,
                LOG_LEVEL_INFO,
                "Location Update",
                arrayOf(
                    location.latitude,
                    location.longitude,
                    location.accuracy,
                    location.bearing,
                    location.speed,
                    gridLocation.zoneId,
                    gridLocation.hemisphere(),
                    gridLocation.northing,
                    gridLocation.easting
                )
            )

            GlobalScope.launch {
                locationRepository.insertLocation(gridLocation.toLocationEntity())
            }

            context.bApplication().sendLocationStrategy.sendSingleLocationData(gridLocation.toSingleProto())
        } else {
            log(context, LOG_LEVEL_WARN, "Location", "Received Broadcast without valid location")
            log(context, LOG_LEVEL_DEBUG, "Location", intent.extras.toString())
        }
    }
}
