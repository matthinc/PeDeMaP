package com.example.peopledensitymeasurementprototype.util

import android.content.Context
import android.content.Intent
import com.example.peopledensitymeasurementprototype.BApplication
import java.util.*
import kotlin.concurrent.timerTask

fun Context.bApplication(): BApplication = applicationContext as BApplication

fun Context.restartService(intent: Intent) {
    stopService(intent)

    Timer().schedule(
        timerTask {
            startService(intent)
        },
        1500
    )
}
