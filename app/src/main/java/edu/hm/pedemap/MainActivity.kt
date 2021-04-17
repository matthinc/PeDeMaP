package edu.hm.pedemap

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import edu.hm.pedemap.density.UniqueIDProvider
import edu.hm.pedemap.fragment.MapFragment
import edu.hm.pedemap.model.entity.LOG_LEVEL_INFO
import edu.hm.pedemap.receiver.BatteryChangedReceiver
import edu.hm.pedemap.service.ActivityRecognitionService
import edu.hm.pedemap.service.LocationBroadcastReceiverService
import edu.hm.pedemap.service.LocationService
import edu.hm.pedemap.util.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (BuildConfig.FLAVOR) {
            "dev_ui"  -> setupDebugLayout()
            "demo_ui" -> setupReleaseLayout()
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(NOTIFICATION_CHANNEL_ID, "Test", NotificationManager.IMPORTANCE_LOW)
            )
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    WARN_MESSAGE_NOTIFICATION_CHANNEL_ID,
                    "Warn",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        // Generate device id
        if (getSettingsPreferences().readPropertyInt(Preferences.uniqueDeviceId)
            == Preferences.uniqueDeviceId.defaultValue
        ) {
            getSettingsPreferences().storeProperty(
                Preferences.uniqueDeviceId.withValue(UniqueIDProvider.generateUniqueDeviceId())
            )
        }

        println(packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT))

        (application as BApplication).reloadPreferences()

        log(
            this,
            LOG_LEVEL_INFO,
            "MainActivity",
            "Started with device-id ${getSettingsPreferences().readPropertyInt(Preferences.uniqueDeviceId)}"
        )

        handlePermissions()
        startService(Intent(this, LocationService::class.java))
        startService(Intent(this, ActivityRecognitionService::class.java))
        startService(Intent(this, LocationBroadcastReceiverService::class.java))
        // startService(Intent(this, WifiP2PService::class.java))

        registerReceiver(BatteryChangedReceiver(), IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    fun setupReleaseLayout() {
        setContentView(R.layout.activity_main_release)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_holder, MapFragment())
            .commit()
    }

    fun setupDebugLayout() {
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
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Permission granted -> restart location service
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            restartService(Intent(this, LocationService::class.java))
        }
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
        const val WARN_MESSAGE_NOTIFICATION_CHANNEL_ID = "ba_mru-warn"
    }
}
