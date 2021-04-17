package edu.hm.pedemap.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import edu.hm.pedemap.model.entity.LOG_LEVEL_DEBUG
import edu.hm.pedemap.model.entity.LOG_LEVEL_ERROR
import edu.hm.pedemap.model.entity.LOG_LEVEL_WARN
import edu.hm.pedemap.receiver.ActivityReceiver
import edu.hm.pedemap.util.log

class ActivityRecognitionService : Service() {

    override fun onBind(intent: Intent) = null

    private val pendingIntent: PendingIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, ActivityReceiver::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    private val activityRecognition by lazy { ActivityRecognitionClient(this) }

    private val relevantActivityTypes = arrayOf(
        DetectedActivity.WALKING,
        DetectedActivity.RUNNING,
        DetectedActivity.STILL
    )

    private val relevantTransitions = arrayOf(
        ActivityTransition.ACTIVITY_TRANSITION_ENTER,
        ActivityTransition.ACTIVITY_TRANSITION_EXIT
    )

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startActivityRecognition()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        stopActivityRecognition()
        super.onDestroy()
    }

    private fun startActivityRecognition() {
        log(applicationContext, LOG_LEVEL_DEBUG, "Activity", "Start activity recognition")

        val request = ActivityTransitionRequest(
            relevantActivityTypes.flatMap { type ->
                relevantTransitions.map { transition ->
                    ActivityTransition.Builder()
                        .setActivityTransition(transition)
                        .setActivityType(type).build()
                }
            }
        )

        val task = activityRecognition.requestActivityTransitionUpdates(request, pendingIntent)

        task.addOnSuccessListener {
            log(applicationContext, LOG_LEVEL_DEBUG, "Activity", "Start activity recognition: Successful")
        }

        task.addOnFailureListener {
            log(applicationContext, LOG_LEVEL_ERROR, "Activity", "Start activity recognition: Error ${it.message}")
        }
    }

    private fun stopActivityRecognition() {
        log(applicationContext, LOG_LEVEL_WARN, "Activity", "Stop activity recognition")
        activityRecognition.removeActivityTransitionUpdates(pendingIntent)
    }
}
