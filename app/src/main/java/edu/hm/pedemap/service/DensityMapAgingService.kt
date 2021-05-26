package edu.hm.pedemap.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import edu.hm.pedemap.util.bApplication
import java.util.*

class DensityMapAgingService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    val task = object : TimerTask() {

        override fun run() {
            bApplication().grid.purge()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timer().scheduleAtFixedRate(task, 1000L, 1000L)
        return super.onStartCommand(intent, flags, startId)
    }
}
