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
        // Récupérer les données de l'alarme
        val eventTitle = intent.getStringExtra(EVENT_TITLE_KEY) ?: "Événement"
        val notificationId = intent.getIntExtra(NOTIFICATION_ID_KEY, 0)

        // Afficher la notification
        showNotification(context, eventTitle, notificationId)
    }

    private fun showNotification(context: Context, eventTitle: String, notificationId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = "Rappel: $eventTitle"
        val text = "Votre événement commence dans 1 jour !"

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            // !! IMPORTANT !! Remplacez ic_mail par une vraie icône de notification
            .setSmallIcon(R.drawable.ic_mail)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}