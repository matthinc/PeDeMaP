package edu.hm.pedemap.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import edu.hm.pedemap.model.entity.LOG_LEVEL_DEBUG
import edu.hm.pedemap.util.log

class BatteryChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        log(context, LOG_LEVEL_DEBUG, "Battery", "$level%")
    }
}
