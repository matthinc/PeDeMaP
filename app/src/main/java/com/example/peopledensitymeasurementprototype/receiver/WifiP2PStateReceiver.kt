package com.example.peopledensitymeasurementprototype.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pManager
import androidx.core.app.ActivityCompat

class WifiP2PStateReceiver(val manager: WifiP2pManager, val channel: WifiP2pManager.Channel) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) return

        if (intent.action == WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION) {
            // log(context, LOG_LEVEL_INFO, "WifiP2P", "Peers changed")

            manager.requestPeers(channel) {
                // log(context, LOG_LEVEL_INFO, "WifiP2P", it.deviceList.joinToString("\n"))

                val devices = it.deviceList

                if (!devices.isEmpty()) {

                    // Connect to first device
                    val config = WifiP2pConfig()
                    config.deviceAddress = it.deviceList.first().deviceAddress

                    manager.connect(
                        channel,
                        config,
                        object : WifiP2pManager.ActionListener {

                            override fun onSuccess() {
                                // log(context, LOG_LEVEL_INFO, "WifiP2P", "Successfully connected")
                            }

                            override fun onFailure(p0: Int) {
                                // log(context, LOG_LEVEL_INFO, "WifiP2P", "Connection failed: $p0")
                            }
                        }
                    )
                }
            }
        }
    }
}
