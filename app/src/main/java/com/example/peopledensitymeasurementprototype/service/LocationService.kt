package com.example.peopledensitymeasurementprototype.service

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.peopledensitymeasurementprototype.MainActivity
import com.example.peopledensitymeasurementprototype.R
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_DEBUG
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_ERROR
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_WARN
import com.example.peopledensitymeasurementprototype.receiver.LocationUpdateReceiver
import com.example.peopledensitymeasurementprototype.util.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationService : Service() {

    private val locationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationUpdateReceiverPendingIntent by lazy {
        val intent = Intent(this, LocationUpdateReceiver::class.java)
        PendingIntent.getBroadcast(this, REQUEST_CODE_LOCATION_UPDATE, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    private val foregroundNotification by lazy {
        val intent = Intent(this, MainActivity::class.java)

        NotificationCompat.Builder(this, MainActivity.NOTIFICATION_CHANNEL_ID).apply {
            setContentTitle(applicationInfo.loadLabel(packageManager))
            setContentText(getString(R.string.foreground_message))
            setContentIntent(PendingIntent.getActivity(this@LocationService, 0, intent, 0))
        }.build()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(LOCATION_SERVICE_FOREGROUND_ID, foregroundNotification)
        startLocationUpdates()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopLocationUpdates()
        super.onDestroy()
    }

    private fun startLocationUpdates() {
        // Permission check
        val permissionsOK = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }

        if (!permissionsOK) {
            log(this, LOG_LEVEL_ERROR, "Location", "Location permission not granted")
            return
        }

        val locationTask = locationClient.requestLocationUpdates(
            getLocationRequest(),
            locationUpdateReceiverPendingIntent
        )

        log(this, LOG_LEVEL_DEBUG, "Location", "Start location updates")

        locationTask.addOnSuccessListener {
            log(this, LOG_LEVEL_DEBUG, "Location", "Start location updates: Successful")
        }

        locationTask.addOnFailureListener {
            log(this, LOG_LEVEL_ERROR, "Location", "Start location updates: Failed\n${it.message}")
        }
    }

    private fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationUpdateReceiverPendingIntent)
        log(this, LOG_LEVEL_WARN, "Location", "Stop location updates")
    }

    private fun getLocationRequest(): LocationRequest {
        val preferences = getSettingsPreferences()

        return LocationRequest.create().apply {
            interval = preferences.readPropertyInt(Preferences.gpsInterval).toLong() * 1_000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Better than 100m 

            if (preferences.readPropertyBoolean(Preferences.smallestDisplacement)) {
                smallestDisplacement = 5f
            }

            // Force constant interval for repeatable measurements
            fastestInterval = interval
        }
    }

    companion object {
        const val REQUEST_CODE_LOCATION_UPDATE = 0x1510
        const val LOCATION_SERVICE_FOREGROUND_ID = 0x1511
    }
}
