package com.poc.firstprojectinfnet.home.data.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.poc.firstprojectinfnet.R

class NotificationTaskReceiver : BroadcastReceiver() {

    companion object {
        private const val TITLE_NOTIFICATION = "Sua tarefa"
        private const val DESCRIPTION_NOTIFICATION = "Data de expiração chegou ao fim!"
        private const val CHANEL_ID_NOTIFICATION = "TasksChannel"

        @RequiresApi(Build.VERSION_CODES.O)
        fun setAndCreateNotificationChannel(context: Context) {
            val name = "Notification Channel Tasks"
            val descriptionText = "Channel for Tasks Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANEL_ID_NOTIFICATION, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val titleTask = intent?.getStringExtra("title")
        context?.let {
            val notification: NotificationCompat.Builder = NotificationCompat.Builder(it, CHANEL_ID_NOTIFICATION)
                .setSmallIcon(R.drawable.notepadlogo)
                .setContentTitle("$TITLE_NOTIFICATION $titleTask expirou!")
                .setContentText(DESCRIPTION_NOTIFICATION)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            val manager = it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(0, notification.build())
        }
    }


}