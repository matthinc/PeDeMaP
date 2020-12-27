package com.example.peopledensitymeasurementprototype.net.send

import android.content.Context
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_DEBUG
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_WARN
import com.example.peopledensitymeasurementprototype.model.proto.Single
import com.example.peopledensitymeasurementprototype.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface

class UDPBroadcastSend(val context: Context): SendStrategy {

    private val socket by lazy {
        DatagramSocket().apply { broadcast = true }
    }

    private fun getWifiInterface(): NetworkInterface? {
        return NetworkInterface.getNetworkInterfaces().toList().find {
            it.name == "wlan0" || it.name == "eth0"
        }
    }

    private fun getBroadcastAddress(): InetAddress? {
        val iface = getWifiInterface()
        if (iface == null) return null
        return iface.interfaceAddresses.find { it.broadcast != null } ?.broadcast
    }

    override fun sendSingleLocationData(data: Single.SingleLocationData) {
        val broadcastAddress = getBroadcastAddress()

        if (broadcastAddress == null) {
            log(context, LOG_LEVEL_WARN, "UDPBroadcastSend", "Broadcast address is null")
            return
        }

        log(context, LOG_LEVEL_DEBUG, "UDPBroadcastSend", arrayOf<Any>("Send location to", broadcastAddress))

        GlobalScope.launch {

            val bytes = data.toByteArray()
            val packet = DatagramPacket(bytes, bytes.size, broadcastAddress, PORT)

            withContext(Dispatchers.IO) {
                socket.send(packet)
            }
        }
    }

    companion object {
        const val PORT = 1510
        const val MAX_MESSAGE_SIZE = 100
    }
}
