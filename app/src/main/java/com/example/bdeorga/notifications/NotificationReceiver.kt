package com.example.bdeorga.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.bdeorga.R

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "event_reminders_alarm" // Nouveau Channel ID
        const val NOTIFICATION_ID_KEY = "notification_id"
        const val EVENT_TITLE_KEY = "event_title"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val eventTitle = intent.getStringExtra(EVENT_TITLE_KEY) ?: "Événement"
        val notificationId = intent.getIntExtra(NOTIFICATION_ID_KEY, 0)

        showNotification(context, eventTitle, notificationId)
    }

    private fun showNotification(context: Context, eventTitle: String, notificationId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = "Rappel: $eventTitle"
        val text = "Votre événement commence bientôt !"

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}