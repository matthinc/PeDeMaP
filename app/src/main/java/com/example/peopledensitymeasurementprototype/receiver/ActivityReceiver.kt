package com.example.peopledensitymeasurementprototype.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.peopledensitymeasurementprototype.model.entity.LOG_LEVEL_INFO
import com.example.peopledensitymeasurementprototype.util.log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

class ActivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)

            result?.transitionEvents?.forEach {

                val typeString = when (it.activityType) {
                    DetectedActivity.WALKING -> "Walking"
                    DetectedActivity.RUNNING -> "Running"
                    DetectedActivity.STILL -> "Still"
                    else -> "Unknown"
                }

                val transitionString = when (it.transitionType) {
                    ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "Exit"
                    ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "Enter"
                    else -> "Unknown transition"
                }

                log(
                    context,
                    LOG_LEVEL_INFO,
                    "Activity Transition",
                    arrayOf<Any>(
                        typeString,
                        transitionString
                    )
                )
            }
        }
    }
}
