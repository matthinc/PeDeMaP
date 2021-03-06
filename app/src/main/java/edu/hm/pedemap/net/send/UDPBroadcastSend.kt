package edu.hm.pedemap.net.send

import android.content.Context
import edu.hm.pedemap.model.entity.LOG_LEVEL_DEBUG
import edu.hm.pedemap.model.entity.LOG_LEVEL_WARN
import edu.hm.pedemap.model.proto.Definitions
import edu.hm.pedemap.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface

class UDPBroadcastSend(val context: Context) : SendStrategy {

    private val socket by lazy {
        DatagramSocket().apply { broadcast = true }
    }

    /**
     * Get current WiFi Interface.
     * @return [NetworkInterface] or [null] if no interface was found
     */
    private fun getWifiInterface(): NetworkInterface? {
        return NetworkInterface.getNetworkInterfaces().toList().find {
            it.name == "wlan0" || it.name == "eth0"
        }
    }

    /**
     * Get broadcast address of the current Wifi Interface
     */
    private fun getBroadcastAddress(): InetAddress? {
        return (getWifiInterface() ?: return null).interfaceAddresses.find { it.broadcast != null }?.broadcast
    }

    /**
     * Sends bytes via a IP/UDP broadcast with the destination port [PORT]
     */
    private fun sendByteData(bytes: ByteArray) {
        val broadcastAddress = getBroadcastAddress()

        if (broadcastAddress == null) {
            log(context, LOG_LEVEL_WARN, "UDPBroadcastSend", "Broadcast address is null")
            return
        }

        log(context, LOG_LEVEL_DEBUG, "UDPBroadcastSend", arrayOf<Any>("Send location to", broadcastAddress))

        GlobalScope.launch {
            val packet = DatagramPacket(bytes, bytes.size, broadcastAddress, PORT)

            withContext(Dispatchers.IO) {
                socket.send(packet)
            }
        }
    }

    override fun sendMessage(data: Definitions.LocationMessageWrapper) {
        sendByteData(data.toByteArray())
    }

    override fun sendSingleLocationData(data: Definitions.SingleLocationData) {
        val wrapper = Definitions.LocationMessageWrapper.newBuilder().apply {
            single = data
        }.build()
        sendMessage(wrapper)
    }

    override fun sendDensityMap(data: Definitions.DensityMap) {
        val wrapper = Definitions.LocationMessageWrapper.newBuilder().apply {
            map = data
        }.build()
        sendMessage(wrapper)
    }

    override fun sendWarnMessage(data: Definitions.WarnMessage) {
        val wrapper = Definitions.LocationMessageWrapper.newBuilder().apply {
            message = data
        }.build()
        sendMessage(wrapper)
    }

    companion object {
        const val PORT = 1510
        const val MAX_MESSAGE_SIZE = 300
    }
}
