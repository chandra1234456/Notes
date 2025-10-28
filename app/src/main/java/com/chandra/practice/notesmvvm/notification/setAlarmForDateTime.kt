package com.chandra.practice.notesmvvm.notification

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import java.text.SimpleDateFormat
import java.util.*

@RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
fun setAlarmForDateTime(context: Context, dateTimeStr: String) {
    // Define your date format matching input string
    val format = SimpleDateFormat("dd-MMM-yyyy h:mm:ss a", Locale.ENGLISH)
    
    val date: Date? = try {
        format.parse(dateTimeStr)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    if (date == null) {
        println("Invalid date format!")
        return
    }

    val calendar = Calendar.getInstance().apply {
        time = date
    }

    // Create an intent for AlarmReceiver (you'll create this next)
    val intent = Intent(context, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Set exact alarm (use setExactAndAllowWhileIdle for API 23+)
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )

    println("Alarm set for: $dateTimeStr")
}
