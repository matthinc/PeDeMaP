package com.example.peopledensitymeasurementprototype

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.peopledensitymeasurementprototype.receiver.BatteryChangedReceiver
import com.example.peopledensitymeasurementprototype.service.ActivityRecognitionService
import com.example.peopledensitymeasurementprototype.service.LocationService
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_map, R.id.navigation_log, R.id.navigation_preferences)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(NotificationChannel(NOTIFICATION_CHANNEL_ID, "Test", NotificationManager.IMPORTANCE_LOW))

        handlePermissions()
        startService(Intent(this, LocationService::class.java))
        startService(Intent(this, ActivityRecognitionService::class.java))

        registerReceiver(BatteryChangedReceiver(), IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    private fun handlePermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            0
        )
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "ba_mru-chanel"
    }
}
