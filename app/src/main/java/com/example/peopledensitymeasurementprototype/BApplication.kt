package com.example.peopledensitymeasurementprototype

import android.app.Application
import com.example.peopledensitymeasurementprototype.density.BaseDensityGrid
import com.example.peopledensitymeasurementprototype.net.send.UDPBroadcastSend

class BApplication: Application() {
    val grid = BaseDensityGrid()

    val sendLocationStrategy = UDPBroadcastSend(this)
}
