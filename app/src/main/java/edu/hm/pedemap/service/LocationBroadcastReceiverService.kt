package edu.hm.pedemap.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import edu.hm.pedemap.model.entity.LOG_LEVEL_DEBUG
import edu.hm.pedemap.model.entity.LOG_LEVEL_WARN
import edu.hm.pedemap.net.send.UDPBroadcastSend
import edu.hm.pedemap.receiver.NetworkLocationReceiver
import edu.hm.pedemap.util.log
import java.net.BindException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class LocationBroadcastReceiverService : Service() {

    lateinit var socket: DatagramSocket

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        listen()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun listen() {
        val receiveBuffer = ByteArray(UDPBroadcastSend.MAX_MESSAGE_SIZE)

        val listenThread = object : Thread() {
            override fun run() {
                try {
                    socket = DatagramSocket(UDPBroadcastSend.PORT, InetAddress.getByName("0.0.0.0"))
                } catch (e: BindException) {
                    log(
                        this@LocationBroadcastReceiverService,
                        LOG_LEVEL_WARN,
                        "LocationBroadcastReceiverService",
                        "Address :${UDPBroadcastSend.PORT} already in use"
                    )

                    return
                }

                val packet = DatagramPacket(receiveBuffer, UDPBroadcastSend.MAX_MESSAGE_SIZE)

                while (!isInterrupted) {
                    socket.receive(packet)

                    val intent = Intent(applicationContext, NetworkLocationReceiver::class.java)

                    // Resize buffer to actual value
                    val receivedData = packet.data.take(packet.length).toByteArray()

                    log(
                        applicationContext,
                        LOG_LEVEL_DEBUG,
                        "LocationBroadcastReceiverService",
                        "Received ${packet.length} bytes of data"
                    )

                    intent.putExtra("data", receivedData)
                    sendBroadcast(intent)
                }
            }
        }

        listenThread.start()
    }
}
