package com.example.bdeorga

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.bdeorga.notifications.NotificationReceiver

class BdeOrgaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Rappels d'événements"
            val descriptionText = "Notifications pour les événements à venir"
            val importance = NotificationManager.IMPORTANCE_HIGH

            // MODIFICATION : Utiliser le NOUVEAU Channel ID
            val channel = NotificationChannel(
                NotificationReceiver.Companion.CHANNEL_ID,
                name,
                importance
            ).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}