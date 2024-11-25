package com.chandra.practice.notesmvvm.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.chandra.practice.notesmvvm.R
import java.util.Date

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("reminderTitle")
        val time = intent.getLongExtra("reminderTime", 0)

        // Create a notification when the alarm triggers
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification Channel for Android 8.0 (Oreo) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setContentTitle("Reminder: $title")
            .setContentText("Scheduled for: ${Date(time)}")
            .setSmallIcon(R.drawable.ic_calendar)
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager.notify(time.toInt(), notification)
    }
}
