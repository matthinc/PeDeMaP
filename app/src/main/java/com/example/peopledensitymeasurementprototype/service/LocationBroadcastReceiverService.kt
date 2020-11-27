package com.example.peopledensitymeasurementprototype.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import com.example.peopledensitymeasurementprototype.model.proto.Single
import com.example.peopledensitymeasurementprototype.net.send.UDPBroadcastSend
import com.example.peopledensitymeasurementprototype.receiver.NetworkLocationReceiver
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.concurrent.thread

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
                socket = DatagramSocket(UDPBroadcastSend.PORT, InetAddress.getByName("0.0.0.0"))
                val packet = DatagramPacket(receiveBuffer, UDPBroadcastSend.MAX_MESSAGE_SIZE)

                while (!isInterrupted) {
                    socket.receive(packet)

                    val intent = Intent(applicationContext, NetworkLocationReceiver::class.java)

                    // Resize buffer to actual value
                    val receivedData = packet.data.take(packet.length).toByteArray()

                    intent.putExtra("data", receivedData)
                    sendBroadcast(intent)
                }
            }
        }

        listenThread.start()
    }
}
