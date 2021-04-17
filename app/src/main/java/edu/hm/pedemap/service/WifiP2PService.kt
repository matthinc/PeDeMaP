package edu.hm.pedemap.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import edu.hm.pedemap.model.entity.LOG_LEVEL_ERROR
import edu.hm.pedemap.model.entity.LOG_LEVEL_INFO
import edu.hm.pedemap.receiver.WifiP2PStateReceiver
import edu.hm.pedemap.util.log

class WifiP2PService : Service(), WifiP2pManager.ActionListener {

    lateinit var channel: WifiP2pManager.Channel

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter()
        val actions = arrayOf(
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION,
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION,
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION,
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
        )

        actions.forEach(filter::addAction)

        val manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)

        registerReceiver(WifiP2PStateReceiver(manager, channel), filter)
        discoverPeers(manager)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun discoverPeers(manager: WifiP2pManager) {
        if (
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            manager.discoverPeers(channel, this)
        }
    }

    override fun onSuccess() {
        log(this, LOG_LEVEL_INFO, "WifiP2P", "Discover peers: success")
    }

    override fun onFailure(p0: Int) {
        log(this, LOG_LEVEL_ERROR, "WifiP2P", "Discover peers: $p0")
    }
}
