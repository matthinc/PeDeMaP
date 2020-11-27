package com.example.peopledensitymeasurementprototype.net.send

import android.content.Context
import com.example.peopledensitymeasurementprototype.model.proto.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.NetworkInterface

class UDPBroadcastSend(val context: Context): SendStrategy {

    private val socket by lazy {
        DatagramSocket().apply { broadcast = true }
    }

    private val broadcastAddress by lazy {
        NetworkInterface.getNetworkInterfaces().toList()
            .filter { it.name!!.contentEquals("wlan0") || it.name!!.contentEquals("eth0") }
            .map { it.interfaceAddresses.filter { it.broadcast != null }.first().broadcast }.first()
    }

    override fun sendSingleLocationData(data: Single.SingleLocationData) {
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
