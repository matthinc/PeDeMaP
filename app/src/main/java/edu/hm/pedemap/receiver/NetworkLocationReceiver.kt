package edu.hm.pedemap.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import edu.hm.pedemap.BApplication
import edu.hm.pedemap.MainActivity
import edu.hm.pedemap.R
import edu.hm.pedemap.density.ForeignDensityMap
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.messages.WarnMessage
import edu.hm.pedemap.model.entity.LOG_LEVEL_DEBUG
import edu.hm.pedemap.model.entity.LOG_LEVEL_ERROR
import edu.hm.pedemap.model.entity.LOG_LEVEL_INFO
import edu.hm.pedemap.model.proto.Definitions
import edu.hm.pedemap.util.log
import com.google.protobuf.InvalidProtocolBufferException

class NetworkLocationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra("data")) {
            val data = intent.getByteArrayExtra("data")

            if (data != null) {
                try {
                    handleProto(context, data)
                } catch (e: InvalidProtocolBufferException) {
                    log(context, LOG_LEVEL_ERROR, "NetworkLocationReceiver", "Received invalid proto")
                }
            }
        }
    }

    fun handleProto(context: Context, data: ByteArray) {
        val application = context.applicationContext as BApplication

        log(context, LOG_LEVEL_INFO, "NetworkLocationReceiver", "Received new packet with size ${data.size}")

        // Protobuf deserialize
        val pb = Definitions.LocationMessageWrapper.parseFrom(data)

        // Single location
        if (pb.hasSingle()) {
            handleSingleLocation(context, pb, application)
        }

        // Density map
        if (pb.hasMap()) {
            handleDensityMap(pb, context, application)
        }

        // Warn message
        if (pb.hasMessage()) {
            handleWarnMessage(pb, context, application)
        }
    }

    private fun handleWarnMessage(
        pb: Definitions.LocationMessageWrapper,
        context: Context,
        application: BApplication
    ) {
        val warnMessage = WarnMessage.fromProto(pb.message)

        log(
            context,
            LOG_LEVEL_INFO,
            "NetworkLocationReceiver",
            "Received warn message: ${warnMessage.message}"
        )

        val builder = NotificationCompat.Builder(context, MainActivity.WARN_MESSAGE_NOTIFICATION_CHANNEL_ID)
        builder.setSmallIcon(R.drawable.ic_baseline_warning_24)
        builder.setContentTitle("Warning")
        builder.setContentText(warnMessage.message)
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0x123, builder.build())

        application.warnMessageManager.add(warnMessage)
    }

    private fun handleDensityMap(
        pb: Definitions.LocationMessageWrapper,
        context: Context,
        application: BApplication
    ) {
        val densityMap = ForeignDensityMap.Builder.fromProto(pb.map).build()

        log(
            context,
            LOG_LEVEL_DEBUG,
            "NetworkLocationReceiver",
            "DensityMap with ${densityMap.locations.size} elements"
        )

        application.grid.integrate(densityMap)
    }

    private fun handleSingleLocation(
        context: Context,
        pb: Definitions.LocationMessageWrapper,
        application: BApplication
    ) {
        log(context, LOG_LEVEL_DEBUG, "NetworkLocationReceiver", "SingleLocation")

        // Convert to UTM
        val utmLocation = UTMLocation.builderFromSingleLocationProtobufObject(pb.single).build()

        // Log device ID
        log(context, LOG_LEVEL_INFO, "NetworkLocationReceiver", "Device ID: " + utmLocation.deviceId)

        // If device is new, send complete density map
        if (!application.grid.containsDeviceId(utmLocation.deviceId!!)) {
            application.sendLocationStrategy.sendDensityMap(
                application.grid.getForeignDensityMap(application.getDeviceId()).toDensityMapProto()
            )
        }

        // Add to density grid
        application.grid.add(utmLocation)

        log(
            context,
            LOG_LEVEL_INFO,
            "NetworkLocationReceiver",
            "Integrated new location into grid. Grid includes information " +
                "of ${application.grid.getNumberOfDevices()} devices."
        )
    }
}
