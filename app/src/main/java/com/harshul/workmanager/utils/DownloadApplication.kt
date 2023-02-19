package com.harshul.workmanager.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class DownloadApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "download_channel",
            "File download",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

    }

}