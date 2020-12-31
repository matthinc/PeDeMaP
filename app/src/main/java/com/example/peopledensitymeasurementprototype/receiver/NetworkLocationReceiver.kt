package com.example.peopledensitymeasurementprototype.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.peopledensitymeasurementprototype.BApplication
import com.example.peopledensitymeasurementprototype.density.ForeignDensityMap
import com.example.peopledensitymeasurementprototype.density.UTMLocation
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_DEBUG
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_INFO
import com.example.peopledensitymeasurementprototype.model.proto.Definitions
import com.example.peopledensitymeasurementprototype.util.log

class NetworkLocationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra("data")) {
            val data = intent.getByteArrayExtra("data")

            if (data != null) {
                log(context, LOG_LEVEL_INFO, "NetworkLocationReceiver", "Received new packet with size ${data.size}")

                // Protobuf deserialize
                val pb = Definitions.LocationMessageWrapper.parseFrom(data)

                // Single location
                if (pb.hasSingle()) {
                    log(context, LOG_LEVEL_DEBUG, "NetworkLocationReceiver", "SingleLocation")

                    // Convert to UTM
                    val utmLocation = UTMLocation.builderFromSingleLocationProtobufObject(pb.single).build()

                    val application = context.applicationContext as BApplication
                    application.grid.add(utmLocation)

                    log(
                        context,
                        LOG_LEVEL_INFO,
                        "NetworkLocationReceiver",
                        "Integrated new location into grid. Grid includes information " +
                            "of ${application.grid.getNumberOfDevices()} devices."
                    )
                }

                // Density map
                if (pb.hasMap()) {
                    log(context, LOG_LEVEL_DEBUG, "NetworkLocationReceiver", "DensityMap")

                    val densityMap = ForeignDensityMap.Builder.fromProto(pb.map)
                }
            }
        }
    }
}
