package com.example.peopledensitymeasurementprototype.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_DEBUG
import com.example.peopledensitymeasurementprototype.util.log

class BatteryChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        log(context, LOG_LEVEL_DEBUG, "Battery", "$level%")
    }
}
